/* Authors: Caitlin Jones, Chelina Ortiz M
 * Date: 11/16/18
 * Project: CS 335 Program 3 - Morph
 * References:
 * Notes:
 *
 */

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.Image;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;

public class Main extends JFrame {
    public PopupSettings settings = new PopupSettings();
    private BufferedImage image;
    private Picture imgView;
    private int rows = 10, columns = 10;
    private Container c = getContentPane();

    //initializer for the JFrame
    public Main(){
        super("Morph");
        this.createMenus();

        imgView = new Picture(readImage("res/boat.gif"), rows, columns);
        //imgView = new Picture(image, rows, columns);
        c.add(imgView);

        setSize(600,600);
        setVisible(true);
    }

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
                (image.getWidth(this), image.getHeight(this),
                        BufferedImage.TYPE_INT_RGB);
        Graphics2D big = bim.createGraphics();
        big.drawImage (image, 0, 0, this);
        return bim;
    }

    public void createMenus(){

        JMenuItem FileOpen = new JMenuItem("Open");
        FileOpen.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                System.out.println("Open file");
                JFileChooser fc = new JFileChooser(".");
                int returnVal = fc.showOpenDialog(Main.this);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = fc.getSelectedFile();
                    try {
                        image = ImageIO.read(file);
                    } catch (IOException e1) {}
                }
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
                System.exit(0);
            }
        });

        JMenu FileMenu = new JMenu("File");
        FileMenu.add(FileOpen);
        FileMenu.add(FileSettings);
        FileMenu.add(FileExit);

        JMenuItem Preview = new JMenuItem("Preview");
        Preview.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                System.out.println("Open Preview Page");
                PreviewWindow OpenPreview = new PreviewWindow(settings);
            }
        });

        JMenuItem Reset = new JMenuItem("Reset");
        Reset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                System.out.println("Reset Images");
            }
        });

        JMenuBar menuBar = new JMenuBar();
        this.setJMenuBar(menuBar);
        menuBar.add(FileMenu);
        menuBar.add(Preview);
        menuBar.add(Reset);
    }//end createMenus function


    public static void main(String[] args){
        JFrame main = new Main();
        main.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e){
                System.exit(0);
            }
        });
    }//end main

}//End class
