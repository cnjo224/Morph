/* Authors: Caitlin Jones, Chelina Ortiz M
 * Date: 11/16/18
 * Project: CS 335 Program 3 - JMorph
 * References: Previous projects from this semester (Rubber-banding, image processing, grid and node creation, etc.)
 * Notes: This class creates the panels that contain the image and the control points to setup for the morph.
 */

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;

public class Picture extends JPanel {
    private int rows, cols, trueRow, trueCol;
    private boolean drawNodes;
    private BufferedImage original, bim;
    private Node points[][];
    private Node activeNode;

    // Default constructor for the Picture class, creates a panel that contains the image and the control points to morph.
    public Picture(BufferedImage bim, int rows, int cols) {
        this.original = bim; // Buffered Image in the panel
        this.bim = copyImage(bim);
        this.rows = rows; // Number of rows that contain control points
        this.cols = cols; // Number of columns that contain control points
        trueRow = rows+2; // Add 2 rows to create a grid with the right number of control points
        trueCol = cols+2; // Add 2 columns to create a grid with the right number of control points
        points = new Node[22][22]; // 2D array of control points
        drawNodes = true;
        setPreferredSize(new Dimension(bim.getWidth(), bim.getHeight())); // Size of the panel
        // Initialize the 2D array of control points
        for (int i = 0; i < trueCol; i++) {
            for (int j = 0; j < trueRow; j++) {
                points[i][j] = new Node(i, j, trueCol, trueRow, bim.getWidth(), bim.getHeight());
            }
        }

        //Initialize the boundary for each control point
        //the Outside points don't need set because they never move
        for(int x = 1; x <= this.cols; x++){
            for(int y = 1; y <= this.rows; y++){
                int[] pointsX = {points[x-1][y-1].getImgX(), points[x][y-1].getImgX(), points[x+1][y].getImgX(), points[x+1][y+1].getImgX(), points[x][y+1].getImgX(), points[x-1][y].getImgX()};
                int[] pointsY = {points[x-1][y-1].getImgY(), points[x][y-1].getImgY(), points[x+1][y].getImgY(), points[x+1][y+1].getImgY(), points[x][y+1].getImgY(), points[x-1][y].getImgY()};
                points[x][y].resetBounds(pointsX, pointsY);
            }
        }


    }//End default constructor

    // Constructor for the Picture class, takes in a 2D array of control points to modify it
    public Picture(BufferedImage bim, Node[][] tempPoints, int size){
        this.bim = copyImage(bim);
        rows = size; // Retrieve the number of rows from the passed 2D array
        cols = size; // Retrieve the number of columns from the passed 2D array
        trueRow = rows+2; // Reset the number of true rows and columns (based on the desired number of control points)
        trueCol = cols+2;
        points = tempPoints; // Reset the control point array
        drawNodes = true;
        setPreferredSize(new Dimension(bim.getWidth(), bim.getHeight()));
    }//End alternative constructor

    public void changeGridSize(int size){
        this.rows = size; // Number of rows that contain control points
        this.cols = size; // Number of columns that contain control points
        trueRow = rows+2; // Add 2 rows to create a grid with the right number of control points
        trueCol = cols+2; // Add 2 columns to create a grid with the right number of control points
        points = new Node[22][22]; // 2D array of control points
        drawNodes = true;

        // Initialize the 2D array of control points
        for (int i = 0; i < trueCol; i++) {
            for (int j = 0; j < trueRow; j++) {
                points[i][j] = new Node(i, j, trueCol, trueRow, bim.getWidth(), bim.getHeight());
            }
        }

        //Initialize the boundary for each control point
        //the Outside points don't need set because they never move
        for(int x = 1; x <= this.cols; x++){
            for(int y = 1; y <= this.rows; y++){
                int[] pointsX = {points[x-1][y-1].getImgX(), points[x][y-1].getImgX(), points[x+1][y].getImgX(), points[x+1][y+1].getImgX(), points[x][y+1].getImgX(), points[x-1][y].getImgX()};
                int[] pointsY = {points[x-1][y-1].getImgY(), points[x][y-1].getImgY(), points[x+1][y].getImgY(), points[x+1][y+1].getImgY(), points[x][y+1].getImgY(), points[x-1][y].getImgY()};
                points[x][y].resetBounds(pointsX, pointsY);
            }
        }
        repaint();
    }

    public BufferedImage copyImage(BufferedImage bim){
        ColorModel m = bim.getColorModel();
        boolean isAlphaPremultiplied = getColorModel().isAlphaPremultiplied();
        WritableRaster raster = bim.copyData(null);
        return new BufferedImage(m, raster, isAlphaPremultiplied, null);
    }

    // Function to reset the 2D array to its default values
    public void resetPicture() {
        for (int i = 0; i < trueCol; i++) {
            for (int j = 0; j < trueRow; j++) {
                points[i][j].resetNode();
            }
        }

        //reset the boundary for each node
        //the Outside points don't need set because they never move
        for(int x = 1; x <= this.cols; x++){
            for(int y = 1; y <= this.rows; y++){
                int[] pointsX = {points[x-1][y-1].getImgX(), points[x][y-1].getImgX(), points[x+1][y].getImgX(), points[x+1][y+1].getImgX(), points[x][y+1].getImgX(), points[x-1][y].getImgX()};
                int[] pointsY = {points[x-1][y-1].getImgY(), points[x][y-1].getImgY(), points[x+1][y].getImgY(), points[x+1][y+1].getImgY(), points[x][y+1].getImgY(), points[x-1][y].getImgY()};
                points[x][y].resetBounds(pointsX, pointsY);
            }
        }

        repaint(); // Redraw the array back to default
    }//End resetPicture()

    // Return the 2D array of control points
    public Node[][] getPoints() { return points; }
    // Get a specific point (for easy coordinate access)
    public Node getPoint(int x, int y) { return points[x][y]; }

    public BufferedImage getBim() { return bim; }
    public void setBim(BufferedImage bim) { this.bim = bim; }

    public void ignoreGrid() { drawNodes = false; }

    // Change the color of an individual node in the 2D array
    public void changeNodeColor(Node n, Color color) { n.setColor(color); }

    public BufferedImage changeBrightness(int changeVal) {

        if(changeVal == 100){return bim;}

        int pixel[] = {0,0,0,0};
        float hsbValues[] = {0.0f, 0.0f, 0.0f},  percent = changeVal/100;
        Color brightAdjust;

        if (bim == null)
            return null;

        for (int i = 0; i < bim.getWidth(); i++){
            for (int j = 0; j < bim.getHeight(); j++) {
                bim.getRaster().getPixel(i, j, pixel);
                Color.RGBtoHSB(pixel[0], pixel[1], pixel[2], hsbValues);

                float newBright = hsbValues[2] * percent;
                if(newBright > 1f){ newBright = 1f;}

                brightAdjust = new Color(Color.HSBtoRGB(hsbValues[0], hsbValues[1], newBright));
                int[] rgb = { brightAdjust.getRed(), brightAdjust.getGreen(), brightAdjust.getBlue(), pixel[3]};
                bim.getRaster().setPixel(i, j, rgb);
            }
        }
        repaint();
        return bim;
    }

    // Draw the connecting lines in between the control points
    public void drawLines(Graphics g) {
        int x, y, xR, yR, xD, yD, xDiag, yDiag;

        for (int i = 0; i < trueCol-1; i++) {
            for (int j = 0; j < trueRow-1; j++) {
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

        if (drawNodes) {
            // Draw connecting lines
            drawLines(g);

            // Draw control points
            for (int i = 0; i < trueCol; i++) {
                for (int j = 0; j < trueRow; j++) {
                    g2d.setColor(points[i][j].getColor());
                    g2d.fillRect(points[i][j].getImgX(), points[i][j].getImgY(), points[i][j].getSize(), points[i][j].getSize());
                }
            }
        }
        if(activeNode != null){
            g2d.setColor(Color.GREEN);
            g2d.drawPolygon(activeNode.getBoundaryPoly());
        }
    }//End paintComponent()

    // Identify if there is a click on a control point and activate it.
    public boolean clickInPoint(Point click){
        for(int i =0; i < trueCol; i++){
            for(int j=0; j< trueRow; j++){
                if(points[i][j].contained(click) && i != 0 && i != trueCol-1 && j != 0 && j != trueRow-1){
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
            //reset the boundaries around the moved point
            for(int x = activeNode.getX()-1; x <= activeNode.getX()+1; x++) {
                for (int y = activeNode.getY()-1; y <= activeNode.getY()+1; y++) {
                    if(x > 0 && x <= cols && y > 0 && y <= rows) {
                        int[] pointsX = {points[x-1][y-1].getImgX(), points[x][y-1].getImgX(), points[x+1][y].getImgX(), points[x+1][y+1].getImgX(), points[x][y+1].getImgX(), points[x-1][y].getImgX()};
                        int[] pointsY = {points[x-1][y-1].getImgY(), points[x][y-1].getImgY(), points[x+1][y].getImgY(), points[x+1][y+1].getImgY(), points[x][y+1].getImgY(), points[x-1][y].getImgY()};
                        points[x][y].resetBounds(pointsX, pointsY);
                    }
                }
            }

            //clear the activeNode
            changeNodeColor(activeNode, Color.BLACK);
            activeNode = null;
        }
    }

    //move the activeNode to new location (within boundary only)
    public void movePoint(int posX, int posY){
        //if the dragging point is within the node's boundaryPoly, then repaint, otherwise, don't repaint
        if(activeNode.withinBounds(posX, posY)) {
            activeNode.setImgX(posX);
            activeNode.setImgY(posY);
            repaint();
        }
    }//End movePoint

}//End class
