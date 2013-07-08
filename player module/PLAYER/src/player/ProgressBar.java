/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package player;

import java.awt.Color;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;

/**
 *
 * @author Amninder Singh
 */
public class ProgressBar extends JProgressBar implements PropertyChangeListener {
    static ProgressBar pb=null;
    public static void createProgressBar()
    {
        pb = new ProgressBar();
    }
    public static ProgressBar removeProgress()
    {
        return pb;
    }
    ProgressBar()
    {
        System.out.println("inside progress bar");
        this.setSize(PLAYERView.panel_video.getWidth(), 20);
        this.setStringPainted(true);
        this.setIndeterminate(true);
        runProgress();
        this.setBackground(Color.ORANGE);
        this.show(true);
        PLAYERView.panel_video.add(this);
    }
    public void runProgress()
    {
        Task task = new Task();
        task.addPropertyChangeListener(this);
        task.execute();
    }

    public void propertyChange(PropertyChangeEvent evt) {
        //throw new UnsupportedOperationException("Not supported yet.");
        if("progress"==evt.getNewValue())
        {
            int progress =(Integer)evt.getNewValue();
            this.setValue(progress);
        }
    }


    class Task extends SwingWorker
    {

        @Override
        protected Object doInBackground() throws Exception {
            //throw new UnsupportedOperationException("Not supported yet.");
             int progress=0;
            setProgress(0);
            while(progress<10)
            {
                try
                {
                    Thread.sleep(1000);
                }
                catch(InterruptedException e)
                {

                }
                progress++;
                setProgress(progress);

            }

            return null;
        }

    }

}
