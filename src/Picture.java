
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
                    points[i][j] = new Node(i, j, 600, trueCol, trueRow);
                //}
            }
        }
    }

    public void drawLines(Graphics g) {
        int x, y, xR = 0, yR = 0, xD = 0, yD = 0, xDiag = 0, yDiag = 0;
        // Declare triangles within this lines
        for (int i = 0; i < trueCol; i++) {
            for (int j = 0; j < trueRow; j++) {
                x = points[i][j].getImgX();
                y = points[i][j].getImgY();

                if (i < cols+1) { // lines to the right
                    xR = points[i+1][j].getImgX();
                    yR = points[i+1][j].getImgY();
                    g.drawLine(x, y, xR, yR);
                }
                if (j < rows+1) { // lines going down
                    xD = points[i][j+1].getImgX();
                    yD = points[i][j+1].getImgY();
                    g.drawLine(x, y, xD, yD);
                }
                if (i < cols+1 && j < rows+1) { // Diagonals
                    xDiag = points[i+1][j+1].getImgX();
                    yDiag = points[i+1][j+1].getImgY();
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
                    points[i][j].drawNode(g);
                //}
            }
        }
        drawLines(g);
    }

}

// TODO: Method to do triangle objects

// Starter code
    /*public static void warpTriangle (BufferedImage src, BufferedImage dest, Triangle S, Triangle D,
                                     Object ALIASING, Object INTERPOLATION) {
        if (ALIASING == null)
            ALIASING = RenderingHints.VALUE_ANTIALIAS_ON;
        if (INTERPOLATION == null)
            INTERPOLATION = RenderingHints.VALUE_INTERPOLATION_BICUBIC;
        double[][] a = new double[3][3];
        for (int i = 0; i < 3; i++) {
            a[i][0] = S.getX(i);
            a[i][1] = S.getY(i);
            a[i][2] = 1.0;
        }
        int l[] = new int[3];
        Gauss(3,a,l);

        double[] b = new double[3];
        for(int i = 0; i < 3; i++) {
            b[i] = D.getX(i);
        }

        double[] x = new double[3];
        solve(3, a, l, b, x);

        double[] by = new double[3];
        for (int i = 0; i  < 3; i++) {
            by[i] = D.getY(i);
        }

        double[] y = new double[3];
        solve(3, a, l, by, y);

        AffineTransform af = new AffineTransform(x[0], y[0], x[1], y[1], x[2], y[2]);

        GeneralPath destPath = new GeneralPath(GeneralPath.WIND_EVEN_ODD);

        destPath.moveTo((float)D.getX(0), (float)D.getY(0));
        destPath.lineTo((float)D.getX(1), (float)D.getY(1));
        destPath.lineTo((float)D.getX(2), (float)D.getY(2));
        destPath.lineTo((float)D.getX(0), (float)D.getY(0));
        Graphics2D g2 = dest.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, ALIASING);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,INTERPOLATION);
        g2.clip(destPath);
        g2.setTransform(af);
        g2.drawImage(src, 0, 0, null);
        g2.dispose();
    }
    */