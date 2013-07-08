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
import java.io.File;
import javax.media.Player;
import javax.swing.JPanel;

/**
 *
 * @author Amninder Singh
 */
public class VideoPanel extends JPanel {
    public static File Video_file=null;
    public static Player video_player;
    public static Component visual_component=null;
    public static Dimension videoDimension=null;
    public static int videoWidth;
    public static int videoHeight;
    VideoPanel()
    {
        this.setLayout(null);
        this.setBackground(Color.black);
        this.addHierarchyBoundsListener(new HierarchyBoundsListener(){

            public void ancestorMoved(HierarchyEvent e) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public void ancestorResized(HierarchyEvent e) {
//                throw new UnsupportedOperationException("Not supported yet.");
                setPanelSize();

            }
        });

        createVideo();

        this.show(true);
        PLAYERView.panel_video.setLayout(null);
        PLAYERView.panel_video.add(this);
        PLAYERView.video_panel.repaint();
    }
    VideoPanel(int i)
    {
        PLAYERView.video_panel.remove(visual_component);
    }
    VideoPanel(Dimension screenSize)
    {
        createVideo();
        this.setSize(screenSize);
        visual_component.setSize(screenSize);
//        createVideo();
        this.show(true);

    }
    public void setPanelSize()
    {
        this.setSize(PLAYERView.panel_video.getWidth(), PLAYERView.panel_video.getHeight());
        visual_component.setSize(this.getSize());
    }
    public void createVideo()
    {
        if(CreatePlayer.player!=null)
        {
            if((VideoPanel.visual_component=CreatePlayer.player.getVisualComponent())==null)
            {
                   System.out.println("no visual component found");
            }
            else if((visual_component=CreatePlayer.player.getVisualComponent())!=null)
            {
                videoDimension=PLAYERView.panel_video.getSize();
                this.setSize(videoDimension.width,videoDimension.height);
                visual_component.setSize(videoDimension);
                this.add(visual_component);
            }
            else
            {
                System.out.println("unsupported video format");
            }
       }
        else
        {
            System.out.println("player not created");
        }

    }
    public static void createVideoObject()
    {
        VideoPanel vp = new VideoPanel();
    }
    public  JPanel retPanel()
    {
        return this;
    }
}
