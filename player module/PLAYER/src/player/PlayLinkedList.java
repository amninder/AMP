/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package player;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.media.CannotRealizeException;
import javax.media.NoPlayerException;
import javax.swing.JOptionPane;

/**
 *
 * @author Amninder Singh
 */
public class PlayLinkedList {
    public static List list = new LinkedList();
    
    public static void addToHead(String str)
    {
        list.add(0, str);
    }

    public static boolean mergeList(String str)
    {
        boolean t=false;
        for(int i=0;i<list.size();i++)
        {
            if(str.equals(list.get(i)))
            {
                JOptionPane.showMessageDialog(null, new File(str).getName()+"\t is already in list");
                t=true;
            }
        }
        System.out.println(String.valueOf(t));
        return t;
    }
    public static void addToEndOfList(String fileName)
    {
        if(mergeList(fileName)!=true)
        {
            list.add(PlayLinkedList.list.size(), fileName);
            PlayList.model.add(PlayList.model.getSize(), new File(fileName).getName().substring(0, new File(fileName).getName().length()-4));
            if(CreatePlayer.playing_file==null)
            {
                try {
                    CreatePlayer.startPlayer(new File(fileName), 0);
                } catch (IOException ex) {
                    Logger.getLogger(PlayLinkedList.class.getName()).log(Level.SEVERE, null, ex);
                } catch (NoPlayerException ex) {
                    Logger.getLogger(PlayLinkedList.class.getName()).log(Level.SEVERE, null, ex);
                } catch (CannotRealizeException ex) {
                    Logger.getLogger(PlayLinkedList.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        else
        {
            
        }
    }
    public static void getFromLinkedList()
    {
        String elements="";
        for(int i=0;i<list.size();i++)
        {
            elements=String.valueOf(list.get(i));
            System.out.println(elements);
        }        
    }
    public static String getSelectedElement(int index)
    {
        String path =String.valueOf(list.get(index));
        return path;
    }
    public static int findTrack(String str)
    { 
        int trackNo = 0;
        for(int i=0;i<list.size();i++)
        {
            if(str.equals(list.get(i)))
            {
                trackNo = i;
            }
        }
        return trackNo;
    }
        public static void removeElement(int index)
        {
            PlayList.model.remove(index);
            PlayLinkedList.list.remove(index);
        }
        public static String setPlayingFileName(File file)
        {
            String str = file.getName().substring(0, file.getName().length()-4);
            return str;
            
        }

}
