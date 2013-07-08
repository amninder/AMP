/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package player;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import player.header.BinaryParser;

/**
 *
 * @author Amninder Singh
 */
public class MPEGAudioFrameHeader {
      private static final int HEADER_SIZE = 4;
    private static final int[][] bitrateTable = { 
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
    private static final int[][] sampleTable = {
	{ 44100, 22050, 11025 },
	{ 48000, 24000, 12000 },
	{ 32000, 16000, 8000 }, 
	{ -1, -1, -1 } };
    private static final String[] versionLabels = { "MPEG Version 2.5", null, 
					     "MPEG Version 2.0", 
					     "MPEG Version 1.0" };
    private static final String[] layerLabels = { null, "Layer III", "Layer II", 
					   "Layer I" };
    private static final String[] channelLabels = { "Stereo", "Joint Stereo (STEREO)",
					     "Dual Channel (STEREO)", 
					     "Single Channel (MONO)" };
    private static final String[] emphasisLabels = { "none", "50/15 ms", null, 
					      "CCIT J.17" };
    private static final int MPEG_V_25 = 0;
    private static final int MPEG_V_2 = 2;
    private static final int MPEG_V_1 = 3;
    private static final int MPEG_L_3 = 1;
    private static final int MPEG_L_2 = 2;
    private static final int MPEG_L_1 = 3;

    private static File mp3 = null;
    private static int version;
    private static int layer;
    private static int bitRate;
    private static int sampleRate;
    private static int channelMode;
    private static boolean copyrighted;
    private static boolean crced;
    private static boolean original;
    private static int emphasis; 
    
    
    public static long findFrame(int offset) throws FileNotFoundException, IOException
    {
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

    private static void findBitRate(int bitrateIndex) {
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

    private static void findSampleRate(int sampleIndex) {
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
     public static void readHeader( long location ) throws FileNotFoundException, IOException {
         

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
        
        PLAYERView.version_textfield.setText("Version: "+String.valueOf(MPEGAudioFrameHeader.versionLabels[version]));
        
        PLAYERView.layer_textfield.setText("MPEG Layer: "+String.valueOf(layer));
                
        PLAYERView.channel_textfield.setText("Channel: "+MPEGAudioFrameHeader.channelLabels[channelMode]);
                
        if(copyrighted)
        {
            PLAYERView.copyrighted_textfield.setText("Copyrighted: TRUE");
        }
        else
        {
            PLAYERView.copyrighted_textfield.setText("Copyrighted: FALSE");
        }
        
        System.out.println(crced);
        PLAYERView.crced_textfield.setText("CRC: "+String.valueOf(crced));
        
        System.out.println(original);
        
        System.out.println(MPEGAudioFrameHeader.emphasisLabels[emphasis]);
        PLAYERView.emphasis_textfield.setText("Emphasis: "+MPEGAudioFrameHeader.emphasisLabels[emphasis]);
        
        System.out.println("------------------------------");
    }
    public static void audioFrameHeader(File mp3, int offset) throws FileNotFoundException, IOException, Exception
    {
        	MPEGAudioFrameHeader.mp3 = mp3;

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
	    throw new Exception();
	}
    }
    public static void removeAudioFrameHeader()
    {
        PLAYERView.version_textfield.setText("Version: ");
        
        PLAYERView.layer_textfield.setText("MPEG Layer: ");
                
        PLAYERView.channel_textfield.setText("Channel: ");
                
        PLAYERView.copyrighted_textfield.setText("Copyrighted: ");
        
        PLAYERView.crced_textfield.setText("CRC: ");
        
        PLAYERView.emphasis_textfield.setText("Emphasis: ");
    }

}
