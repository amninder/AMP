/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package player;

import java.awt.Component;

/**
 *
 * @author Amninder Singh
 */
public class VisualComponent {

    public static Component visual_component=null;

    public static void getVisualComponent()
    {
        if(CreatePlayer.player!=null)
        {
            visual_component=CreatePlayer.player.getVisualComponent();
        }
    }
     public static void setVideoSize(int width, int height)
     {
         visual_component.setSize(width, height);
     }
     public static Component retVisualComponent()
     {
         return(visual_component);
     }

}