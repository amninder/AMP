/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package player;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Map;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JOptionPane;
import player.header.ID3v2FormatException;
import player.header.ID3v2Tag;
import player.header.MPEGAudioFrameHeader;
import player.header.NoMPEGFramesException;
/**
 *
 * @author Amninder Singh
 */
public class AudioProperty {

    public static File file=null;
    public static AudioFileFormat fileFormat=null;
    public static AudioFormat format=null;
    public static Map properties=null;
    public static String authorName=null;
    private static final int TAG_SIZE = 128;
    private static final int TITLE_SIZE = 30;
    private static final int ARTIST_SIZE = 30;
    private static final int ALBUM_SIZE = 30;
    private static final int YEAR_SIZE = 4;
    private static final int COMMENT_SIZE = 29;
    private static final int TRACK_LOCATION = 126;
    private static final int GENRE_LOCATION = 127;
    private static final int MAX_GENRE = 255;
    private static final int MAX_TRACK = 255;
    private static final String ENC_TYPE = "Cp437";
    private static final String TAG_START = "TAG";

    private static File mp3 = null;
    private static boolean headerExists = false;
    private static String title = null;
    private static String artist = null;
    private static String album = null;
    private static String year = null;
    private static String comment = null;
    private static int genre;
    private static int track;
    public static void getrate() throws UnsupportedAudioFileException, IOException
    {
        file=FileManager.retFile();
        System.out.println(file.toString());
        fileFormat=AudioSystem.getAudioFileFormat(file);
        properties=fileFormat.properties();
        authorName=(String) properties.get("copyright");
        JOptionPane.showMessageDialog(null, authorName);
    }
    public static void encoding() throws FileNotFoundException, IOException
    {
        RandomAccessFile raf= new RandomAccessFile(FileManager.retFile(),"r");
        if(raf.length()>128)
        {
            raf.seek(raf.length()-128);
            byte[] buf= new byte[3];
            if(raf.read(buf)!=3)
            {
                System.out.println("buf not equal to 3");
            }
            else
            {
                String result=new String(buf,0,3,"Cp437");
                JOptionPane.showMessageDialog(null, result);
            }
        }
        raf.close();
    }
    public static void readTag(File file) throws FileNotFoundException, IOException
    {
        RandomAccessFile raf = new RandomAccessFile(file,"r");
        raf.seek(raf.length()-TAG_SIZE);
        byte[]buf=new byte[TAG_SIZE];
        raf.read(buf,0,TAG_SIZE);
	String tag = new String( buf, 0, TAG_SIZE, ENC_TYPE ); 
	int start = TAG_START.length();
	title = tag.substring( start, start += TITLE_SIZE );
	artist = tag.substring( start, start += ARTIST_SIZE );
	album = tag.substring( start, start += ALBUM_SIZE );
	year = tag.substring( start, start += YEAR_SIZE );
	comment = tag.substring( start, start += COMMENT_SIZE );
	track = (int)tag.charAt( TRACK_LOCATION );
	genre = (int)tag.charAt( GENRE_LOCATION );
        PLAYERView.artist_label.setText("Artist: "+(String)artist);
        PLAYERView.artist_label.setToolTipText(artist);
        PLAYERView.artist_textfield.setText("Artist: "+artist);
        
        PLAYERView.album_label.setText("Album: "+album+"");
        PLAYERView.album_label.setToolTipText(album);
        PLAYERView.album_textfield.setText("Album: "+album);
        
        PLAYERView.year_label.setText("Year: "+year+"");
        PLAYERView.year_label.setToolTipText(year);
        PLAYERView.year_textfield.setText("Year: "+year);
        
        PLAYERView.track_label.setText("Track: "+track+"");
        PLAYERView.track_label.setToolTipText(String.valueOf(track));
        PLAYERView.track_textfield.setText("Track: "+String.valueOf(track));
        
        PLAYERView.genre_label.setText("Genre: "+Gener.getGenre(genre)+"");
        PLAYERView.genre_label.setToolTipText(Gener.getGenre(genre));
        PLAYERView.genre_textfield.setText("Genre: "+Gener.getGenre(genre));
        
        raf.close();        
    }
    public static void removeTag()
    {
        PLAYERView.artist_label.setText("Artist ");
        PLAYERView.artist_textfield.setText("Artist ");
        
        PLAYERView.album_label.setText("Album ");
        PLAYERView.album_textfield.setText("Album ");
        
        PLAYERView.year_label.setText("Year ");
        PLAYERView.year_textfield.setText("Year ");
        
        PLAYERView.track_label.setText("Track: ");
        PLAYERView.track_textfield.setText("Track ");
        
        PLAYERView.genre_label.setText("Genre ");
        PLAYERView.genre_textfield.setText("Genre ");
    }
    public static void testingBit() throws FileNotFoundException, IOException, ID3v2FormatException, NoMPEGFramesException
    {
             File mp = FileManager.retFile();
            ID3v2Tag id3v2 = new ID3v2Tag(mp);    
            MPEGAudioFrameHeader head = new MPEGAudioFrameHeader(mp,id3v2.getSize());
            long datasize = (mp.length() * 8) - id3v2.getSize();
            long bps = head.getBitRate() * 1000;
            JOptionPane.showMessageDialog(null, String.valueOf(CreatePlayer.player.getDuration().getSeconds())+" / "+String.valueOf(datasize/bps));
            
    }
    public static void getProperties()
    {
        int r;
    }
}
