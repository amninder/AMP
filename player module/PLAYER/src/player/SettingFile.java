/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package player;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Amninder Singh
 */
public class SettingFile {

   public static void createSettingsFile()
   {
       File settingfile= new File("settings.txt");
        try {
            boolean success = settingfile.createNewFile();
        } catch (IOException ex) {
            Logger.getLogger(SettingFile.class.getName()).log(Level.SEVERE, null, ex);
        }
   }
   public static void writeSettings()
   {
       boolean exists =  (new File("settings.txt")).exists();
       BufferedWriter out;
       if(exists)
       {
            try {
                out = new BufferedWriter(new FileWriter("settings.txt"));
                out.write("amninder singh");
                out.close();
            } catch (IOException ex) {
                Logger.getLogger(SettingFile.class.getName()).log(Level.SEVERE, null, ex);
            }
       }
   }
   public static void readSettings()
   {
       BufferedReader in;
       String get_setting;
       boolean exists =(new File("settings.txt")).exists();
       if(exists)
       {
            try {
                in = new BufferedReader(new FileReader("settings.txt"));
                while ((get_setting = in.readLine()) != null) {
                    JOptionPane.showMessageDialog(null, get_setting);
                }
                in.close();
            } catch (IOException ex) {
                Logger.getLogger(SettingFile.class.getName()).log(Level.SEVERE, null, ex);
            }
       }
   }
}
