/* Authors: Caitlin Jones, Chelina Ortiz M
 * Date: 11/16/18
 * Project: CS 335 Program 3 - JMorph
 * References: Previous projects from this semester (Rubber-banding, image processing, grid and node creation, etc.)
 * Notes: This class initializes the nodes or control points that we will use to control the morphing from one image
 *        to the other.
 */

import java.awt.*;

public class Node extends Polygon {
    private int x, y, imgX, imgY, cols, rows;
    private final int pictureWidth = 600, size = 5;
    private Color color;
    private Polygon boundaryPoly;

    // Constructor: initializes the node based on its position in a 2D array and calculates its pixel position in a panel.
    public Node(int X, int Y, int Cols, int Rows){
        x = X; // X Position in the array (row)
        y = Y; // Y Position in the array (column)
        cols = Cols; // Columns in the 2D array
        rows = Rows; // Rows in the 2D array
        color = Color.BLACK; // Default node color

        imgX = x * Math.floorDiv(pictureWidth,cols-1); // X Position of the pixel in the pane/picture
        imgY = y * Math.floorDiv(pictureWidth,rows-1); // Y Position of the pixel in the pane/picture

        xpoints = new int[]{imgX - size, imgX + size, imgX + size, imgX - size}; // Points of the polygon object
        ypoints = new int[]{imgY - size, imgY - size, imgY + size, imgY + size};

        npoints = 4; // Number of sides of the polygon

    }//End constructor

    // Return the x and y coordinates of the point in the 2D array
    public int getX(){return x;}
    public int getY(){return y;}

    // Return the x and y coordinates of the pixels in the picture
    public int getImgX(){ return imgX; }
    public int getImgY() { return imgY; }

    // Set the x and y coordinates of the pixels in the picture
    public void setImgX(int xLocation){
        imgX = xLocation;
        xpoints = new int[]{imgX - size, imgX + size, imgX + size, imgX - size};
    }
    public void setImgY(int yLocation){
        imgY = yLocation;
        ypoints = new int[]{imgY - size, imgY - size, imgY + size, imgY + size};
    }

    public boolean contained(Point click) {
        if(click.x >= xpoints[0] && click.x <= xpoints[1] && click.y >= ypoints[0] && click.y <= ypoints[2]){
            return true;
        }
        return false;
    }

    // Set and get the color of the node
    public Color getColor() { return color; }
    public void setColor(Color color) { this.color = color; }

    // Resets node to default position in the image
    public void resetNode() {
        imgX = x * Math.floorDiv(pictureWidth,cols-1);
        imgY = y * Math.floorDiv(pictureWidth,rows-1);

        xpoints = new int[]{imgX - size, imgX + size, imgX + size, imgX - size};
        ypoints = new int[]{imgY - size, imgY - size, imgY + size, imgY + size};
    }//End resetNode()

    public int getSize(){return size;}

    public void resetBounds(int[] pointsX, int[] pointsY){
        boundaryPoly = new Polygon(pointsX, pointsY, 6);
    }

    public boolean withinBounds(int posX, int posY){
        if(boundaryPoly.contains(posX, posY)){
            return true;
        }
        return false;
    }

    public Polygon getBoundaryPoly() {
        return boundaryPoly;
    }
}//End class
