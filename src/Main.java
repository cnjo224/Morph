/* Authors: Caitlin Jones, Chelina Ortiz M
 * Date: 11/16/18
 * Project: CS 335 Program 3 - Morph
 * References:
 * Notes:
 *
 */

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;

public class Main extends JFrame {
    private BufferedImage image;

    //initializer for the JFrame
    public Main(){
        super("Morph");
        this.createMenus();
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
                PopupSettings settings = new PopupSettings();
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

        JMenuItem Morph = new JMenuItem("Morph");
        Morph.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                System.out.println("Perform Morph");
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
        menuBar.add(Morph);
        menuBar.add(Reset);
    }//end createMenus function




    public static void main(String[] args){
        JFrame main = new Main();
        main.setSize(600,600);
        main.setVisible(true);
        main.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e){
                System.exit(0);
            }
        });
    }//end main

}//End class
