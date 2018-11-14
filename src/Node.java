import java.awt.*;

/* Authors: Caitlin Jones, Chelina Ortiz M
 * Date: 11/16/18
 * Project: CS 335 Program 3 - Morph
 * References:
 * Notes:
 */
public class Node {
    private int x, y, pictureWidth,
            xCoord[] = new int[4],
            yCoord[] = new int[4],
            size = 4;
    private Polygon node;

    public Node(int X, int Y) { //}, int PictureWidth){
        x = X;
        y = Y;
        //pictureWidth = PictureWidth;

        xCoord[0] = x - size;
        xCoord[1] = x + size;
        xCoord[2] = x + size;
        xCoord[3] = x - size;

        yCoord[0] = y - size;
        yCoord[1] = y - size;
        yCoord[2] = y + size;
        yCoord[3] = y + size;

        node = new Polygon(xCoord, yCoord, size);
    }

    public int getX(){return x;}
    public int getY(){return y;}

    public void setX(int X){x = X;}
    public void setY(int Y){y= Y;}

    public void drawNode(Graphics g) {
        g.setColor(Color.BLACK);
        g.drawPolygon(node);
        g.fillPolygon(node);
    }
}
