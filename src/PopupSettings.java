import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

public class PopupSettings extends JFrame {
    private int gridSizeValue=10, tweenImageValue=30;
    public PopupSettings(){
        super("Settings");

        JLabel gridSizeLabel = new JLabel("Grid Size: ");

        //length of one size of the grid. Must be square grid size
        SpinnerModel gridModel = new SpinnerNumberModel(10, 10, 10, 1);
        JSpinner gridSize = new JSpinner(gridModel);
        gridSize.setEnabled(false);

        JLabel tweenImagesLabel = new JLabel("Frames Per Second: ");

        //how many frames per second to render
        SpinnerModel model = new SpinnerNumberModel( 30,1,100,1);
        JSpinner tweenImages = new JSpinner(model);

        JButton okButton = new JButton("Okay");
        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                //only set variables when okButton is pressed.
                gridSizeValue = (Integer)gridSize.getValue();
                tweenImageValue = (Integer)tweenImages.getValue();
                PopupSettings.this.dispatchEvent(new WindowEvent(
                        PopupSettings.this, WindowEvent.WINDOW_CLOSING
                ));
            }
        });

        JButton resetButton = new JButton("Reset");
        resetButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                gridSize.setValue(10);
                tweenImages.setValue(30);
            }
        });

        //add components to JFrame
        add(gridSizeLabel);
        add(gridSize);
        add(tweenImagesLabel);
        add(tweenImages);
        add(okButton);
        add(resetButton);

        //Format and visualize JFrame
        setLayout(new GridLayout(3,2, 10, 10));
        setSize(400,125);
        setResizable(false);
        setVisible(true);
    }//end constructor

    //return set information
    public int getGridSizeValue(){return gridSizeValue;}
    public int getTweenImageValue(){return tweenImageValue;}

}//End class
