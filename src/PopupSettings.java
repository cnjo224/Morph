/* Authors: Caitlin Jones, Chelina Ortiz M
 * Date: 11/16/18
 * Project: CS 335 Program 3 - Morph
 * References: Caitlin's Bombs Project
 * Notes: This is a
 *
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class PopupSettings extends JFrame {
    private int gridSizeDefault = 10, tweenImagesDefault = 30;
    private int gridSizeValue=10, tweenImageValue=30;
    private boolean applyHit = false;

    //These must be global to ensure access within event listeners
    private JSpinner gridSize, tweenImages;

    public PopupSettings(){
        super("Settings");

        //add WindowClosing Event
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent) {
                //if apply was hit, set the accessible values
                if(applyHit){
                    gridSizeValue = (Integer)gridSize.getValue();
                    tweenImageValue = (Integer)tweenImages.getValue();
                    applyHit = false;
                }else{//if apply wasn't hit, then reset the spinners to previously applied values
                    gridSize.setValue(gridSizeValue);
                    tweenImages.setValue(tweenImageValue);
                }
            }
        });

        JLabel gridSizeLabel = new JLabel("Grid Size: ");

        //length of one size of the grid. Must be square grid size
        SpinnerModel gridModel = new SpinnerNumberModel(gridSizeDefault, 10, 10, 1);
        gridSize = new JSpinner(gridModel);
        gridSize.setEnabled(false);

        JLabel tweenImagesLabel = new JLabel("Frames Per Second: ");

        //how many frames per second to render
        SpinnerModel model = new SpinnerNumberModel( tweenImagesDefault,1,100,1);
        tweenImages = new JSpinner(model);

        JButton applyButton = new JButton("Apply");
        applyButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                applyHit = true;
            }
        });

        JButton resetButton = new JButton("Reset");
        resetButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                gridSize.setValue(gridSizeDefault);
                tweenImages.setValue(tweenImagesDefault);
            }
        });

        //add components to JFrame
        add(gridSizeLabel);
        add(gridSize);
        add(tweenImagesLabel);
        add(tweenImages);
        add(applyButton);
        add(resetButton);

        //Format and visualize JFrame
        setLayout(new GridLayout(3,2, 10, 10));
        setSize(400,125);
        setResizable(false);
    }//end constructor

    //return set information
    public int getGridSizeValue(){return gridSizeValue;}
    public int getTweenImageValue(){return tweenImageValue;}

}//End class
