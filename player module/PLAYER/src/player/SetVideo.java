/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package player;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.HierarchyBoundsListener;
import java.awt.event.HierarchyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.media.CannotRealizeException;
import javax.media.NoPlayerException;
import javax.swing.JPanel;

/**
 *
 * @author Amninder Singh
 */
public class SetVideo {

    public static Component visual_component=null;
    public static JPanel video_panel=null;
    public static JPanel setVideo(Dimension d)
    {
        PLAYERView.panel_video.setLayout(null);
        video_panel=new JPanel();
        video_panel.setLayout(null);
        video_panel.setBackground(Color.black);
        if(CreatePlayer.player!=null)
        {
                visual_component=CreatePlayer.player.getVisualComponent();
                visual_component.setSize(d);
                video_panel.add(visual_component);
        }
        video_panel.setSize(d);
        video_panel.addHierarchyBoundsListener(new HierarchyBoundsListener(){

            public void ancestorMoved(HierarchyEvent e) {
                //throw new UnsupportedOperationException("Not supported yet.");
            }

            public void ancestorResized(HierarchyEvent e) {
                //throw new UnsupportedOperationException("Not supported yet.");
                video_panel.setSize(PLAYERView.panel_video.getSize());
                visual_component.setSize(PLAYERView.panel_video.getSize());
            }
        });
        visual_component.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent me)
            {
                if(me.getClickCount()==2)
                {
                    try {
                        FullScreen.createVideoPlayer(CreatePlayer.playing_file, CreatePlayer.currentTime());
                    } catch (IOException ex) {
                        Logger.getLogger(SetVideo.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (NoPlayerException ex) {
                        Logger.getLogger(SetVideo.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (CannotRealizeException ex) {
                        Logger.getLogger(SetVideo.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });
        return video_panel;
    }
    public static void removeVideo()
    {
        PLAYERView.panel_video.removeAll();
        SetVideo.video_panel.removeAll();
        SetVideo.visual_component=null;
    }
}
