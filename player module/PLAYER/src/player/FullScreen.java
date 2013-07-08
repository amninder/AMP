/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package player;

import java.awt.Component;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.media.CannotRealizeException;
import javax.media.ControllerAdapter;
import javax.media.EndOfMediaEvent;
import javax.media.Manager;
import javax.media.NoPlayerException;
import javax.media.Player;
import javax.media.Time;
import javax.swing.JPanel;
import javax.swing.JWindow;

/**
 *
 * @author Amninder Singh
 */
public class FullScreen {

    public static JWindow window;
    public static JPanel video_panel;
    public static Component visual_component;
    FullScreen()
    {

    }
    public static void setFullScreenVideo()
    {
        window = new JWindow();
        window.setLayout(null);
        window.setSize(Toolkit.getDefaultToolkit().getScreenSize());
        window.add(SetVideo.setVideo(window.getSize()));
        window.show(true);

        window.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent me)
            {
                if(me.getClickCount()==2)
                {
                    window.show(false);
                }
            }
        });

    }
    public static void createVideoPlayer(File file, Long startTime) throws IOException, NoPlayerException, CannotRealizeException
    {
        play_progress_bar.mIncrementor=1;
        play_progress_bar.nIncrementor=1;
        CreatePlayer.stopPlayer();
        window = new JWindow();
        Component visualComponent = null;
        window.setLayout(null);
        final Player player = Manager.createRealizedPlayer(file.toURL());
        player.addControllerListener(new ControllerAdapter(){
            @Override
            public void endOfMedia(EndOfMediaEvent eome)
            {
                if(CreatePlayer.IS_LOOP_ON==true)
                {
                    window.show(false);
                    player.close();
                    PlayList.playNext();
                }
            }
        });
        player.setMediaTime(new Time(startTime));
        player.start();
        if(player.getVisualComponent()!=null)
        {
            window.setSize(Toolkit.getDefaultToolkit().getScreenSize());
            JPanel panel = new JPanel();
            panel.setLayout(null);
            panel.setSize(window.getSize());
            visualComponent = player.getVisualComponent();
            visualComponent.setSize(panel.getSize());
            panel.add(visualComponent);
            window.add(panel);
            window.show(true);
        }
        visualComponent.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent me)
            {
                if(me.getClickCount()==1)
                {
                }
                else if(me.getClickCount()==2)
                {
                    window.show(false);
                    player.stop();
                    player.close();
                    try {
                        CreatePlayer.startPlayer(CreatePlayer.playing_file,player.getMediaNanoseconds());
                    } catch (IOException ex) {
                        Logger.getLogger(FullScreen.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (NoPlayerException ex) {
                        Logger.getLogger(FullScreen.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (CannotRealizeException ex) {
                        Logger.getLogger(FullScreen.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });
    }
}
