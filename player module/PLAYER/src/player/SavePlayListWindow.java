/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package player;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author Amninder Singh
 */
public class SavePlayListWindow{
    
    public static JFrame saveFrame = new JFrame();
    public static JPanel panel = new JPanel();
    public static JTextField textfield = new JTextField();
    public static JButton save= new JButton("Save");
    public static JButton cancel= new JButton("Cancel");
    public static boolean opened=true;

    SavePlayListWindow()
    {
        
    }
    
    public static void showWindow()
    {
        saveFrame.setSize(PLAYERView.play_right_panel.getSize());
        panel.setSize(saveFrame.getSize());
        panel.setLayout(null);
        
        textfield.setBounds(10, 10, (panel.getWidth()-10), 20);
        textfield.addKeyListener(new KeyAdapter(){
            @Override
            public void keyPressed(KeyEvent ke)
            {
                if(ke.getKeyChar()==KeyEvent.VK_ENTER)
                {
                    if(textfield.getText().equals("")||PlayLinkedList.list.size()==0)
                    {
                        
                    }
                    else
                    {
                        //JOptionPane.showMessageDialog(null, textfield.getText());
                        if(new File(textfield.getText()+".m3u").exists())
                        {
                            JOptionPane.showMessageDialog(null, "File Already Exists");
                            textfield.setText("");
                        }
                        else
                        {
                           try {
                                PlayList.writeToM3UFile(textfield.getText());
                                saveFrame.setVisible(false);
                            } catch (FileNotFoundException ex) {
                                Logger.getLogger(SavePlayListWindow.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (IOException ex) {
                                Logger.getLogger(SavePlayListWindow.class.getName()).log(Level.SEVERE, null, ex);
                            } 
                        }
                    }
                }
                if(ke.getKeyChar()==KeyEvent.VK_ESCAPE)
                {
                    saveFrame.setVisible(false);
                    textfield.setText("");
                }
            }
        });
        panel.add(textfield);
        
        save.setBounds(10, 35, (textfield.getWidth()/2)-1, 20);
        save.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent e) {
                //throw new UnsupportedOperationException("Not supported yet.");
                if(e.getActionCommand().equals("Save"))
                {
                    if(saveFrame.isVisible()==false)
                    {
                        saveFrame.show();
                    }
                    
                    if(textfield.getText().equals("")||PlayLinkedList.list.size()==0)
                    {
                        
                    }
                    else
                    {
                        //JOptionPane.showMessageDialog(null, textfield.getText());
                        if(new File(textfield.getText()+".m3u").exists())
                        {
                            JOptionPane.showMessageDialog(null, "File Already Exists");
                            textfield.setText("");
                        }
                        else
                        {
                           try {
                                PlayList.writeToM3UFile(textfield.getText());
                                saveFrame.setVisible(false);
                            } catch (FileNotFoundException ex) {
                                Logger.getLogger(SavePlayListWindow.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (IOException ex) {
                                Logger.getLogger(SavePlayListWindow.class.getName()).log(Level.SEVERE, null, ex);
                            } 
                        }
                    }
                }
            }
        });
        panel.add(save);
        
        cancel.setBounds((textfield.getWidth()/2)+11, 35, (textfield.getWidth()/2)-1, 20);
        cancel.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent e) {
                //throw new UnsupportedOperationException("Not supported yet.");
                if(e.getActionCommand().equals("Cancel"))
                {
                    saveFrame.setVisible(false);
                }
            }
        });
        panel.add(cancel);
        
        saveFrame.add(panel);
        if(opened)
        {
            saveFrame.setLocationRelativeTo(PLAYERView.play_right_panel);
            saveFrame.setUndecorated(true);
            opened=false;
        }

        saveFrame.setVisible(true);
    }
    
    public static void closeWindow()
    {
        saveFrame.setVisible(false);
        textfield.setText("");
    }
}
