import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Picture extends JPanel {
    private int rows, cols;
    private BufferedImage bim;
    private Node points[][];

    public Picture(BufferedImage bim, int rows, int cols) {
        this.bim = bim;
        this.rows = rows;
        this.cols = cols;
        points = new Node[rows][cols];

        for (int i = 0; i < cols; i++) {
            for (int j = 0; j < rows; j++) {
                if (i != 0 || i != cols || j != 0 || j != rows) {
                    points[i][j] = new Node(i, j);
                    points[i][j].setX(i);
                    points[i][j].setY(j);
                }
            }
        }
    }

    public void drawLines(Graphics g) {
        int x, y;
        for (int i = 0; i < cols; i++) {
            for (int j = 0; j < rows; j++) {
                x = points[i][j].getX();
                y = points[i][j].getY();

                if (i < cols-1) { // lines to the right
                    g.drawLine(x, y, x+1, y);
                }
                if (j < rows-1) { // lines going down
                    g.drawLine(x, y, x, y+1);
                }
                if (i != 0 && i < cols-1 && j != 0 && j < rows-1) { // Diagonals
                    g.drawLine(x, y, x+1, y+1);
                }
            }
        }
    }

    public void paintComponent(Graphics g) {
        g.setColor(Color.BLACK);
        Graphics2D g2d = (Graphics2D)g;

        g2d.drawImage(bim, 0, 0, this);
        for (int i = 0; i < cols; i++) {
            for (int j = 0; j < rows; j++) {
                points[i][j].drawNode(g);
            }
        }
        drawLines(g);
    }

}
