/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package player;

import java.awt.FlowLayout;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JWindow;

/**
 *
 * @author Amninder Singh
 */
public class testThread extends Thread{
    public static boolean runT=true;
    public static Thread thread=new testThread();
    @Override
    public void run()
    {
        int i=0;
        while(runT==true&&i<10)
        {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                
            }
            System.out.println(i+1);
            i++;
        }
    }
    public static void startThread()
    {
        thread.start();
    }
    public static void killThread()
    {
        runT=false;
        thread.destroy();
    }
    public static void testing()
    {
        JWindow window = new JWindow();
        JPanel panel = new JPanel();
        JTextField text = new JTextField(10);
        window.setSize(300, 500);
        panel.setSize(window.getSize());
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));
        panel.add(text);
        window.add(panel);
        window.show(true);
        
    }
}
