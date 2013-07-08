/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package player;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JPanel;
import javax.swing.JWindow;

/**
 *
 * @author Amninder Singh
 */
public class VideoJWindow extends JWindow{
    public static Dimension screen_dimension=null;
    public static Component visual_component=null;
    public static JPanel panel=null;

    VideoJWindow()
    {
        screen_dimension=new Dimension(Toolkit.getDefaultToolkit().getScreenSize());
        panel = new JPanel();

        panel.setLayout(null);
        panel.setBackground(Color.red);


        fullScreen();
        this.add(panel);
        this.setLayout(null);
        this.setSize(screen_dimension);
       
        this.show(true);
    }

    public static void fullScreen()
    {
        if(CreatePlayer.player!=null)
        {
            if((visual_component=CreatePlayer.player.getVisualComponent())!=null)
            {
                panel.setSize(screen_dimension);
                visual_component.setSize(panel.getSize());
                panel.add(visual_component);
            }
        }
    }
}
