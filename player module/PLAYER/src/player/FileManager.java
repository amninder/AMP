/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package player;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.media.CannotRealizeException;
import javax.media.NoPlayerException;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author Amninder Singh
 */
public class FileManager {

    public static File file;
    public static JFileChooser filechooser;
    public static FileNameExtensionFilter filter;
    public static File tempPlaylist=null;
    public static String tempPlaylistName="tempPlaylist.txt";
    public static String[] Tracks= new String[100];
    public static int index=0;
    private final String[] okFileExtension= new String[]{".mp3",".mpeg"};


    public static void createTempPlaylist()
    {
        boolean exists = (new File(tempPlaylistName)).exists();
        if(exists==true)
        {
            System.out.println("File is already created");
        }
        else
        {
            tempPlaylist= new File(tempPlaylistName);

            tempPlaylist.deleteOnExit();
        }
    }
    public static void writeOnTempPlaylist(File file)
    {
        createTempPlaylist();
        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new FileWriter(tempPlaylist,true));
            out.write(file.toString());
            out.close();
        } catch (IOException ex) {
            Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                out.close();
            } catch (IOException ex) {
                Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static File getFile() throws IOException, NoPlayerException, CannotRealizeException
    {
        //private Static FileFilter mp3File;
//        if(CreatePlayer.player!=null)
//        {
//            CreatePlayer.destroyPlayer();
//        }
        if(PlayList.playlist==null)
        {
            PLAYERView.playlist_scrollpane.getViewport().add(PlayList.list());
        }
            filechooser = new JFileChooser();
           filechooser.setFileFilter(new MP3FileFilter() );
           // filechooser.setFileFilter(new AVIFileFilter());
            //filter = new FileNameExtensionFilter("mp3");
            //filechooser.setFileFilter(filter);
            filechooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            int result=filechooser.showOpenDialog(null);
            if(result==JFileChooser.CANCEL_OPTION)
            {
                return null;
            }
            else
            {
                file=filechooser.getSelectedFile();
                writeOnTempPlaylist(file);
                PlayLinkedList.addToEndOfList(file.toString());
                Tracks[index]=retFile().toString();
                //System.out.println(Tracks[PlayList.index]);
                index++;
            }
        return file;
    }
    public static File retFile()
    {
        return file;
    }
}
class MP3FileFilter extends javax.swing.filechooser.FileFilter
{
    
    @Override
    public boolean accept(File f) {
//        throw new UnsupportedOperationException("Not supported yet.");

        String fileName=f.getName();
        return fileName.endsWith(".mp3");
       }

    @Override
    public String getDescription() {
//        throw new UnsupportedOperationException("Not supported yet.");
        return("*.mp3");
    }

    class AVIFileFilter extends javax.swing.filechooser.FileFilter
    {

        @Override
        public boolean accept(File f) {
            //throw new UnsupportedOperationException("Not supported yet.");
            String fileName = f.getName();
            return fileName.endsWith(fileName);
        }

        @Override
        public String getDescription() {
            //throw new UnsupportedOperationException("Not supported yet.");
            return(".avi");
        }

    }

}
