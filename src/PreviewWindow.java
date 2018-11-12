/* Authors: Caitlin Jones, Chelina Ortiz M
 * Date: 11/16/18
 * Project: CS 335 Program 3 - Morph
 * References: Caitlin's Bombs Project Settings Page
 * Notes:
 *
 */

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class PreviewWindow extends JFrame{

    public PreviewWindow(PopupSettings settings){
        super("Preview");

        addMenus(settings);

        setSize(300,300);
        setVisible(true);
    }// End Constructor

    private void addMenus(PopupSettings settings){
        //Export will produce a window
        JMenuItem FileExport = new JMenuItem("Export");
        FileExport.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                System.out.println("Export to video file");
            }
        });

        JMenuItem FileSettings = new JMenuItem("Settings");
        FileSettings.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                settings.setVisible(true);
                settings.addWindowListener(new WindowAdapter() {
                    public void windowClosing(WindowEvent windowEvent) {
                        System.out.println("Settings Closed");
                        //call member functions of settings page here
                    }
                });
            }
        });

        JMenuItem FileExit = new JMenuItem("Exit");
        FileExit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                PreviewWindow.this.dispatchEvent(new WindowEvent(
                        PreviewWindow.this, WindowEvent.WINDOW_CLOSING
                ));
            }
        });

        //Initialize the File menu and add it's child controls
        JMenu File = new JMenu("File");
        File.add(FileExport);
        File.add(FileSettings);
        File.add(FileExit);

        //Initialize the menubar and add it to the JFrame
        JMenuBar menuBar = new JMenuBar();
        this.setJMenuBar(menuBar);
        menuBar.add(File);
    }//end addMenus()

}//End class
