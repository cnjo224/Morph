/* Authors: Caitlin Jones, Chelina Ortiz M
 * Date: 11/16/18
 * Project: CS 335 Program 3 - Morph
 * References:
 * Notes:
 */

import java.awt.*;

public class Node {
    private int x, y, imgX, imgY, cols, rows, pictureWidth,
            xCoord[] = new int[4],
            yCoord[] = new int[4],
            size = 4;
    private Polygon node;

    public Node(int X, int Y, int PictureWidth, int Cols, int Rows){
        x = X; // Position in the array
        y = Y;
        pictureWidth = PictureWidth;
        cols = Cols;
        rows = Rows;

        imgX = x * Math.floorDiv(pictureWidth,cols-1); // Position in the pane/picture
        imgY = y * Math.floorDiv(pictureWidth,rows-1);

        xCoord[0] = imgX - size;
        xCoord[1] = imgX + size;
        xCoord[2] = imgX + size;
        xCoord[3] = imgX - size;

        yCoord[0] = imgY - size;
        yCoord[1] = imgY - size;
        yCoord[2] = imgY + size;
        yCoord[3] = imgY + size;

        node = new Polygon(xCoord, yCoord, size);
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

    public void drawNode(Graphics g) {
        g.setColor(Color.BLUE);
        g.drawPolygon(node);
        g.fillPolygon(node);
    }
}
