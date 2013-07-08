/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package player;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Panel;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.media.CachingControl;
import javax.media.CachingControlEvent;
import javax.media.Controller;
import javax.media.ControllerClosedEvent;
import javax.media.ControllerErrorEvent;
import javax.media.ControllerEvent;
import javax.media.ControllerListener;
import javax.media.EndOfMediaEvent;
import javax.media.Manager;
import javax.media.MediaLocator;
import javax.media.NoPlayerException;
import javax.media.Player;
import javax.media.RealizeCompleteEvent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
/**
 *
 * @author Amninder Singh
 */
public class VideoFrame extends JFrame implements ControllerListener
{
    private File file;
    Player p=null;
    Component visualComponent =null;
    Component ControlComponent=null;
    Component progressBar=null;
    boolean firstTime=true;
    long cachingSize=0L;
    Panel panel=null;
    int controlPanelHeight=0;
    int videoWidth=0;
    int videoHeight=0;
    Toolkit toolkit;


    VideoFrame()
    {
        toolkit= Toolkit.getDefaultToolkit();
        createPlayer();
    }
    public File getFile()
    {
        JFileChooser filechooser=new JFileChooser();
        filechooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        filechooser.setFileFilter(new FileFilter());
        int result= filechooser.showOpenDialog(null);
        if(result==JFileChooser.CANCEL_OPTION)
        {
            System.exit(0);
        }
        else
        {
            file=filechooser.getSelectedFile();
        }
        return file;
    }
    public void createPlayer()
    {
        this.setLayout(null);
        this.setBackground(Color.red);
        panel=new Panel();
        panel.setLayout(null);
        panel.setBounds(0, 0, 320, 240);
        MediaLocator ml=null;
        String mediaFile=null;
        URL url=null;

        try {
            p = Manager.createPlayer(getFile().toURL());
        } catch (IOException ex) {
            System.out.println("inside IOException of createPlayer");
            Logger.getLogger(VideoFrame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoPlayerException ex) {
            System.out.println("inside NoPlayerException of createPlayer");
            Logger.getLogger(VideoFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        p.addControllerListener(this);
        p.start();

                this.addWindowListener(new WindowAdapter(){
            @Override
            public void windowClosing(WindowEvent we)
            {
                if(p==null)
                {
                    System.out.println("player value: null");
                    setShowOFF();
                }
                else if(p!=null)
                {
                    p.close();
                    System.out.println("palyer value:"+String.valueOf(p));
                    setShowOFF();
                }
                else
                {
                    System.out.println("player exit without showing value");
                    setShowOFF();
                }
            }
        });
        this.setResizable(true);
        add(panel);
        //setSize(640,480);
        //show(true);
    }
    public int retVideoHeight()
    {
        return(p.getVisualComponent().getPreferredSize().height);
    }
    public synchronized void controllerUpdate(ControllerEvent evt)
    {
        if(p==null)
            return;
        if(evt instanceof RealizeCompleteEvent)
        {
            if(progressBar!=null)
            {
                panel.remove(progressBar);
                progressBar=null;
            }
//            int width=640;
//            int width = toolkit.getScreenSize().width;
            int width= p.getVisualComponent().getPreferredSize().width;
            int height=0;
            if(visualComponent==null)
            {
                if((visualComponent=p.getVisualComponent())!=null)
                {
                    panel.add(visualComponent);
                    Dimension videoSize=visualComponent.getPreferredSize();
//                    Dimension videoSize=visualComponent.getSize();
                    videoWidth=videoSize.width;
                    videoHeight=videoSize.height;
                    height+=videoHeight;
                    visualComponent.setBounds(0, 0, videoWidth, videoHeight);
                }
            }
            panel.setBounds(0, 0, p.getVisualComponent().getPreferredSize().width, p.getVisualComponent().getPreferredSize().height);
            this.setSize(p.getVisualComponent().getSize().width, p.getVisualComponent().getSize().height);
            this.show(true);
            if(ControlComponent!=null)
            {
                ControlComponent.setBounds(0, videoHeight, width, controlPanelHeight);
                ControlComponent.invalidate();
            }
        }
        else if(evt instanceof CachingControlEvent)
        {
            if(p.getState()>Controller.Realizing)
                return;
            CachingControlEvent e=(CachingControlEvent) evt;
            CachingControl cc= e.getCachingControl();
            if(progressBar==null)
            {
                if((progressBar=cc.getControlComponent())!=null)
                {
                   //panel.add(progressBar);
//                   panel.setSize(progressBar.getPreferredSize());
//                  panel.setSize(progressBar.getSize());
                   validate();
                }
            }
        }
        else if(evt instanceof EndOfMediaEvent)
        {
            p.close();
            //p.setMediaTime(new Time(0));
           // p.start();
        }
        else if(evt instanceof ControllerErrorEvent)
        {
            p=null;
        }
        else if(evt instanceof ControllerClosedEvent)
        {
            panel.removeAll();
        }
    }
    public void setShowOFF()
    {
        this.show(false);
    }
    public void setShowON()
    {
        this.show(true);
    }
}
class FileFilter extends javax.swing.filechooser.FileFilter
    {

        @Override
        public boolean accept(File f) {
            //throw new UnsupportedOperationException("Not supported yet.");
            String fileName = f.getName();
            return fileName.endsWith(".avi");
        }

        @Override
        public String getDescription() {
            //throw new UnsupportedOperationException("Not supported yet.");
            return (".avi");
        }

  }
