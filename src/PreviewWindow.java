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
import java.awt.image.BufferedImage;

public class PreviewWindow extends JFrame {

    private int seconds, framesPerSecond, currFrame = 1;
    private Picture start;
    private Node[][] startP, originalP, end;

    private Timer anim;
    private Container c = getContentPane();

    public PreviewWindow(PopupSettings settings, BufferedImage bim, Node[][] Start, Node[][] End){
        super("Preview");
        originalP = Start;
        startP = new Node[Start.length][Start.length];
        for(int i = 0; i < Start.length; i++){
            for(int j = 0; j < Start.length; j++){
                Node nd = new Node(Start[i][j].getX(), Start[i][j].getY(), Start.length, Start.length);
                nd.setImgX(Start[i][j].getImgX());
                nd.setImgY(Start[i][j].getImgY());
                startP[i][j] = nd;
            }
        }

        start = new Picture(bim, startP);
        end = End;

        addMenus(settings);
        framesPerSecond = settings.getTweenImageValue();
        seconds = settings.getSeconds();
        anim = new Timer(seconds*10, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                animation();
                if (currFrame > seconds*framesPerSecond) {
                    anim.stop();
                    currFrame = 1;
                }
            }
        });

        c.add(start);

        setSize(650,700);
        setVisible(true);
    }// end Constructor

    public void animation() {
        System.out.println("Entered Animation");
        int x, y, x1, x2, y1, y2;
        for (int i = 0; i < end.length; i++) {
            for (int j = 0; j < end[i].length; j++) {
                x1 = start.getPoints()[i][j].getImgX();
                y1 = start.getPoints()[i][j].getImgY();
                x2 = end[i][j].getImgX();
                y2 = end[i][j].getImgY();

                x = ((x2-x1) * currFrame/(framesPerSecond*seconds)) + x1;
                y = ((y2-y1) * currFrame/(framesPerSecond*seconds)) + y1;

                // Change the coordinates of x and y of the start panel and redraw.
                start.getPoints()[i][j].setImgX(x);
                start.getPoints()[i][j].setImgY(y);
            }
        }
        currFrame++;
        start.repaint();

    }

    public void resetPreview() {
        for(int i = 0; i < originalP.length; i++) {
            for (int j = 0; j < originalP.length; j++) {
                startP[i][j].setImgX(originalP[i][j].getImgX());
                startP[i][j].setImgY(originalP[i][j].getImgY());
            }
        }
        start.repaint();
    }

    private void addMenus(PopupSettings settings){
        //Export will produce a window
        JMenuItem FileExport = new JMenuItem("Export");
        FileExport.setEnabled(false);
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

        JMenuItem startButton = new JMenuItem("Start Animation");
        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                anim.start();
            }
        });

        JMenuItem stopButton = new JMenuItem("Stop Animation");
        stopButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                anim.stop();
            }
        });

        JMenuItem resetButton = new JMenuItem("Reset");
        resetButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("Reset Button Functionality");
                resetPreview();
            }
        });

        //Initialize the menubar and add it to the JFrame
        JMenuBar menuBar = new JMenuBar();
        this.setJMenuBar(menuBar);
        menuBar.add(File);
        menuBar.add(startButton);
        menuBar.add(stopButton);
        menuBar.add(resetButton);
    }//end addMenus()

}//end class
