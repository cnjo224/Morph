/* Authors: Caitlin Jones, Chelina Ortiz M
 * Date: 11/16/18
 * Project: CS 335 Program 3 - JMorph
 * References: Previous projects from this semester (Rubber-banding, image processing, grid and node creation, etc.)
 * Notes: This class creates the panels that contain the image and the control points to setup for the morph.
 */

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Picture extends JPanel {
    private int rows, cols, trueRow, trueCol;
    private BufferedImage bim;
    private Node points[][];
    private Node activeNode;

    // Default constructor for the Picture class, creates a panel that contains the image and the control points to morph.
    public Picture(BufferedImage bim, int rows, int cols) {
        this.bim = bim; // Buffered Image in the panel
        this.rows = rows; // Number of rows that contain control points
        this.cols = cols; // Number of columns that contain control points
        trueRow = rows+2; // Add 2 rows to create a grid with the right number of control points
        trueCol = cols+2; // Add 2 columns to create a grid with the right number of control points
        points = new Node[trueRow][trueCol]; // 2D array of control points
        setPreferredSize(new Dimension(600, 600)); // Size of the panel
        // Initialize the 2D array of control points
        for (int i = 0; i < trueCol; i++) {
            for (int j = 0; j < trueRow; j++) {
                points[i][j] = new Node(i, j, trueCol, trueRow);
            }
        }
    }//End default constructor

    // Constructor for the Picture class, takes in a 2D array of control points to modify it
    public Picture(BufferedImage bim, Node[][] tempPoints){
        this.bim = bim;
        rows = tempPoints.length-2; // Retrieve the number of rows from the passed 2D array
        cols = tempPoints.length-2; // Retrieve the number of columns from the passed 2D array
        trueRow = rows+2; // Reset the number of true rows and columns (based on the desired number of control points)
        trueCol = cols+2;
        points = tempPoints; // Reset the control point array
        setPreferredSize(new Dimension(600, 600));
    }//End alternative constructor

    // Function to reset the 2D array to its default values
    public void resetPicture() {
        for (int i = 0; i < trueCol; i++) {
            for (int j = 0; j < trueRow; j++) {
                points[i][j].resetNode();
            }
        }
        repaint(); // Redraw the array back to default
    }//End resetPicture()

    // Return the 2D array of control points
    public Node[][] getPoints() { return points; }

    // Change the color of an individual node in the 2D array
    public void changeNodeColor(Node n, Color color) { n.setColor(color); }

    // Draw the connecting lines in between the control points
    public void drawLines(Graphics g) {
        int x, y, xR, yR, xD, yD, xDiag, yDiag;

        for (int i = 0; i < trueCol; i++) {
            for (int j = 0; j < trueRow; j++) {
                x = points[i][j].getImgX()+2;
                y = points[i][j].getImgY()+2;

                if (i < cols+1) { // Set the lines to the right
                    xR = points[i+1][j].getImgX()+2;
                    yR = points[i+1][j].getImgY()+2;
                    g.drawLine(x, y, xR, yR); // Draw connecting line between the points
                }
                if (j < rows+1) { // Set the lines going down
                    xD = points[i][j+1].getImgX()+2;
                    yD = points[i][j+1].getImgY()+2;
                    g.drawLine(x, y, xD, yD); // Draw connecting line between the points
                }
                if (i < cols+1 && j < rows+1) { // Set the lines diagonally to the bottom right control point
                    xDiag = points[i+1][j+1].getImgX()+2;
                    yDiag = points[i+1][j+1].getImgY()+2;
                    g.drawLine(x, y, xDiag, yDiag); // Draw connecting line between the points
                }
            }
        }
    }//End drawLines()

    // Paint component to draw the image, the control points and the connecting lines.
    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D)g;
        g2d.clearRect(0, 0, getWidth(), getHeight());
        g2d.drawImage(bim, 0, 0, this); // Draw image

        // Draw control points
        for (int i = 0; i < trueCol; i++) {
            for (int j = 0; j < trueRow; j++) {
                g2d.setColor(points[i][j].getColor());
                g2d.fillRect(points[i][j].getImgX(),points[i][j].getImgY(), 5,5);
            }
        }

        // Draw connecting lines
        drawLines(g);
    }//End paintComponent()

    // Identify if there is a click on a control point and activate it.
    public boolean clickInPoint(Point click){
        for(int i =0; i < points.length; i++){
            for(int j=0; j< points.length; j++){
                if(points[i][j].contains(click) && i != 0 && i != trueCol-1 && j != 0 && j != trueRow-1){
                    activeNode = points[i][j];
                    return true;
                }
            }
        }
        return false;
    }//End clickInPoint()

    // Activate node given (to change the color of the control point in both panels for easy reference)
    public void setActiveNode(int x, int y) {
        this.activeNode = points[x][y];
        changeNodeColor(activeNode, Color.RED);
    }

    // Gets the active node and changes it color
    public Node getActiveNode(){
        changeNodeColor(activeNode, Color.RED);
        return activeNode;
    }

    // Resets the node when it is no longer active.
    public void clearActiveNode(){
        if (activeNode != null) {
            changeNodeColor(activeNode, Color.BLACK);
            activeNode = null;
        }
    }
}//End class
