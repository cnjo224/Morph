import javax.swing.*;

public class PopupSettings extends JFrame {
    public PopupSettings(){
        super("Settings");

        //length of one size of the grid. Must be square grid size
        SpinnerModel gridModel = new SpinnerNumberModel(10, 10, 10, 1);
        JSpinner gridSize = new JSpinner(gridModel);

        //how many frames per second to render
        SpinnerModel model = new SpinnerNumberModel( 30,1,100,1);
        JSpinner tweenImages = new JSpinner(model);










        setSize(300,300);
        setResizable(false);
        setVisible(true);
    }//end constructor
}//End class
