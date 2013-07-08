/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package player;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.media.Time;

/**
 *
 * @author Amninder Singh
 */
public class ProgressBarPlay extends Thread implements Runnable {
    
     public static Thread thread=null;
     public String hour;
     public String minute;
     public String seconds;
     
    public void killThread()
    {
        thread=null;
    }
    public  void runThread()
    {
        thread = new Thread(this);
        thread.start();
    }
    public String intToTime(int i)
    {
        int h=0;
        int m=0;
        int s=i;
        while(s>59)
        {
            if(s>59)
            {
                s-=60;
                m++;
                if(m>59)
                {
                    m-=60;
                    h++;
                }
            }
        }
        String time=String.valueOf(h)+":"+String.valueOf(m)+":"+String.valueOf(s);
        return time;
    }
    @Override
    @SuppressWarnings("static-access")
    public void run() {
        //throw new UnsupportedOperationException("Not supported yet.");
        while(CreatePlayer.player!=null)
        {
            if(CreatePlayer.player!=null)
            {
                Time time = CreatePlayer.player.getMediaTime();
                PLAYERView.play_progress_bar.setValue((int)time.getSeconds()+1);
                PLAYERView.play_slider.setValue((int)time.getSeconds()+1);
                PLAYERView.play_progress_bar.setString(intToTime((int)time.getSeconds()));
                PLAYERView.time_panel_textfield.setText(intToTime((int)time.getSeconds()));
                try {
                    this.thread.sleep(1,1);
                } catch (InterruptedException ex) {
                    Logger.getLogger(ProgressBarPlay.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        CreatePlayer.stopPlayer();
    }
    

}