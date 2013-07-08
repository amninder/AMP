/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package player;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingWorker;

/**
 *
 * @author Amninder Singh
 */
class play_progress_bar implements PropertyChangeListener {
    public static long initial_value;
    public static long final_value;
    public static long curent_value;
    public static Task task;
    public static long mIncrementor;
    public static int nIncrementor;



    play_progress_bar()
    {
        //PLAYERView.play_progress_bar.setIndeterminate(true);
        final_value=CreatePlayer.finalTime();
        task = new Task();
        task.addPropertyChangeListener(this);
        //task.execute();
    }
    public static void playProgressBar()
    {
        task.execute();
    }
    public static void killProgressBar()
    {
        try {
            task.wait();
        } catch (InterruptedException ex) {
            Logger.getLogger(play_progress_bar.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public static void killTask()
    {
        mIncrementor=(long) 0;
        nIncrementor=1;
    }
    public static void getTime(long initial_time, long final_time)
    {
        play_progress_bar.initial_value=initial_time;
        play_progress_bar.final_value=final_time;
    }
    public static void getCurrentTime(long current_time)
    {
        play_progress_bar.curent_value=current_time;
    }

    public void propertyChange(PropertyChangeEvent evt) {
        //throw new UnsupportedOperationException("Not supported yet.");
        if("progress"==evt.getNewValue())
        {
            int progress=(Integer)evt.getNewValue();
            
        }
    }

    class Task extends SwingWorker
    {

        @Override
        protected Object doInBackground() throws Exception {
            //throw new UnsupportedOperationException("Not supported yet.");
            long m=0;
            long n=0;
            mIncrementor=CreatePlayer.timeInMilisecond()/100;
            nIncrementor=(CreatePlayer.remainingTimeInNanoSeconds() / 100);
            int progress=0;
            setProgress(0);
           
                while(CreatePlayer.PLAYER_IS_RUNNING==true)
                {
                try
                {
                      
                    Thread.sleep(mIncrementor, nIncrementor);
                    System.out.println(m +" : "+n);
                    m+=mIncrementor;
                    n+=nIncrementor;
                    PLAYERView.play_progress_bar.setValue(progress);
                    PLAYERView.play_progress_bar.setString(String.valueOf(progress));
                    PLAYERView.play_slider.setValue(progress);
                    
                    progress++;
                    
                }
                catch(InterruptedException e)
                {
                }
                //progress++;
                setProgress(progress);

            }
            PLAYERView.play_progress_bar.setValue(0);

            return null;
        }

    }

}
