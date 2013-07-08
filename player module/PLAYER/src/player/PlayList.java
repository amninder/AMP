/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package player;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.media.CannotRealizeException;
import javax.media.NoPlayerException;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JOptionPane;

/**
 *
 * @author Amninder Singh
 */
public class PlayList {

    public static File temp_file;
    public static JList playlist=null;
    public static DefaultListModel model=null;
    public static JList retList()
    {
        return playlist;
    }
    public static JList list()
    {
        model= new DefaultListModel();

        playlist = new JList(model)
        {
            @Override
            public String getToolTipText(MouseEvent me)
            {
                int index=locationToIndex(me.getPoint());
                Object item = getModel().getElementAt(index);
                return (String) item;
            }
        };
        playlist.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent me)
            {
                JList list =(JList) me.getSource();
                if(me.getClickCount()==2)
                {
                    play_progress_bar.mIncrementor=0;
                    play_progress_bar.nIncrementor=0;
                    int index = list.locationToIndex(me.getPoint());
                    try {
                        AudioProperty.readTag(new File(PlayLinkedList.getSelectedElement(index)));
                        CreatePlayer.startPlayer(new File(PlayLinkedList.getSelectedElement(index)),0);
                    } catch (IOException ex) {
                        Logger.getLogger(PlayList.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (NoPlayerException ex) {
                        Logger.getLogger(PlayList.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (CannotRealizeException ex) {
                        Logger.getLogger(PlayList.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                if(me.getButton()==MouseEvent.BUTTON3)
                {
                    PlayLinkedList.removeElement(PlayList.playlist.getSelectedIndex());
                }
            }
        });
        playlist.addFocusListener(new FocusAdapter(){
            @Override
            public void focusLost(FocusEvent fe)
            {
                playlist.addKeyListener(new KeyAdapter(){
                    @Override
                    public void keyPressed(KeyEvent ke)
                    {
                        if(ke.getKeyChar()==KeyEvent.VK_N)
                        {
                            System.out.println("down pressed");
                        }
                    }
                });
            }
        });
        
        return playlist;
    }
    public static void tempPlaylist()
    {
        temp_file= new File(FileManager.tempPlaylistName);

    }
    public static void getTempPlaylist()
    {
        boolean exists = ((new File(FileManager.tempPlaylistName))).exists();
        if(exists==true)
        {
            try {
                BufferedReader in = new BufferedReader(new FileReader(FileManager.tempPlaylistName));
                String str;
                while ((str = in.readLine()) != null) {
                    JOptionPane.showMessageDialog(null, str);

                }
                in.close();
            } catch (IOException ex) {
                Logger.getLogger(PlayList.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
     public static void playNext()
    {
                    String str = CreatePlayer.playing_file.toString();
                    int index=0;
                    index=PlayLinkedList.findTrack(str)+1;
                    if(index==PlayLinkedList.list.size())
                    {
                              index=0;
                    }
                    try {
                      PlayList.playlist.setSelectedIndex(index);
                      CreatePlayer.startPlayer(new File(String.valueOf(PlayLinkedList.list.get(index))),0);
                    } catch (IOException ex) {
                        Logger.getLogger(PLAYERView.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (NoPlayerException ex) {
                        Logger.getLogger(PLAYERView.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (CannotRealizeException ex) {
                    Logger.getLogger(PLAYERView.class.getName()).log(Level.SEVERE, null, ex);
                    }
    }
     public static void playPrevious()
     {
         String str = CreatePlayer.playing_file.toString();
         int index = 0;
         index= PlayLinkedList.findTrack(str);
         if(index==0)
         {
             index=PlayLinkedList.list.size()-1;
         }
         else
         {
             index-=1;
         }
         PlayList.playlist.setSelectedIndex(index);
        try {
            CreatePlayer.startPlayer(new File(String.valueOf(PlayLinkedList.list.get(index))),0);
        } catch (IOException ex) {
            Logger.getLogger(PlayList.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoPlayerException ex) {
            Logger.getLogger(PlayList.class.getName()).log(Level.SEVERE, null, ex);
        } catch (CannotRealizeException ex) {
            Logger.getLogger(PlayList.class.getName()).log(Level.SEVERE, null, ex);
        }
     }
    public static void readFromM3UFile(File m3uFile) throws FileNotFoundException, IOException
    {
        FileReader in =new FileReader(m3uFile);
        BufferedReader reader = new BufferedReader(in);
        String str = reader.readLine();
        if( !str.equals( "#EXTM3U\r\n" ) ) {
           while( (str = reader.readLine()) != null ) {
               if( str.indexOf( "EXTINF:" ) != -1 ) {
                   str = reader.readLine();
                   PlayLinkedList.addToEndOfList(str);
               }
           }   
        }       
    }
    public static void writeToM3UFile(String name) throws FileNotFoundException, IOException
    {
        
        String str = "" ;
        for(int i=0;i<PlayLinkedList.list.size();i++)
        {
            str+="#EXTM3U\r\n#EXTINF:"+i+","+PlayLinkedList.list.get(i)+"\r\n"+PlayLinkedList.list.get(i)+"\r\n";
        }
        File playList = new File(name+".m3u");
        FileOutputStream out = new FileOutputStream(playList);
        PrintWriter printer = new PrintWriter(out);
        printer.print(str);
        printer.close();
        out.close();
    }
}
