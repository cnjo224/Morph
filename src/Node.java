/* Authors: Caitlin Jones, Chelina Ortiz M
 * Date: 11/16/18
 * Project: CS 335 Program 3 - Morph
 * References:
 * Notes:
 */

import java.awt.*;

public class Node extends Polygon {
    private int x, y, imgX, imgY, cols, rows;
    private final int pictureWidth = 600, size = 4;

    public Node(int X, int Y, int Cols, int Rows){
        x = X; // Position in the array
        y = Y;
        cols = Cols;
        rows = Rows;

        imgX = x * Math.floorDiv(pictureWidth,cols-1); // Position in the pane/picture
        imgY = y * Math.floorDiv(pictureWidth,rows-1);

        xpoints = new int[]{imgX - size, imgX + size, imgX + size, imgX - size};
        ypoints = new int[]{imgY - size, imgY - size, imgY + size, imgY + size};

    }

    public int getX(){return x;}
    public int getY(){return y;}

    public int getImgX(){ return imgX; }
    public int getImgY() { return imgY; }

    public void setX(int X){
        x = X;
        imgX = x * Math.floorDiv(pictureWidth,cols);
    }

    public void setY(int Y){
        y= Y;
        imgY = Y * Math.floorDiv(pictureWidth,rows);
    }
}
