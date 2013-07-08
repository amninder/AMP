/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package player.header;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 *
 * @author Amninder Singh
 */
class ID3v2ExtendedHeader {
    private final int EXT_HEAD_LOCATION = 10;
    private final int MIN_SIZE = 6;
    private final int CRC_SIZE = 5;
    private final int[] MAX_TAG_FRAMES_TABLE = { 128, 64, 32, 32 };
    private final int[] MAX_TAG_SIZE_TABLE = {8000000, 1024000, 320000, 32000};
    private final int[] MAX_TEXT_SIZE_TABLE = { -1, 1024, 128, 30 };

    private File mp3 = null;
    private int size;
    private int numFlagBytes;
    private boolean update;
    private boolean crced;
    private byte[] crc;
    private int maxFrames;
    private int maxTagSize;
    private boolean textEncode;
    private int maxTextSize;
    private boolean imageEncode;
    private int imageRestrict;
    
        public ID3v2ExtendedHeader( File mp3 ) 
	throws FileNotFoundException, IOException, ID3v2FormatException {

	this.mp3 = mp3;

	size = 0;
	numFlagBytes = 0;
	update = false;
	crced = false;
	crc = new byte[CRC_SIZE];
	maxFrames = -1;
	maxTagSize = -1;
	textEncode = false;
	maxTextSize = -1;
	imageEncode = false;
	imageRestrict = -1;

	readExtendedHeader();
        }
            private void readExtendedHeader() 
	throws FileNotFoundException, IOException, ID3v2FormatException {

	RandomAccessFile raf = new RandomAccessFile( mp3, "r" );
	raf.seek( EXT_HEAD_LOCATION );

	byte[] buf = new byte[4];
	if( raf.read( buf ) != buf.length ) {
	    throw new IOException("Error reading extended header:size");
	}

	size = BinaryParser.convertToInt( buf );
	if( size < MIN_SIZE ) {
	    throw new ID3v2FormatException( "The extended header size data" +
				" is less than the minimum required size.");
	}

	buf = new byte[1];
	if( raf.read( buf ) != buf.length ) {
	    throw new IOException("Error reading extended header:numflags");
	}

	numFlagBytes = (int)buf[0];
	buf = new byte[numFlagBytes+1];

	if( raf.read( buf ) != buf.length ) {
	    throw new IOException( "Error reading extended header:flags" );
	}

	parseFlags( buf );

	raf.close();
    }
                private void parseFlags( byte[] flags ) throws ID3v2FormatException {
	int bytesRead = 1;

	if( BinaryParser.bitSet( flags[0], 6 ) ) {
	    update = true;
	    bytesRead += 1;
	}
	if( BinaryParser.bitSet( flags[0], 5 ) ) {
	    crced = true;
	    bytesRead += 1;
	    for( int i = 0; i < crc.length; i++ ) {
		crc[i] = flags[bytesRead++];
	    }
	}
	if( BinaryParser.bitSet( flags[0], 4 ) ) {
	    bytesRead += 1;
	    maxTagSize = BinaryParser.convertToDecimal( 
						flags[bytesRead], 6, 7 );
	    textEncode = BinaryParser.bitSet( flags[bytesRead], 5 );
	    maxTextSize = BinaryParser.convertToDecimal( 
						flags[bytesRead], 3, 4 );
	    imageEncode = BinaryParser.bitSet( flags[bytesRead], 2 );
	    imageRestrict = BinaryParser.convertToDecimal( 
						flags[bytesRead], 0, 1 );
	    bytesRead += 1;
	}

	if( bytesRead != numFlagBytes ) {
	    throw new ID3v2FormatException("The number of found flag bytes " +
					   "in the extended header is not " +
					   "equal to the number specified " +
					   "in the extended header." );
	}
    }
                    public byte[] getBytes() {
	byte[] b = new byte[size];
	int bytesCopied = 0;

	System.arraycopy( BinaryParser.convertToBytes(size), 0, b, 
			  bytesCopied, 4 );
	bytesCopied += 4;
	b[bytesCopied++] = (byte)numFlagBytes;
	System.arraycopy( getFlagBytes(), 0, b, bytesCopied, numFlagBytes );
	bytesCopied += numFlagBytes;

	return b;
    }
    private byte[] getFlagBytes() {
	byte[] b = new byte[numFlagBytes];
	int bytesCopied = 1;
	b[0] = 0;

	if( update ) {
	    b[0] = BinaryParser.setBit( b[0], 7 );
	    b[bytesCopied++] = 0;
	}
	if( crced ) {
	    b[0] = BinaryParser.setBit( b[0], 6 );
	    b[bytesCopied++] = (byte)crc.length;
	    System.arraycopy( crc, 0, b, bytesCopied, crc.length );
	    bytesCopied += crc.length;
	}
	if( (maxTagSize != -1) || textEncode || (maxTextSize != -1) || 
	    imageEncode || (imageRestrict != -1) ) {
	    
	    b[0] = BinaryParser.setBit( b[0], 5 );
	    b[bytesCopied++] = 0x01;
	    byte restrict = 0;
	    if( maxTagSize != -1 ) {
		if( BinaryParser.bitSet( (byte)maxTagSize, 0 ) ) {
		    restrict = BinaryParser.setBit( restrict, 6 );
		}
		if( BinaryParser.bitSet( (byte)maxTagSize, 1 ) ) {
		    restrict = BinaryParser.setBit( restrict, 7 );
		}
	    }
	    if( textEncode ) {
		restrict = BinaryParser.setBit( restrict, 5 );
	    }
	    if( maxTextSize != -1 ) {
		if( BinaryParser.bitSet( (byte)maxTextSize, 0 ) ) {
		    restrict = BinaryParser.setBit( restrict, 3 );
		}
		if( BinaryParser.bitSet( (byte)maxTextSize, 1 ) ) {
		    restrict = BinaryParser.setBit( restrict, 4 );
		}
	    }
	    if( imageEncode ) {
		restrict = BinaryParser.setBit( restrict, 2 );
	    }
	    if( imageRestrict != -1 ) {
		if( BinaryParser.bitSet( (byte)imageRestrict, 0 ) ) {
		    restrict = BinaryParser.setBit( restrict, 0 );
		}
		if( BinaryParser.bitSet( (byte)imageRestrict, 1 ) ) {
		    restrict = BinaryParser.setBit( restrict, 1 );
		}
	    }

	    b[bytesCopied++] = restrict;
	}


	return b;
    }
        public int getSize() {
	return size;
    }
            public int getNumFlagBytes() {
	return numFlagBytes;
    }
                public int getMaxFrames() {
	int retval = -1;

	if( (maxTagSize >= 0) && (maxTagSize < MAX_TAG_FRAMES_TABLE.length) ) {
	    retval = MAX_TAG_FRAMES_TABLE[maxTagSize];
	}

	return retval;
    }
    public int getMaxTagSize() {
	int retval = -1;

	if( (maxTagSize >= 0) && (maxTagSize < MAX_TAG_SIZE_TABLE.length) ) {
	    retval = MAX_TAG_SIZE_TABLE[maxTagSize];
	}

	return retval;
    }
     public boolean getTextEncode() {
	return textEncode;
    }

    /**
     * Returns the maximum length of a string if set or -1
     *
     * @return the maximum length of a string if set or -1
     */
    public int getMaxTextSize() {
	int retval = -1;

	if( (maxTextSize >= 0) && (maxTextSize < MAX_TEXT_SIZE_TABLE.length)) {
	    retval = MAX_TEXT_SIZE_TABLE[maxTextSize];
	}

	return retval;
    }

    /**
     * Returns true if the image encode flag is set
     *
     * @return true if the image encode flag is set
     */
    public boolean getImageEncode() {
	return imageEncode;
    }

    /**
     * Returns the value of the image restriction field or -1 if not set
     *
     * @return the value of the image restriction field or -1 if not set
     */
    public int getImageRestriction() {
	return imageRestrict;
    }

    /**
     * Returns true if this tag is an update of a previous tag
     *
     * @return true if this tag is an update of a previous tag
     */
    public boolean getUpdate() {
	return update;
    }

    /**
     * Returns true if CRC information is provided for this tag
     *
     * @return true if CRC information is provided for this tag
     */
    public boolean getCRCed() {
	return crced;
    }

    /**
     * If there is crc data in the extended header, then the attached 5 byte
     * crc will be returned.  An empty array will be returned if this has
     * not been set.
     *
     * @return the attached crc data if there is any
     */
    public byte[] getCRC() {
	return crc;
    }

    /**
     * Returns a string representation of this object that contains all
     * information within.
     *
     * @return a string representation of this object
     */
    @Override
    public String toString() {
	return "ExtendedSize:\t\t\t" + getSize() + " bytes" + 
	    "\nNumFlagBytes:\t\t\t" + getNumFlagBytes() + 
	    "\nUpdated:\t\t\t" + getUpdate() + "\nCRC:\t\t\t\t" + getCRCed() +
	    "\nMaxFrames:\t\t\t" 
	    + getMaxFrames() + "\nMaxTagSize:\t\t\t" + getMaxTagSize() +
	    "\nTextEncoded:\t\t\t" + getTextEncode() + "\nMaxTextSize:\t\t\t"
	    + getMaxTextSize() + "\nImageEncoded:\t\t\t" + getImageEncode()
	    + "\nImageRestriction:\t\t" + getImageRestriction();
    }
}
