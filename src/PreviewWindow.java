/* Authors: Caitlin Jones, Chelina Ortiz M
 * Date: 11/16/18
 * Project: CS 335 Program 3 - JMorph
 * References: Previous projects from this semester (Rubber-banding, image processing, grid and node creation, etc.)
 * Notes: This class sets the window where the preview of the morphing is set (animation of the morphing preview)
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
    private JMenuItem status; //because it's needed inside an event listener

    /* Default constructor of the preview window, gets a panel with an image and shows the linear transformation of
     the points from the starting to the ending image*/
    public PreviewWindow(PopupSettings settings, BufferedImage bim, Node[][] Start, Node[][] End){
        super("Preview");
        originalP = Start; // Store the 2D array of control points that was originally passed in for easy resetting.
        startP = new Node[Start.length][Start.length]; // Initialize a new array to store a copy of the original array.

        // Populate the copy of the array with new Node objects native to the PreviewWindow class only
        for(int i = 0; i < Start.length; i++){
            for(int j = 0; j < Start.length; j++){
                Node nd = new Node(Start[i][j].getX(), Start[i][j].getY(), Start.length, Start.length, start.getPicture().getWidth(), start.getPicture().getHeight());
                nd.setImgX(Start[i][j].getImgX());
                nd.setImgY(Start[i][j].getImgY());
                startP[i][j] = nd;
            }
        }

        start = new Picture(bim, startP); // Create a new panel of Picture class.
        end = End;

        addMenus(settings);
        framesPerSecond = settings.getTweenImageValue(); // Retrieve the values from the settings window
        seconds = settings.getSeconds();
        anim = new Timer(seconds*10, new ActionListener() { // Timer to allow the animation to be visible
            public void actionPerformed(ActionEvent e) {
                animation();
                if (currFrame > seconds*framesPerSecond) { // If we are creating more frames than we need, the animation must stop.
                    anim.stop();
                    currFrame = 1; // Reset the number of frames for future use
                    status.setText("Status: Finished");
                }
            }
        });

        //add the Picture panel to the JFrame
        c.add(start);
        setSize(650,700);
        setVisible(true);
    }// end Constructor

    // Animation: Will use linear transformation to compute the amount of pixels a control point needs to move to go from
    // the starting image to the position on the ending image.
    public void animation() {
        int x, y, x1, x2, y1, y2;
        for (int i = 0; i < end.length; i++) {
            for (int j = 0; j < end[i].length; j++) {
                x1 = start.getPoints()[i][j].getImgX(); // Get the x coordinate of the pixel the point is in (starting image)
                y1 = start.getPoints()[i][j].getImgY(); // Get the y coordinate of the pixel the point is in (starting image)
                x2 = end[i][j].getImgX(); // Get the y coordinate of the pixel the point is in (ending image)
                y2 = end[i][j].getImgY(); // Get the y coordinate of the pixel the point is in (ending image)
                // Compute the difference of the points depending on the number of frames rendered so far (both for x and y)
                x = ((x2-x1) * currFrame/(framesPerSecond*seconds)) + x1;
                y = ((y2-y1) * currFrame/(framesPerSecond*seconds)) + y1;

                // Change the coordinates of x and y of the start panel
                start.getPoints()[i][j].setImgX(x);
                start.getPoints()[i][j].setImgY(y);
            }
        }
        currFrame++; // Increase the number of frames rendered
        start.repaint(); //repaint each frame
    }//End animation()

    // Reset the preview values to the original position of the preview
    public void resetPreview() {
        for(int i = 0; i < originalP.length; i++) {
            for (int j = 0; j < originalP.length; j++) {
                startP[i][j].setImgX(originalP[i][j].getImgX());
                startP[i][j].setImgY(originalP[i][j].getImgY());
            }
        }
        start.repaint();
    }//End resetPreview()

    private void addMenus(PopupSettings settings){
        //Initialize the menuBar

        //Export will produce a savable video (Disabled until Morph Part2)
        JMenuItem FileExport = new JMenuItem("Export");
        FileExport.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                System.out.println("Export to video file");
            }
        });
        FileExport.setEnabled(false);

        //The settings will transfer between the Morph JFrame and the PreviewWindow JFrame
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

        //close the PreviewWindow JFrame
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

        //Initialize the buttons on the menubar (no submenus)
        JMenuItem startButton = new JMenuItem("Start Animation");
        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                anim.start();
                status.setText("Status: Running");
            }
        });

        JMenuItem stopButton = new JMenuItem("Stop Animation");
        stopButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                anim.stop();
                status.setText("Status: Stopped");
            }
        });

        JMenuItem resetButton = new JMenuItem("Reset");
        resetButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //Reset the PreviewWindow to the original locations of the Nodes
                resetPreview();
            }
        });

        status = new JMenuItem("Status: Not Running");

        //Initialize the menubar and add it to the JFrame
        JMenuBar menuBar = new JMenuBar();
        this.setJMenuBar(menuBar);
        menuBar.add(File);
        menuBar.add(startButton);
        menuBar.add(stopButton);
        menuBar.add(resetButton);
        menuBar.add(status);
    }//end addMenus()

}//end class
