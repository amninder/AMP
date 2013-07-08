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
public class MPEGAudioFrameHeader {
        private final int HEADER_SIZE = 4;
    private final int[][] bitrateTable = { 
	{ -1, -1, -1, -1, -1 }, 
	{ 32, 32, 32, 32, 8 },
	{ 64, 48, 40, 48, 16 },
	{ 96, 56, 48, 56, 24 },
	{ 128, 64, 56, 64, 32 },
	{ 160, 80, 64, 80, 40 },
	{ 192, 96, 80, 96, 48 },
	{ 224, 112, 96, 112, 56 },
	{ 256, 128, 112, 128, 64 },
	{ 288, 160, 128, 144, 80 },
	{ 320, 192, 160, 160, 96 },
	{ 352, 224, 192, 176, 112 },
	{ 384, 256, 224, 192, 128 },
	{ 416, 320, 256, 224, 144 },
	{ 448, 384, 320, 256, 160 },
	{ -1, -1, -1, -1, -1 } };
    private final int[][] sampleTable = {
	{ 44100, 22050, 11025 },
	{ 48000, 24000, 12000 },
	{ 32000, 16000, 8000 }, 
	{ -1, -1, -1 } };
    private final String[] versionLabels = { "MPEG Version 2.5", null, 
					     "MPEG Version 2.0", 
					     "MPEG Version 1.0" };
    private final String[] layerLabels = { null, "Layer III", "Layer II", 
					   "Layer I" };
    private final String[] channelLabels = { "Stereo", "Joint Stereo (STEREO)",
					     "Dual Channel (STEREO)", 
					     "Single Channel (MONO)" };
    private final String[] emphasisLabels = { "none", "50/15 ms", null, 
					      "CCIT J.17" };
    private final int MPEG_V_25 = 0;
    private final int MPEG_V_2 = 2;
    private final int MPEG_V_1 = 3;
    private final int MPEG_L_3 = 1;
    private final int MPEG_L_2 = 2;
    private final int MPEG_L_1 = 3;

    private File mp3 = null;
    private int version;
    private int layer;
    private int bitRate;
    private int sampleRate;
    private int channelMode;
    private boolean copyrighted;
    private boolean crced;
    private boolean original;
    private int emphasis;
    
    public MPEGAudioFrameHeader( File mp3 )throws NoMPEGFramesException, FileNotFoundException, IOException {

	this( mp3, 0 );
    }
     public MPEGAudioFrameHeader( File mp3, int offset )throws NoMPEGFramesException, FileNotFoundException, IOException {

	this.mp3 = mp3;

	version = -1;
	layer = -1;
	bitRate = -1;
	sampleRate = -1;
	channelMode = -1;
	copyrighted = false;
	crced = false;
	original = false;
	emphasis = -1;

	long location = findFrame( offset );
	
	if( location != -1 ) {
	    readHeader( location );
	}
	else {
	    throw new NoMPEGFramesException();
	}
    }
        private long findFrame( int offset ) 
	throws FileNotFoundException, IOException {

	RandomAccessFile raf = new RandomAccessFile( mp3, "r" );
	byte test;
	long loc = -1;
	raf.seek( offset );

	while( loc == -1 ) {
	    test = raf.readByte();

	    if( BinaryParser.matchPattern( test, "11111111" ) ) {
		test = raf.readByte();
		
		if( BinaryParser.matchPattern( test, "111xxxxx" ) ) {
		    loc = raf.getFilePointer() - 2;
		}
	    }
	}

	raf.close();

	return loc;
    }
        private void readHeader( long location ) 
	throws FileNotFoundException, IOException {

	RandomAccessFile raf = new RandomAccessFile( mp3, "r" );
	byte[] head = new byte[HEADER_SIZE];
	raf.seek( location );

	if( raf.read( head ) != HEADER_SIZE ) {
	    throw new IOException("Error reading MPEG frame header.");
	}

	version = BinaryParser.convertToDecimal( head[1], 3, 4 );
	layer = BinaryParser.convertToDecimal( head[1], 1, 2 );
	findBitRate( BinaryParser.convertToDecimal( head[2], 4, 7 ) );
	findSampleRate( BinaryParser.convertToDecimal( head[2], 2, 3 ) );
	channelMode = BinaryParser.convertToDecimal( head[3], 6, 7 );
	copyrighted = BinaryParser.bitSet( head[3], 3 );
	crced = !BinaryParser.bitSet( head[1], 0 );
	original = BinaryParser.bitSet( head[3], 2 );
	emphasis = BinaryParser.convertToDecimal( head[3], 0, 1 );
    }
            private void findBitRate( int bitrateIndex ) {
	int ind = -1;

	if( version == MPEG_V_1 ) {
	    if( layer == MPEG_L_1 ) {
		ind = 0;
	    }
	    else if( layer == MPEG_L_2 ) {
		ind = 1;
	    }
	    else if( layer == MPEG_L_3 ) {
		ind = 2;
	    }
	}
	else if( (version == MPEG_V_2) || (version == MPEG_V_25) ) {
	    if( layer == MPEG_L_1 ) {
		ind = 3;
	    }
	    else if( (layer == MPEG_L_2) || (layer == MPEG_L_3) ) {
		ind = 4;
	    }
	}
  
	if( (ind != -1) && (bitrateIndex >= 0) && (bitrateIndex <= 15) ) {
	    bitRate = bitrateTable[bitrateIndex][ind];
	}
    }
            
        private void findSampleRate( int sampleIndex ) {
	int ind = -1;

	switch( version ) {
	case MPEG_V_1:
	    ind = 0;
	    break;
	case MPEG_V_2:
	    ind = 1;
	    break;
	case MPEG_V_25:
	    ind = 2;
	}

	if( (ind != -1) && (sampleIndex >= 0) && (sampleIndex <= 3) ) {
	    sampleRate = sampleTable[sampleIndex][ind];
	}
    }
        
    @Override
            public String toString() {
	return getVersion() + " " + getLayer() + "\nBitRate:\t\t\t" + 
	    getBitRate() + "kbps\nSampleRate:\t\t\t" + getSampleRate() +
	    "Hz\nChannelMode:\t\t\t" + getChannelMode() + 
	    "\nCopyrighted:\t\t\t" + isCopyrighted() + "\nOriginal:\t\t\t" +
	    isOriginal() + "\nCRC:\t\t\t\t" + isProtected() + 
	    "\nEmphasis:\t\t\t" + getEmphasis();
    }
        public String getVersion() {
	String str = null;
	
	if( (version >= 0) && (version < versionLabels.length) ) {
	    str = versionLabels[version];
	}

	return str;
    }
            public String getLayer() {
	String str = null;

	if( (layer >= 0) && (layer < layerLabels.length) ) {
	    str = layerLabels[layer];
	}

	return str;
    }
                public String getChannelMode() {
	String str = null;

	if( (channelMode >= 0) && (channelMode < channelLabels.length) ) {
	    str = channelLabels[channelMode];
	}
	    
	return str;
    }
        public int getBitRate() {
	return bitRate;
    }
       public int getSampleRate() {
	return sampleRate;
    }
           public boolean isCopyrighted() {
	return copyrighted;
    }
               public boolean isProtected() {
	return crced;
    }
               
        public boolean isOriginal() {
	return original;
    }
            public String getEmphasis() {
	String str = null;

	if( (emphasis >= 0) && (emphasis < emphasisLabels.length) ) {
	    str = emphasisLabels[emphasis];
	}

	return str;
    }
               public boolean isMP3() {
	return (layer == MPEG_L_3);
    }
        
}
