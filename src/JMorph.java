/* Authors: Caitlin Jones, Chelina Ortiz M
 * Date: 11/16/18
 * Project: CS 335 Program 3 - JMorph
 * References: Previous projects from this semester (Rubber-banding, image processing, grid and node creation, etc.)
 * Notes: This class sets up the main JFrame where all control elements stem from
 */

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.Image;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;

public class JMorph extends JFrame {
    //The settings object is global across the Morph and PreviewWindow panes
    public PopupSettings settings = new PopupSettings();
    private BufferedImage image = readImage("res/boat.gif"), image2 = readImage("res/boatR.jpg");
    private Picture imgView, endView;
    private int rows = 10, columns = 10;
    private Container c = getContentPane();
    private boolean isDragging = false;

    //initializer for the JFrame
    public JMorph(){
        super("Morph");
        this.createMenus();

        //create two panels of class Picture to handle modification of Nodes and holding beginning and final images
        imgView = new Picture(image, rows, columns);
        imgView.addMouseListener(new MouseListener() {
            public void mouseClicked(MouseEvent e) {}
            public void mousePressed(MouseEvent e) {
                //if the click was inside a Node point, then approve it to move. Recolor the corresponding node on the opposite panel
                if(imgView.clickInPoint(e.getPoint())){
                    endView.setActiveNode(imgView.getActiveNode().getX(), imgView.getActiveNode().getY());
                    endView.repaint();
                    isDragging = true;
                }
            }
            public void mouseReleased(MouseEvent e) {
                //when the mouse button is released, stop dragging the control point
                isDragging = false;
                imgView.clearActiveNode();
                endView.clearActiveNode();
                endView.repaint();
            }
            public void mouseEntered(MouseEvent e) {}
            public void mouseExited(MouseEvent e) {}
        });//End imgView mouseListener
        imgView.addMouseMotionListener(new MouseMotionListener() {
            public void mouseDragged(MouseEvent e) {
                //While the control point is being dragged, redraw it in the new location
                if(isDragging){
                    //make sure the control point stays within the bounds of the panel
                    if(e.getX() >=0 && e.getX() < imgView.getWidth() && e.getY() >=0 && e.getY() < imgView.getHeight()) {
                        Node nd = imgView.getActiveNode();
                        nd.setImgX(e.getX());
                        nd.setImgY(e.getY());
                        imgView.repaint();
                    }
                }
            }
            public void mouseMoved(MouseEvent e) {}
        });//End imgView mouseMotionListener

        //imgView = new Picture(image, rows, columns);
        endView = new Picture(image2, rows, columns);
        endView.addMouseListener(new MouseListener() {
            public void mouseClicked(MouseEvent e) {}
            public void mousePressed(MouseEvent e) {
                //if the click was inside a Node point, then approve it to move. Recolor the corresponding node on the opposite panel
                if(endView.clickInPoint(e.getPoint())){
                    imgView.setActiveNode(endView.getActiveNode().getX(), endView.getActiveNode().getY());
                    imgView.repaint();
                    isDragging = true;
                }
            }
            public void mouseReleased(MouseEvent e) {
                //when the mouse button is released, stop dragging the control point
                isDragging = false;
                endView.clearActiveNode();
                imgView.clearActiveNode();
                imgView.repaint();
            }
            public void mouseEntered(MouseEvent e) {}
            public void mouseExited(MouseEvent e) {}
        });//End endView MouseListener
        endView.addMouseMotionListener(new MouseMotionListener() {
            public void mouseDragged(MouseEvent e) {
                //While the control point is being dragged, redraw it in the new location
                if(isDragging){
                    //make sure the control point stays within the bounds of the panel
                    if(e.getX() >=0 && e.getX() < endView.getWidth() && e.getY() >=0 && e.getY() < endView.getHeight()) {
                        Node nd = endView.getActiveNode();
                        nd.setImgX(e.getX());
                        nd.setImgY(e.getY());
                        endView.repaint();
                    }
                }
            }
            public void mouseMoved(MouseEvent e) {}
        });//End endView mouseMotionListener

        c.add(imgView, BorderLayout.WEST);
        c.add(endView, BorderLayout.EAST);

        setSize(1220,655);
        //Resizing must not be allowed to ensure that control points don't exit panel when drawing
        setResizable(false);
        setVisible(true);
    }//End class constructor

    // This method reads an Picture object from a file indicated by
    // the string provided as the parameter.  The image is converted
    // here to a BufferedImage object, and that new object is the returned
    // value of this method.
    // The mediatracker in this method can throw an exception

    private BufferedImage readImage (String file) {
        Image image = Toolkit.getDefaultToolkit().getImage(getClass().getResource(file));
        MediaTracker tracker = new MediaTracker (new Component () {});
        tracker.addImage(image, 0);
        try { tracker.waitForID (0); }
        catch (InterruptedException e) {}
        BufferedImage bim = new BufferedImage
                (600, 600,
                /*(image.getWidth(this), image.getHeight(this),*/
                        BufferedImage.TYPE_INT_RGB);
        Graphics2D big = bim.createGraphics();
        big.drawImage (image, 0, 0, this);
        return bim;
    }//End readImage

    public void createMenus(){
        //Initialize the menuBar and its components

        //Initialize sub options under the File menu
        JMenuItem FileOpen = new JMenuItem("Open");
        FileOpen.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                System.out.println("Open file");
                JFileChooser fc = new JFileChooser(".");
                int returnVal = fc.showOpenDialog(JMorph.this);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = fc.getSelectedFile();
                    try {
                        image = ImageIO.read(file);
                    } catch (IOException e1) {}
                }
            }
        });

        //Settings is persistent in all the places it is viewed
        JMenuItem FileSettings = new JMenuItem("Settings");
        FileSettings.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                settings.setVisible(true);
                settings.addWindowListener(new WindowAdapter() {
                    public void windowClosing(WindowEvent windowEvent) {
                        //call member functions of settings page here
                    }
                });
            }
        });

        //Close the program
        JMenuItem FileExit = new JMenuItem("Exit");
        FileExit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                System.exit(0);
            }
        });

        JMenu FileMenu = new JMenu("File");
        FileMenu.add(FileOpen);
        FileMenu.add(FileSettings);
        FileMenu.add(FileExit);

        //Initialize buttons on the menu bar (no submenus)
        JMenuItem Preview = new JMenuItem("Preview");
        Preview.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                //Initialize and open a PreviewWindow
                PreviewWindow OpenPreview = new PreviewWindow(settings, image, imgView.getPoints(), endView.getPoints());
            }
        });

        //Reset the screen to the default layout
        JMenuItem Reset = new JMenuItem("Reset");
        Reset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                imgView.resetPicture();
                endView.resetPicture();
            }
        });

        //set the menuBar to the JFrame
        JMenuBar menuBar = new JMenuBar();
        this.setJMenuBar(menuBar);
        menuBar.add(FileMenu);
        menuBar.add(Preview);
        menuBar.add(Reset);
    }//end createMenus function


    //Initialize the program driver
    public static void main(String[] args){
        JFrame main = new JMorph();
        main.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e){
                System.exit(0);
            }
        });
    }//end main

}//End class
