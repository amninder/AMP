/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package player;

import java.awt.Toolkit;
import javax.swing.SwingWorker;

/**
 *
 * @author Amninder Singh
 */
public class task extends SwingWorker {

    @Override
    protected Object doInBackground() throws Exception {
        //throw new UnsupportedOperationException("Not supported yet.");

        int progress=0;
        setProgress(0);
        while(progress<110)
        {
            progress++;
            try
            {
                Thread.sleep(10);
            }
            catch(InterruptedException ie)
            {

            }
            setProgress(progress);
        }


        return null;
    }
    public void done()
    {
        Toolkit.getDefaultToolkit().beep();
    }

}
