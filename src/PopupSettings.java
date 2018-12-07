/* Authors: Caitlin Jones, Chelina Ortiz M
 * Date: 11/16/18
 * Project: CS 335 Program 3 - JMorph
 * References: Previous projects from this semester (Rubber-banding, image processing, grid and node creation, etc.)
 * Notes: This class initializes a JFrame for holding and accessing the Morphing and control point settings
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class PopupSettings extends JFrame {
    private int tweenImagesDefault = 30, previewSecondsDefault = 2;
    private int gridSizeValue=10, tweenImageValue=30, previewSeconds = 2, startBChange = 50, endBchange = 50;
    private boolean applyHit = false;

    //These must be global to ensure access within event listeners
    private JSpinner tweenImages, secondsSelect;
    private JSlider startBrightness, endBrightness;
    private JComboBox gridSize;

    public PopupSettings(){
        super("Settings");

        //add WindowClosing Event
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent) {
                //if apply was hit, set the accessible values
                if(applyHit){
                    getGridSize("value");
                    tweenImageValue = (Integer)tweenImages.getValue();
                    previewSeconds = (Integer)secondsSelect.getValue();
                    startBChange = startBrightness.getValue();
                    endBchange = endBrightness.getValue();
                    applyHit = false;
                }else{
                    //if apply wasn't hit, then reset the spinners to previously applied values
                    getGridSize("index");
                    tweenImages.setValue(tweenImageValue);
                    secondsSelect.setValue(previewSeconds);
                    startBrightness.setValue(100);
                    endBrightness.setValue(100);
                }
            }
        });

        JLabel gridSizeLabel = new JLabel("Grid Size: ");

        //length of one size of the grid. Must be square grid size
        gridSize = new JComboBox();
        gridSize.addItem("5 x 5");
        gridSize.addItem("10 x 10");
        gridSize.addItem("20 x 20");
        gridSize.setSelectedIndex(1);

        JLabel tweenImagesLabel = new JLabel("Frames Per Second: ");

        //how many frames per second to render
        SpinnerModel model = new SpinnerNumberModel( tweenImagesDefault,1,100,1);
        tweenImages = new JSpinner(model);

        JLabel secondsLabel = new JLabel("Seconds of Preview: ");

        SpinnerModel secModel = new SpinnerNumberModel(previewSecondsDefault, 1, 10, 1);
        secondsSelect = new JSpinner(secModel);

        JLabel startBrightLab = new JLabel("Starting Image Brightness");
        startBrightness = new JSlider(JSlider.HORIZONTAL, 0, 200, 100);
        startBrightness.setMajorTickSpacing(50);
        startBrightness.setMinorTickSpacing(25);
        startBrightness.setPaintTicks(true);
        startBrightness.setSnapToTicks(true);
        startBrightness.setPaintLabels(true);

        JLabel endBrightLab = new JLabel("Ending Image Brightness");
        endBrightness = new JSlider(JSlider.HORIZONTAL, 0, 200, 100);
        endBrightness.setMajorTickSpacing(50);
        endBrightness.setMinorTickSpacing(25);
        endBrightness.setPaintTicks(true);
        endBrightness.setSnapToTicks(true);
        endBrightness.setPaintLabels(true);

        //When apply is clicked, the settings are saved, otherwise they're discarded when the window closes
        JButton applyButton = new JButton("Apply");
        applyButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                applyHit = true;
            }
        });

        //Reset the settings values to default
        JButton resetButton = new JButton("Reset");
        resetButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                gridSize.setSelectedIndex(1);
                tweenImages.setValue(tweenImagesDefault);
                secondsSelect.setValue(previewSecondsDefault);
            }
        });

        //add components to JFrame
        add(gridSizeLabel);
        add(gridSize);
        add(tweenImagesLabel);
        add(tweenImages);
        add(secondsLabel);
        add(secondsSelect);
        add(startBrightLab);
        add(startBrightness);
        add(endBrightLab);
        add(endBrightness);
        add(applyButton);
        add(resetButton);

        //Format and visualize JFrame
        setLayout(new GridLayout(6,2, 10, 10));
        setSize(400,350);
        setResizable(false);
    }//end constructor

    private void getGridSize(String flag){
        if(flag == "value") {
            switch (gridSize.getSelectedIndex()) {
                case 0:
                    gridSizeValue = 5;
                    break;
                case 1:
                    gridSizeValue = 10;
                    break;
                case 2:
                    gridSizeValue = 20;
            }//end switch
        }else{
            switch (gridSizeValue){
                case 5:
                    gridSize.setSelectedIndex(0);
                    break;
                case 10:
                    gridSize.setSelectedIndex(1);
                    break;
                case 20:
                    gridSize.setSelectedIndex(2);
            }
        }
    }//end getGridSize

    //return set information
    public int getGridSizeValue(){return gridSizeValue;} //Will use in Morph Part 2
    public int getTweenImageValue(){return tweenImageValue;}
    public int getSeconds(){return previewSeconds;}
    public int getStartBChange() { return startBChange; }
    public int getEndBchange() { return endBchange; }
}//End class
