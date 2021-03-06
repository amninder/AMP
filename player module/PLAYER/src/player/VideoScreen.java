package player;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.HierarchyBoundsListener;
import java.awt.event.HierarchyEvent;
import java.awt.event.WindowAdapter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.media.CannotRealizeException;
import javax.media.ControllerEvent;
import javax.media.ControllerListener;
import javax.media.Manager;
import javax.media.NoPlayerException;
import javax.media.Player;
import javax.media.RealizeCompleteEvent;
import javax.swing.JFrame;
import javax.swing.JPanel;

class VideoScreen extends JFrame implements ControllerListener
{

    public static JPanel panel= new JPanel();
    public static Component visual_component=null;
    public static int video_height;
    public static int video_width;
    public static Player player=null;
    VideoScreen()
    {
        this.setLayout(null);
        VideoScreen.setDefaultLookAndFeelDecorated(true);
        createVideo();
        //this.show(true);
    }

    public void createVideo()
    {
        panel = new JPanel();
        panel.setBackground(Color.red);
        System.out.println("after red panel");
            try {
                player = Manager.createPlayer(FileManager.getFile().toURL());
            } catch (IOException ex) {
                Logger.getLogger(VideoScreen.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NoPlayerException ex) {
                Logger.getLogger(VideoScreen.class.getName()).log(Level.SEVERE, null, ex);
            } catch (CannotRealizeException ex) {
                Logger.getLogger(VideoScreen.class.getName()).log(Level.SEVERE, null, ex);
            }
        this.addWindowListener(new WindowAdapter(){
            public void windowClosing()
            {
                player.close();
                player.stop();
                System.exit(0);
            }
        });
        this.getContentPane().addHierarchyBoundsListener(new HierarchyBoundsListener (){

            public void ancestorMoved(HierarchyEvent e) {
                //throw new UnsupportedOperationException("Not supported yet.");
            }

            public void ancestorResized(HierarchyEvent e) {
               // throw new UnsupportedOperationException("Not supported yet.");
                panel.setSize(retVideoScreenWidth(), retVideoScreenHeight());
                visual_component.setSize(retVideoScreenWidth(), retVideoScreenHeight());

            }
        });
            player.addControllerListener(this);
            player.start();
            //this.add(panel);
            //this.show(true);

    }
    public synchronized void controllerUpdate(ControllerEvent ce)
    {
        if(player==null)
            return;
        if(ce instanceof RealizeCompleteEvent)
        {
            System.out.println();
            int width=player.getVisualComponent().getPreferredSize().width;
            int height=player.getVisualComponent().getPreferredSize().height;

            if((visual_component)==null)
            {
                if((visual_component=player.getVisualComponent())!=null)
                {
                   panel.add(visual_component);
                   Dimension videoSize=visual_component.getPreferredSize();
                   video_width=videoSize.width;
                   video_height=videoSize.height;
                   visual_component.setBounds(0, 0, video_width, video_height);
                }
            }
            visual_component.setSize(video_width, video_height);
            panel.setLayout(null);
            panel.setBounds(0, 0, video_width, video_height);
            panel.add(visual_component);
            videoScreenSize(video_width,video_height);
        }
    }
    public void videoScreenSize(int width, int height)
    {
        this.setSize(width, height);
        this.add(panel);
        this.show(true);
    }
    public int retVideoScreenHeight()
    {
        return this.getHeight();
    }
    public int retVideoScreenWidth()
    {
        return this.getWidth();
    }
    public static Player retPlayer()
    {
        return player;
    }
    public void closeThis()
    {
        this.show(false);
    }
    public static Component retVideoComponent()
    {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        visual_component.setSize(toolkit.getScreenSize().width, toolkit.getScreenSize().height);
        return visual_component;
    }
}