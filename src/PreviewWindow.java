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

import static java.lang.Thread.*;

public class PreviewWindow extends JFrame {

    private int seconds, framesPerSecond, currFrame = 1;
    private Picture start;
    private Node[][] end;
    private Timer anim;
    private Container c = getContentPane();

    public PreviewWindow(PopupSettings settings, Picture Start, Node[][] End){
        super("Preview");
        start = Start;
        end = End;

        addMenus(settings);
        framesPerSecond = settings.getTweenImageValue();
        seconds = settings.getSeconds();
        c.add(Start);

        setSize(700,700);
        setVisible(true);
    }// end Constructor

    public void startAnimation() {
        for(int i = 0; i < framesPerSecond*seconds; i++){
            animation();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void animation() {
        int x, y, x1, x2, y1, y2;
        for (int i = 0; i < end.length; i++) {
            for (int j = 0; j < end[i].length; j++) {
                x1 = start.getPoints()[i][j].getImgX();
                y1 = start.getPoints()[i][j].getImgY();
                x2 = end[i][j].getImgX();
                y2 = end[i][j].getImgY();

                x = (x2-x1) * ((currFrame)/(framesPerSecond*seconds)) + x1;
                y = (y2-y1) * ((currFrame)/(framesPerSecond*seconds)) + y1;
                currFrame++;

                // Change the coordinates of x and y of the start panel and redraw.
                start.getPoints()[i][j].setImgX(x);
                start.getPoints()[i][j].setImgY(y);
                //start.getPoints()[i][j].setImgX(x2);
                //start.getPoints()[i][j].setImgY(y2);

            }
        }
        start.repaint();
    }

    private void addMenus(PopupSettings settings){
        //Export will produce a window
        JMenuItem FileExport = new JMenuItem("Export");
        FileExport.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                System.out.println("Export to video file");
                startAnimation();
            }
        });

        JMenuItem FileSettings = new JMenuItem("Settings");
        FileSettings.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                settings.setVisible(true);
                settings.addWindowListener(new WindowAdapter() {
                    public void windowClosing(WindowEvent windowEvent) {
                        //call member functions of settings page here
                        framesPerSecond = settings.getTweenImageValue();
                        seconds = settings.getSeconds();
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
