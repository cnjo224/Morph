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
    private int gridSizeDefault = 10, tweenImagesDefault = 30, previewSecondsDefault = 2;
    private int gridSizeValue=10, tweenImageValue=30, previewSeconds = 2;
    private boolean applyHit = false;

    //These must be global to ensure access within event listeners
    private JSpinner gridSize, tweenImages, secondsSelect;

    public PopupSettings(){
        super("Settings");

        //add WindowClosing Event
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent) {
                //if apply was hit, set the accessible values
                if(applyHit){
                    gridSizeValue = (Integer)gridSize.getValue();
                    tweenImageValue = (Integer)tweenImages.getValue();
                    previewSeconds = (Integer)secondsSelect.getValue();
                    applyHit = false;
                }else{
                    //if apply wasn't hit, then reset the spinners to previously applied values
                    gridSize.setValue(gridSizeValue);
                    tweenImages.setValue(tweenImageValue);
                    secondsSelect.setValue(previewSeconds);
                }
            }
        });

        JLabel gridSizeLabel = new JLabel("Grid Size: ");

        //length of one size of the grid. Must be square grid size
        SpinnerModel gridModel = new SpinnerNumberModel(gridSizeDefault, 10, 10, 1);
        gridSize = new JSpinner(gridModel);
        gridSize.setEnabled(false); //Currently only works on 10x10 grid (+2 for non-movable control points)

        JLabel tweenImagesLabel = new JLabel("Frames Per Second: ");

        //how many frames per second to render
        SpinnerModel model = new SpinnerNumberModel( tweenImagesDefault,1,100,1);
        tweenImages = new JSpinner(model);

        JLabel secondsLabel = new JLabel("Seconds of Preview: ");

        SpinnerModel secModel = new SpinnerNumberModel(previewSecondsDefault, 1, 10, 1);
        secondsSelect = new JSpinner(secModel);

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
                gridSize.setValue(gridSizeDefault);
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
        add(applyButton);
        add(resetButton);

        //Format and visualize JFrame
        setLayout(new GridLayout(4,2, 10, 10));
        setSize(400,175);
        setResizable(false);
    }//end constructor

    //return set information
    public int getGridSizeValue(){return gridSizeValue;} //Will use in Morph Part 2
    public int getTweenImageValue(){return tweenImageValue;}
    public int getSeconds(){return previewSeconds;}

}//End class
