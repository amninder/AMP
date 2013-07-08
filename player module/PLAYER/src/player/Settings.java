/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package player;

import java.awt.Color;

/**
 *
 * @author Amninder Singh
 */
public class Settings {

    public static int red=240;
    public static int blue=240;
    public static int green=240;

    public static int redColor()
    {
        return red;
    }
    public static int greenColor()
    {
        return green;
    }
    public static int blueColor()
    {
        return blue;
    }
    public static void volumePanelColor()
    {
        PLAYERView.volume_panel.setBackground(new Color(red,green,blue));
        PLAYERView.volume_control_slider.setBackground(new Color(red,green,blue));
        PLAYERView.speed_checkbox.setBackground(new Color(red,green,blue));
        PLAYERView.loop_checkbox.setBackground(new Color(red,green,blue));
    }
    public static void volume_control_slider_color()
    {
        PLAYERView.volume_control_slider.setBackground(new Color(red,green,blue));
    }
    public static void property_panel_color()
    {
        PLAYERView.properties_panel.setBackground(new Color(red,green,blue));
        PLAYERView.propertyPannel_middle.setBackground(new Color(red,green,blue));
        PLAYERView.property_panel_left.setBackground(new Color(red,green,blue));
        PLAYERView.property_panel_right.setBackground(new Color(red,green,blue));
        PLAYERView.artist_textfield.setBackground(new Color(red,green,blue));
        PLAYERView.album_textfield.setBackground(new Color(red,green,blue));
        PLAYERView.track_textfield.setBackground(new Color(red,green,blue));
        PLAYERView.genre_textfield.setBackground(new Color(red,green,blue));
        PLAYERView.year_textfield.setBackground(new Color(red,green,blue));
        PLAYERView.version_textfield.setBackground(new Color(red,green,blue));
        PLAYERView.layer_textfield.setBackground(new Color(red,green,blue));
        PLAYERView.layer_textfield.setBackground(new Color(red,green,blue));
        PLAYERView.channel_textfield.setBackground(new Color(red,green,blue));
        PLAYERView.copyrighted_textfield.setBackground(new Color(red,green,blue));
        PLAYERView.crced_textfield.setBackground(new Color(red,green,blue));
        PLAYERView.emphasis_textfield.setBackground(new Color(red,green,blue));
        PLAYERView.timePanel.setBackground(new Color(red,green,blue));
        PLAYERView.time_panel_textfield.setBackground(new Color(red,green,blue));
        PLAYERView.AUDIO_textfield.setBackground(new Color(red,green,blue));
        PLAYERView.ALBUM_textfield.setBackground(new Color(red,green,blue));
        PLAYERView.playing_file_heading_textfield.setBackground(new Color(red,green,blue));
    }
    public static void propertytab_mainpanel()
    {
        PLAYERView.property_tab_mainpanel.setBackground(new Color(red,green,blue));
    }
    public static void mainpanel_color()
    {
        PLAYERView.mainPanel.setBackground(new Color(red,green,blue));
    }
    public static void menuBar()
    {
        PLAYERView.menuBar.setBackground(new Color(red,green,blue));
    }
    public static void rightLeftVolumePanel()
    {
        PLAYERView.play_right_panel.setBackground(new Color(red,green,blue));
        PLAYERView.play_left_panel.setBackground(new Color(red,green,blue));
    }
    public static void allCheckBoxes()
    {
        PLAYERView.loop_checkbox.setBackground(new Color(red,green,blue));
        PLAYERView.speed_checkbox.setBackground(new Color(red,green,blue));
    }
    public static void allSlideBars()
    {
        PLAYERView.play_slider.setBackground(new Color(red,green,blue));
    }
    public static void propertyTab()
    {
        PLAYERView.property_tab.setBackground(new Color(red,green,blue));
    }
}

