/* Authors: Caitlin Jones, Chelina Ortiz M
 * Date: 11/16/18
 * Project: CS 335 Program 3 - Morph
 * References: Previous projects from this semester (Rubber-banding, image processing, grid and node creation, etc.),
 *      https://stackoverflow.com/questions/3514158/how-do-you-clone-a-bufferedimage
 * Notes: This class
 */

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.Image;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.*;

public class JMorph extends JFrame {
    public PopupSettings settings = new PopupSettings();
    private BufferedImage image = readImage("res/gal.jpg"), image2 = readImage("res/lion.jpg");
    private Picture startView, endView;
    private int rows = 10, columns = 10;
    private Container c = getContentPane();
    private boolean isDragging = false;

    //initializer for the JFrame
    public JMorph(){
        super("Morph");


        startView = new Picture(image, rows, columns);
        startView.addMouseListener(new MouseListener() {
            public void mouseClicked(MouseEvent e) {}
            public void mousePressed(MouseEvent e) {
                if(startView.clickInPoint(e.getPoint())){
                    endView.setActiveNode(startView.getActiveNode().getX(), startView.getActiveNode().getY());
                    endView.repaint();
                    isDragging = true;
                }
            }
            public void mouseReleased(MouseEvent e) {
                isDragging = false;
                startView.clearActiveNode();
                endView.clearActiveNode();
            }
            public void mouseEntered(MouseEvent e) {}
            public void mouseExited(MouseEvent e) {}
        });
        startView.addMouseMotionListener(new MouseMotionListener() {
            public void mouseDragged(MouseEvent e) {
                if(isDragging){
                    if(e.getX() >=0 && e.getX() < startView.getWidth() && e.getY() >=0 && e.getY() < startView.getHeight()) {
                        startView.movePoint(e.getX(), e.getY());
                        startView.repaint();
                    }
                }
            }
            public void mouseMoved(MouseEvent e) {}
        });

        //startView = new Picture(image, rows, columns);
        endView = new Picture(image2, rows, columns);
        endView.addMouseListener(new MouseListener() {
            public void mouseClicked(MouseEvent e) {}
            public void mousePressed(MouseEvent e) {
                if(endView.clickInPoint(e.getPoint())){
                    startView.setActiveNode(endView.getActiveNode().getX(), endView.getActiveNode().getY());
                    startView.repaint();
                    isDragging = true;
                }
            }
            public void mouseReleased(MouseEvent e) {
                isDragging = false;
                endView.clearActiveNode();
                startView.clearActiveNode();
            }
            public void mouseEntered(MouseEvent e) {}
            public void mouseExited(MouseEvent e) {}
        });
        endView.addMouseMotionListener(new MouseMotionListener() {
            public void mouseDragged(MouseEvent e) {
                if(isDragging){
                    if(e.getX() >=0 && e.getX() < endView.getWidth() && e.getY() >=0 && e.getY() < endView.getHeight()) {
                        endView.movePoint(e.getX(), e.getY());
                        endView.repaint();
                    }
                }
            }
            public void mouseMoved(MouseEvent e) {}
        });

        c.add(startView, BorderLayout.WEST);
        c.add(endView, BorderLayout.EAST);

        this.createMenus();
        setSize(startView.getBim().getWidth()+endView.getBim().getWidth()+30,startView.getBim().getHeight()+70);
        setVisible(true);
    }

    // This method reads an Picture object from a file indicated by
    // the string provided as the parameter.  The image is converted
    // here to a BufferedImage object, and that new object is the returned
    // value of this method.
    // The mediatracker in this method can throw an exception

    private BufferedImage readImage (String file) {
        System.out.println(file);
        Image image = Toolkit.getDefaultToolkit().getImage(getClass().getResource(file));
        MediaTracker tracker = new MediaTracker (new Component () {});
        tracker.addImage(image, 0);
        try { tracker.waitForID (0); }
        catch (InterruptedException e) {}
        BufferedImage bim = new BufferedImage(image.getWidth(this), image.getHeight(this), BufferedImage.TYPE_INT_RGB);
        Graphics2D big = bim.createGraphics();
        big.drawImage (image, 0, 0, this);
        return bim;
    }

    public void createMenus(){

        JMenuItem FileOpenLeft = new JMenuItem("Open Left Image");
        FileOpenLeft.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                System.out.println("Open file");
                JFileChooser fc = new JFileChooser(".");
                int returnVal = fc.showOpenDialog(JMorph.this);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = fc.getSelectedFile();
                    try {
                        BufferedImage image3 = ImageIO.read(file);
                        if(image3.getWidth() != 600 || image3.getHeight() != 600){
                            System.out.println("Image must be of height 600 and width 600!");
                        }else{
                            image = image3;
                            startView.changeImage(image);
                        }
                    } catch (IOException e1) {System.out.println(e1.getMessage());}
                }
            }
        });

        JMenuItem FileOpenRight = new JMenuItem("Open Right Image");
        FileOpenRight.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                System.out.println("Open file");
                JFileChooser fc = new JFileChooser(".");
                int returnVal = fc.showOpenDialog(JMorph.this);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = fc.getSelectedFile();
                    try {
                        BufferedImage image3 = ImageIO.read(file);
                        if(image3.getWidth() != 600 || image3.getHeight() != 600){
                            System.out.println("Image must be of height 600 and width 600!");
                        }else{
                            image2 = image3;
                            endView.changeImage(image2);
                        }
                    } catch (IOException e1) {System.out.println(e1.getMessage());}
                }
            }
        });

        JMenuItem FileSettings = new JMenuItem("Settings");
        FileSettings.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                settings.setVisible(true);
                settings.addWindowListener(new WindowAdapter() {
                    public void windowClosing(WindowEvent windowEvent) {
                        System.out.println(settings.applyHit);
                        //call member functions of settings page here
                        // TODO: Add change listener to brightness
                        if(settings.applyHit) {
                            if(settings.getGridSizeValue() != rows){
                                rows = settings.getGridSizeValue();
                                columns = settings.getGridSizeValue();
                                startView.changeGridSize(rows);
                                endView.changeGridSize(rows);
                            }

                            if(settings.startBright) {
                                image = startView.changeBrightness(settings.getStartBChange());
                                settings.startBright = false;
                            }
                            if(settings.endBright) {
                                image2 = endView.changeBrightness(settings.getEndBchange());
                                settings.endBright = false;
                            }
                        }
                        settings.applyHit = false;
                    }
                });
            }
        });

        JMenuItem FileExit = new JMenuItem("Exit");
        FileExit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                System.exit(0);
            }
        });

        JMenu FileMenu = new JMenu("File");
        FileMenu.add(FileOpenLeft);
        FileMenu.add(FileOpenRight);
        FileMenu.add(FileSettings);
        FileMenu.add(FileExit);

        JMenuItem Preview = new JMenuItem("Preview");
        Preview.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                System.out.println("Open Preview Page");
                PreviewWindow OpenPreview = new PreviewWindow(settings, image, startView.getPoints(), endView.getPoints(), rows);
            }
        });

        JMenuItem Morph = new JMenuItem("Morph");
        Morph.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                System.out.println("Open Morph Page");
                MorphWindow OpenMorph = new MorphWindow(settings, copyImage(image), startView.getPoints(), copyImage(image2), endView.getPoints(), rows);
            }
        });

        JMenuItem Reset = new JMenuItem("Reset");
        Reset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                startView.resetPicture();
                endView.resetPicture();
                System.out.println("Reset Images");
            }
        });

        JMenuBar menuBar = new JMenuBar();
        this.setJMenuBar(menuBar);
        menuBar.add(FileMenu);
        menuBar.add(Preview);
        menuBar.add(Morph);
        menuBar.add(Reset);
    }//end createMenus function

    public BufferedImage copyImage(BufferedImage init) {
        ColorModel m = init.getColorModel();
        boolean isAlphaPremultiplied = getColorModel().isAlphaPremultiplied();
        WritableRaster raster = init.copyData(null);
        return new BufferedImage(m, raster, isAlphaPremultiplied, null);
    }

    public static void main(String[] args){
        JFrame main = new JMorph();
        main.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e){
                System.exit(0);
            }
        });
    }//end main

}//End class
