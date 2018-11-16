/* Authors: Caitlin Jones, Chelina Ortiz M
 * Date: 11/16/18
 * Project: CS 335 Program 3 - Morph
 * References: Caitlin's Bombs Project Settings Page
 * Notes:
 *
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class PreviewWindow extends JFrame {

    private int seconds, framesPerSecond;
    private JPanel animationPanel = new JPanel();
    private Node[][] start, end;
    private Container c = getContentPane();

    public PreviewWindow(PopupSettings settings, Node[][] Start, Node[][] End){
        super("Preview");
        start = Start;
        end = End;
        // Change these to be dynamic
        seconds = 5;
        framesPerSecond = 20;

        addMenus(settings);
        animation();


        setSize(700,700);
        setVisible(true);
    }// end Constructor

    public void animation() {
        int x, y, x1, x2, y1, y2;
        for (int i = 0; i < start.length; i++) {
            for (int j = 0; j < start[i].length; j++) {
                x1 = start[i][j].getImgX();
                y1 = start[i][j].getImgY();
                x2 = end[i][j].getImgX();
                y2 = end[i][j].getImgY();

                x = x1; // + the percentage to get to x2 -(x2-x1) * ??
                y = y1; // ^^

                // Change the coordinates of x and y of the start panel and redraw.
            }
        }

    }

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

}//end class
