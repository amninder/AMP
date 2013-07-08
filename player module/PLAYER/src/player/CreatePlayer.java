/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package player;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.processing.Processor;
import javax.media.CannotRealizeException;
import javax.media.CaptureDeviceInfo;
import javax.media.CaptureDeviceManager;
import javax.media.ControllerAdapter;
import javax.media.EndOfMediaEvent;
import javax.media.Manager;
import javax.media.MediaLocator;
import javax.media.NoPlayerException;
import javax.media.Player;
import javax.media.PrefetchCompleteEvent;
import javax.media.RealizeCompleteEvent;
import javax.media.StartEvent;
import javax.media.Time;
import javax.media.format.RGBFormat;
import javax.swing.SwingWorker;

/**
 *
 * @author Amninder Singh
 */
public class CreatePlayer implements Runnable {

    public static Player player;
    public static Processor processor;
    public static File playing_file;
    public static URL currentTrack;
    public static boolean IS_PLAYER_PAUSED;
    public static boolean IS_PLAYER_RUNNING=false;
    public static boolean PLAYER_IS_RUNNING=true;
    public static boolean PLAYER_IS_NOT_RUNNING=false;
    public static boolean IS_LOOP_ON=false;
    public static boolean progress_bar_thread=false;
    public static long pause_time;
    public static long initial_time;
    public static long final_time;
    public static long current_time;
    public static long startTime;
    public static double speed=1.0;
    public static Thread thread=null;
    public static MediaLocator medialocator;

    public static void createPlayer() throws IOException, NoPlayerException, CannotRealizeException
    {
        IS_PLAYER_PAUSED=false;
        setVolumeLabel();
            FileManager.getFile();
            startPlayer(FileManager.retFile(),0);
             
    }
    public static void startPlayer(final File file, long startTime) throws IOException, NoPlayerException, CannotRealizeException
    {
        IS_PLAYER_RUNNING=true;
        playing_file=file;
        if(player!=null)
        {
            stopPlayer();
        }
        
        
            medialocator= new MediaLocator(file.toURL());
            player = Manager.createRealizedPlayer(medialocator);
            
            
            player.addControllerListener(new ControllerAdapter(){
            @Override
                public void prefetchComplete(PrefetchCompleteEvent pce)
                {
                    PLAYERView.playing_file_heading_textfield.setText(PlayLinkedList.setPlayingFileName(playing_file));
                }
                @Override
                public void endOfMedia(EndOfMediaEvent eome)
                {
                    progress_bar_thread=false;
                    IS_PLAYER_RUNNING=false;
                    new ProgressBarPlay().killThread();
                    if(VideoPanel.visual_component!=null)
                    {
                        VideoPanel.visual_component=null;
                    }
                    if(CreatePlayer.IS_LOOP_ON==true)
                    {
                        System.out.println("inside the IS LOOP ON");
                        PlayList.playNext();
                    }
                    else
                    {
                        //player.close();
                        //player.deallocate();
                    }
                    if(CreatePlayer.player.getVisualComponent()!=null)
                    {
                        SetVideo.removeVideo();
                    }
                }
            @Override
                public void realizeComplete(RealizeCompleteEvent e)
                {
                    initial_time=player.getMediaTime().getNanoseconds();
                    System.out.println(initial_time);
                    
                    
                     
                }

            @Override
            public void start(StartEvent s)
            {                
                try {
                    AudioProperty.readTag(file);
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(CreatePlayer.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(CreatePlayer.class.getName()).log(Level.SEVERE, null, ex);
                }
                try {
                    MPEGAudioFrameHeader.audioFrameHeader(file, 0);
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(CreatePlayer.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(CreatePlayer.class.getName()).log(Level.SEVERE, null, ex);
                } catch (Exception ex) {
                    Logger.getLogger(CreatePlayer.class.getName()).log(Level.SEVERE, null, ex);
                }
                //new VideoPanel();
                if(player.getVisualComponent()!=null)
                {
                    PLAYERView.panel_video.add(SetVideo.setVideo(PLAYERView.panel_video.getSize()));
                }
                //new play_progress_bar();
                //play_progress_bar.playProgressBar();
                PLAYERView.panel_video.repaint();
                    
                    System.out.println(player.getDuration().getSeconds());
                    PLAYERView.play_progress_bar.setMaximum((int) player.getDuration().getSeconds());
                    PLAYERView.play_slider.setMaximum((int)player.getDuration().getSeconds());
                    System.out.println(PLAYERView.play_progress_bar.getMaximum());
                    new ProgressBarPlay().runThread();
            }
            });
            
            
            player.setRate((float) speed);
            initial_time=0;
           final_time=player.getDuration().getNanoseconds();
           System.out.println(final_time);
           
           
           player.getGainControl().setLevel(PLAYERView.volume_control_slider.getValue()/100);
           player.setMediaTime(new Time(startTime));
           player.start();
    }
    public static void stopPlayer()
    {
        if(player!=null)
        {
            if(player.getVisualComponent()!=null)
            {
                SetVideo.removeVideo();
            }
            IS_PLAYER_RUNNING=false;
            player.close();
            player.deallocate();
            player.stop();
            player=null;
        }
   }
    public static void destroyPlayer()
    {
       // IS_PLAYER_RUNNING=false;
        player.close();
        player.deallocate();
    }
    public static void pausePlayer()
    {
        //IS_PLAYER_RUNNING=false;
        if(IS_PLAYER_PAUSED==false)
        {
            //player is playing
            IS_PLAYER_PAUSED=true;
            pause_time=player.getMediaNanoseconds();
            player.stop();
        }
        else
        {
            IS_PLAYER_PAUSED=false;
            player.setMediaTime(new Time(pause_time));
            player.start();
        }
    }
    public static void playToPause()
    {
        IS_PLAYER_RUNNING=false;
        IS_PLAYER_PAUSED=true;
        pause_time=player.getMediaNanoseconds();
        player.stop();
    }
    public static void pauseToPlay()
    {
        IS_PLAYER_RUNNING=true;
        IS_PLAYER_PAUSED=false;

    }
    public static long currentTime()
    {
        return (player.getMediaNanoseconds());
    }
    public static long totalTime()
    {
        return(player.getDuration().getNanoseconds());
    }
    public static long timeInMilisecond()
    {
        long m = player.getDuration().getNanoseconds();
        m/=1000000;
        return(m);
    }
    public static int remainingTimeInNanoSeconds()
    {
        long n=player.getDuration().getNanoseconds();
        int nn=(int) (n % 1000000);
        return(nn);
    }
    public static long finalTime()
    {
       final_time= player.getDuration().getNanoseconds();
       final_time/=1000000;
        return (final_time);
    }
    public static int remainingNano()
    {
        int remaining=(int) (player.getDuration().getNanoseconds() % 1000000);
        return (remaining);
    }
    public static float retPercentage()
    {
        float per =((player.getMediaNanoseconds())/(player.getDuration().getNanoseconds()))*100;
        System.out.println(per);
        return per;
    }
    public static void changeSpeedWhilePlay()
    {
        if(PLAYERView.speed_checkbox.isSelected()==true)
        {
            speed=2.0;
            PLAYERView.speed_checkbox.setText("2X ON");
        }
        else
        {
            speed=1.0;
            PLAYERView.speed_checkbox.setText("2X OFF");
        }
        pause_time=player.getMediaNanoseconds();
        player.setRate((float) speed);
        player.setMediaTime(new Time(pause_time));
        try {
            CreatePlayer.startPlayer(playing_file, pause_time);
            //player.start();
        } catch (IOException ex) {
            Logger.getLogger(CreatePlayer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoPlayerException ex) {
            Logger.getLogger(CreatePlayer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (CannotRealizeException ex) {
            Logger.getLogger(CreatePlayer.class.getName()).log(Level.SEVERE, null, ex);
        }
        //player.start();
    }
    public static boolean retPlayerStatus()
    {
        if(player!=null)
        {
            return PLAYER_IS_RUNNING;
        }
        else
        {
            return PLAYER_IS_NOT_RUNNING;
        }
    }
    public static void setVolume(float volume)
    {
        player.getGainControl().setLevel(volume);
    }
    public static void setVolumeLabel()
    {
        PLAYERView.volume_label.setText(String.valueOf(PLAYERView.volume_control_slider.getValue()));
    }
    public static void setDB(float decibel)
    {
        player.getGainControl().setDB(decibel);
    }

    public static void backgroundThreadForProgressBar()
    {
         SwingWorker swingworker = new SwingWorker() {

            @Override
            protected Object doInBackground() throws Exception {
                //throw new UnsupportedOperationException("Not supported yet.");
                long progress=0;
                setProgress(0);
                while(progress<final_time)
                {
                    progress=player.getMediaNanoseconds();
                  try
                  {
                      Thread.sleep(1);
                      PLAYERView.play_progress_bar.setValue((int)((progress/final_time)*100));

                  }
                  catch(InterruptedException ie)
                  {

                  }
                  setProgress((int)((progress/final_time)*100));
                }


                return null;
            }
            @Override
            public void done()
            {

            }
        };
        swingworker.addPropertyChangeListener(new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                //throw new UnsupportedOperationException("Not supported yet.");
                if("progress"==evt.getPropertyName())
                {
                    int progress=(Integer)evt.getNewValue();
                   // PLAYERView.play_progress_bar.setValue(progress);
                    //PLAYERView.play_slider.setValue(progress);
                }
            }
        });
        swingworker.execute();
    }
    public static void getAudioFileTimeValues()
    {
        final_time=player.getDuration().getNanoseconds();
        initial_time=0;
    }
    public static void nothing()
    {
        Vector device =CaptureDeviceManager.getDeviceList(new RGBFormat());
        CaptureDeviceInfo[] deviceInfo=new CaptureDeviceInfo[device.size()];
        for(int i=0;i<deviceInfo.length;i++)
        {
            System.out.println(deviceInfo[i]);
        }
    }

    public void run() {
        //throw new UnsupportedOperationException("Not supported yet.");
        System.out.println("inside run");
        while(player!=null)
        {
            PLAYERView.play_progress_bar.setValue((int) player.getMediaTime().getSeconds());
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                Logger.getLogger(CreatePlayer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
