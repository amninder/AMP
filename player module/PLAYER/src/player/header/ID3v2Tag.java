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
public class ID3v2Tag {
    private final String ENC_TYPE = "ISO-8859-1";
    private File mp3 = null;
    private ID3v2Header head = null;
    private ID3v2ExtendedHeader ext_head = null;
//    private ID3v2Frames frames = null;
//    private ID3v2Footer foot = null;
    private int padding;
    private int origSize;
    private int origPadding;
    private boolean exists;
    private ID3v2Frames frames;
    private ID3v2Footer foot;
    
     public ID3v2Tag( File mp3 ) throws FileNotFoundException, IOException, ID3v2FormatException {


	this.mp3 = mp3;


	frames = new ID3v2Frames();

	head = new ID3v2Header( mp3 );

	padding = 0;

	exists = head.headerExists();


	if( exists )
	{

	    if( head.getExtendedHeader() )
	  {

		ext_head = new ID3v2ExtendedHeader( mp3 );

	   }

	    if( head.getFooter() )
	   {

		foot = new ID3v2Footer( mp3,head.getTagSize() + head.getHeaderSize() );

	   }


	   RandomAccessFile in = null;


	   try
 	  {

		in = new RandomAccessFile( mp3, "r" );


		// For now only support id3v2.3.0 or greater

		if( head.getMajorVersion() >= 3 )
 		{

		    parseFrames( in );

		}

	    }

	    finally
	 {

		if( in != null )
 		{

		    in.close();

		}

	    }


	    origSize = getSize();

	    origPadding = getPadding();

}
     }
         private void parseFrames( RandomAccessFile raf ) 
	throws FileNotFoundException, IOException, ID3v2FormatException
 {


	int offset = head.getHeaderSize();

	int framesLength = head.getTagSize();

	int bytesRead = 0;

	int curLength = 0;

	ID3v2Frame frame = null;

	String id = null;

	byte[] buf;

	byte[] flags;

	boolean done = false;


	if( head.getExtendedHeader() )
 	{

	    framesLength -= ext_head.getSize();

	    offset += ext_head.getSize();

	}


	raf.seek( offset );


	while( (bytesRead < framesLength) && !done )
 	{


	    buf = new byte[4];

	    bytesRead += raf.read( buf );


	    if( buf[0] != '\0' )
 	   {

		id = new String( buf );

		buf = new byte[4];

		bytesRead += raf.read( buf );

		curLength = BinaryParser.convertToInt( buf );

		flags = new byte[2];

		bytesRead += raf.read( flags );

		buf = new byte[curLength];

		bytesRead += raf.read( buf );

		frame = new ID3v2Frame( id, flags, buf );

		frames.put( id, frame );

	    }

	    else
 	   {

		done = true;

		padding = framesLength - bytesRead - buf.length;

	    }

	}

    }
         
        public int getSize(){
	int retval = head.getTagSize() + head.getHeaderSize();
	if( head.getFooter() ) 
	{
	    retval += foot.getFooterSize();
	}
	return retval;
        }
        public int getPadding(){
	return padding;
        }
}
