
// TODO: Account for images being bigger/smaller pictures (size in pixels)
// TODO: Make the points draggable
// TODO: Morph class and algorithms

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.image.BufferedImage;

public class Picture extends JPanel {
    private int rows, cols, trueRow, trueCol;
    private BufferedImage bim;
    private Node points[][];
    private Node activeNode;

    public Picture(BufferedImage bim, int rows, int cols) {
        this.bim = bim;
        this.rows = rows;
        this.cols = cols;
        trueRow = rows+2;
        trueCol = cols+2;
        points = new Node[trueRow][trueCol];
        setPreferredSize(new Dimension(600, 600));

        for (int i = 0; i < trueCol; i++) {
            for (int j = 0; j < trueRow; j++) {
                //if (i > 0 || i <= cols || j > 0 || j <= rows) {
                    points[i][j] = new Node(i, j, trueCol, trueRow);
                //}
            }
        }
    }

    public void drawLines(Graphics g) {
        int x, y, xR = 0, yR = 0, xD = 0, yD = 0, xDiag = 0, yDiag = 0;
        // Declare triangles within this lines
        for (int i = 0; i < trueCol; i++) {
            for (int j = 0; j < trueRow; j++) {
                x = points[i][j].getImgX()+2;
                y = points[i][j].getImgY()+2;

                if (i < cols+1) { // lines to the right
                    xR = points[i+1][j].getImgX()+2;
                    yR = points[i+1][j].getImgY()+2;
                    g.drawLine(x, y, xR, yR);
                }
                if (j < rows+1) { // lines going down
                    xD = points[i][j+1].getImgX()+2;
                    yD = points[i][j+1].getImgY()+2;
                    g.drawLine(x, y, xD, yD);
                }
                if (i < cols+1 && j < rows+1) { // Diagonals
                    xDiag = points[i+1][j+1].getImgX()+2;
                    yDiag = points[i+1][j+1].getImgY()+2;
                    g.drawLine(x, y, xDiag, yDiag);
                }
            }
        }
    }

    public void paintComponent(Graphics g) {
        g.setColor(Color.BLUE);
        Graphics2D g2d = (Graphics2D)g;

        g2d.drawImage(bim, 0, 0, this);
        for (int i = 0; i < trueCol; i++) {
            for (int j = 0; j < trueRow; j++) {
                //if (i > 0 || i <= cols+1 || j > 0 || j <= rows+1) {
                //}
                //g2d.drawPolygon(points[i][j]);
                g2d.fillRect(points[i][j].getImgX(),points[i][j].getImgY(), 5,5);
            }
        }
        drawLines(g);
    }

    /************************************/
    //RUBBER BANDING FUNCTIONS
    /************************************/

    public boolean clickInPoly2(Point click){
        for(int i =0; i < points.length; i++){
            for(int j=0; j< points.length; j++){
                if(points[i][j].contains(click)){
                    activeNode = points[i][j];
                    System.out.println("Click in poly true" + i +"-"+ j);
                    return true;
                }
            }
        }System.out.println("Click in poly false");
        return false;
    }

    public Node getActiveNode(){return activeNode;}
    public void clearActiveNode(){activeNode = null;}



}
