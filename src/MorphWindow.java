import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class MorphWindow extends JFrame {
    private Picture start, end, morph;
    private int r, c, currFrame, framesPerSecond, seconds;
    private float completedFrames;
    private Timer animate;
    private JMenuItem status; //because it's needed inside an event listener

    public MorphWindow(PopupSettings settings, BufferedImage startImage, Node[][] initPoints, BufferedImage endImage, Node[][] finPoints, int size) {
        super("Morph");
        r = size+2;
        c = size+2;
        System.out.println("Size = " + size);
        // Initialize a new array to store a copy of the original array.
        Node[][] startPoints = new Node[22][22];
        Node[][] endPoints = new Node[22][22];

        // Populate the copy of the array with new Node objects native to the PreviewWindow class only
        for (int i = 0; i < c; i++) {
            for (int j = 0; j < r; j++) {
                Node nd = new Node(initPoints[i][j].getX(), initPoints[i][j].getY(), c, r, startImage.getWidth(), startImage.getHeight());
                nd.setImgX(initPoints[i][j].getImgX());
                nd.setImgY(initPoints[i][j].getImgY());
                startPoints[i][j] = nd;

                Node nd2 = new Node(finPoints[i][j].getX(), finPoints[i][j].getY(), c, r, endImage.getWidth(), endImage.getHeight());
                nd2.setImgX(finPoints[i][j].getImgX());
                nd2.setImgY(finPoints[i][j].getImgY());
                endPoints[i][j] = nd2;
            }
        }

        start = new Picture(startImage, startPoints, size);
        end = new Picture(endImage, endPoints,size);

        morph = new Picture(startImage, startPoints, size);
        morph.ignoreGrid();
        morph.repaint();

        addMenus(settings);
        framesPerSecond = settings.getTweenImageValue(); // Retrieve the values from the settings window
        seconds = settings.getSeconds();
        animate = new Timer(seconds * 10, new ActionListener() { // Timer to allow the animation to be visible
            public void actionPerformed(ActionEvent e) {
                animation();
                if (currFrame > (seconds * framesPerSecond)) { // If we are creating more frames than we need, the animation must stop.
                    animate.stop();
                    currFrame = 1; // Reset the number of frames for future use
                    status.setText("Status: Finished");
                }
            }
        });
        //animate.start();
        //add the Picture panel to the JFrame
        add(morph);
        setSize(650, 675);
        setVisible(true);
    }

    private void addMenus(PopupSettings settings) {
        //Initialize the menuBar

        //Export will produce a savable video (Disabled until Morph Part2)
        JMenuItem FileExport = new JMenuItem("Export");
        FileExport.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                System.out.println("Export to video file");
            }
        });
        FileExport.setEnabled(false);

        //The settings will transfer between the Morph JFrame and the PreviewWindow JFrame
        JMenuItem FileSettings = new JMenuItem("Settings");
        FileSettings.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                settings.setVisible(true);
                settings.addWindowListener(new WindowAdapter() {
                    public void windowClosing(WindowEvent windowEvent) {
                        //call member functions of settings page here
                        framesPerSecond = settings.getTweenImageValue();
                        seconds = settings.getSeconds();
                    }
                });
            }
        });
        FileSettings.setEnabled(false); //Disabled control


        //close the PreviewWindow JFrame
        JMenuItem FileExit = new JMenuItem("Exit");
        FileExit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                MorphWindow.this.dispatchEvent(new WindowEvent(
                        MorphWindow.this, WindowEvent.WINDOW_CLOSING
                ));
            }
        });

        //Initialize the File menu and add it's child controls
        JMenu File = new JMenu("File");
        File.add(FileExport);
        File.add(FileSettings);
        File.add(FileExit);

        //Initialize the buttons on the menubar (no submenus)
        JMenuItem startButton = new JMenuItem("Start Animation");
        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                animate.start();
                status.setText("Status: Running");
            }
        });

        JMenuItem stopButton = new JMenuItem("Stop Animation");
        stopButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                animate.stop();
                status.setText("Status: Stopped");
            }
        });

        status = new JMenuItem("Status: Not Running");

        //Initialize the menubar and add it to the JFrame
        JMenuBar menuBar = new JMenuBar();
        this.setJMenuBar(menuBar);
        menuBar.add(File);
        menuBar.add(startButton);
        menuBar.add(stopButton);
        menuBar.add(status);
    }//end addMenus()

    // Animation: Will use linear transformation to compute the amount of pixels a control point needs to move to go from
    // the starting image to the position on the ending image.
    public void animation() {
        completedFrames = (float) (currFrame) / (framesPerSecond * seconds);
        float x, y;
        int x1, x2, y1, y2, tweenImageR, tweenImageG, tweenImageB, tweenImageAlp;
        Color currColor, endColor, tweenColor;
        for (int i = 0; i < c; i++) {
            for (int j = 0; j < r; j++) {
                x1 = morph.getPoints()[i][j].getImgX(); // Get the x coordinate of the pixel the point is in (starting image)
                y1 = morph.getPoints()[i][j].getImgY(); // Get the y coordinate of the pixel the point is in (starting image)
                x2 = end.getPoints()[i][j].getImgX(); // Get the y coordinate of the pixel the point is in (ending image)
                y2 = end.getPoints()[i][j].getImgY(); // Get the y coordinate of the pixel the point is in (ending image)
                // Compute the difference of the points depending on the number of frames rendered so far (both for x and y)
                x = ((x2 - x1) * completedFrames) + x1;
                y = ((y2 - y1) * completedFrames) + y1;

                // Change the coordinates of x and y of the start panel
                morph.getPoints()[i][j].setImgX((int) x);
                morph.getPoints()[i][j].setImgY((int) y);
            }
        }

        //BufferedImage startingTriangles = setTriangles(start, new Picture(morph.getBim(), morph.getPoints(), r-2));
        setTriangles(start, morph);//new Picture(morph.getBim(), morph.getPoints(), r-2));
        //BufferedImage endingTriangles =
        //setTriangles(end, morph);//new Picture(morph.getBim(), morph.getPoints(),r-2));

        // setRGB method: takes in x an y values to access the pixels, make sure to touch all pixels, taking the
        // difference of source and destination images and applying it to the tween. Pass int from setRGB into
        // color constructor to know what to do with the integer from getRGB
        BufferedImage frame = new BufferedImage(morph.getBim().getWidth(), morph.getBim().getHeight(), BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < morph.getBim().getWidth(); i++){//startingTriangles.getWidth(); i++) {
            for (int j = 0; j < morph.getBim().getHeight(); j++) {//startingTriangles.getHeight(); j++) {
                currColor = new Color(morph.getBim().getRGB(i, j));//startingTriangles.getRGB(i, j));
                endColor = new Color(end.getBim().getRGB(i,j));//endingTriangles.getRGB(i, j));
                // cross dissolve formula
                tweenImageR = (int) (currColor.getRed() + completedFrames * (endColor.getRed() - currColor.getRed()));
                tweenImageG = (int) (currColor.getGreen() + completedFrames * (endColor.getGreen() - currColor.getGreen()));
                tweenImageB = (int) (currColor.getBlue() + completedFrames * (endColor.getBlue() - currColor.getBlue()));
                tweenImageAlp = (int) (currColor.getAlpha() + completedFrames * (endColor.getAlpha() - currColor.getAlpha()));

                tweenColor = new Color(tweenImageR, tweenImageG, tweenImageB, tweenImageAlp);
                frame.setRGB(i, j, tweenColor.getRGB());
            }
        }
        morph.setBim(frame);

        File outputFile = new File("MOJ" + currFrame + ".jpg");
        try {
            ImageIO.write(frame, "jpg", outputFile);
        } catch (IOException e) {
            System.out.println("Error Saving Morph Frame");
        }
        currFrame++; // Increase the number of frames rendered
        morph.ignoreGrid();
        morph.repaint(); //repaint each frame
    } //End animation()

    //public BufferedImage
    public void setTriangles(Picture first, Picture last) {
        double sx1, sx2, sx3, sx4, dx1, dx2, dx3, dx4;
        double sy1, sy2, sy3, sy4, dy1, dy2, dy3, dy4;
        for (int i = 0; i < c - 1; i++) {
            for (int j = 0; j < r - 1; j++) {
                if (i + 1 <= c || j + 1 <= r) {
                    // Source triangle (S) points
                    sx1 = first.getPoint(i, j).getImgX();
                    sy1 = first.getPoint(i, j).getImgY();
                    sx2 = first.getPoint(i + 1, j).getImgX();
                    sy2 = first.getPoint(i + 1, j).getImgY();
                    sx3 = first.getPoint(i+1, j + 1).getImgX();
                    sy3 = first.getPoint(i+1, j + 1).getImgY();
                    sx4 = first.getPoint(i, j + 1).getImgX();
                    sy4 = first.getPoint(i, j + 1).getImgY();

                    // Destination triangle (D) points
                    dx1 = last.getPoint(i, j).getImgX();
                    dy1 = last.getPoint(i, j).getImgY();
                    dx2 = last.getPoint(i + 1, j).getImgY();
                    dy2 = last.getPoint(i + 1, j).getImgY();
                    dx3 = last.getPoint(i+1, j + 1).getImgX();
                    dy3 = last.getPoint(i+1, j + 1).getImgY();
                    dx4 = last.getPoint(i , j + 1).getImgX();
                    dy4 = last.getPoint(i, j + 1).getImgY();

                    Triangle S = new Triangle(sx1, sy1, sx2, sy2, sx3, sy3);
                    Triangle D = new Triangle(dx1, dy1, dx2, dy2, dx3, dy3);
                    warpTriangle(first.getBim(), last.getBim(), S, D, null, null);

                    S = new Triangle(sx1, sy1, sx4, sy4, sx3, sy3);
                    D = new Triangle(dx1, dy1, dx4, dy4, dx3, dy3);
                    warpTriangle(first.getBim(), last.getBim(), S, D, null, null);
                }
            }
        }
        //return last.getBim();
    }//End setTriangles()

    public static void warpTriangle(
            BufferedImage src,
            BufferedImage dest,
            Triangle S,
            Triangle D,
            Object ALIASING,
            Object INTERPOLATION) {

        /*****************************************************
         solve Xi = sx*xi + shx*yi + tx    for i = 1,2,3 where xi is a point on
         the source triangle and Xi = the corresponding point on the
         destination  triangle. Do the same thing for Yi = shy*y + sy*x + ty.
         shx is the shearing of x and sx is the scaling of x and tx is the
         translation of x needed to map one triangle to the other.
         r
         Gaussian Elimination with scaled partial pivoting is the method
         used solve the two systems of linear equations.
         ********************************************************/
        if (ALIASING == null)
            ALIASING = RenderingHints.VALUE_ANTIALIAS_ON;
        if (INTERPOLATION == null)
            INTERPOLATION = RenderingHints.VALUE_INTERPOLATION_BICUBIC;
        double[][] a = new double[3][3];
        for (int i = 0; i < 3; ++i) {
            a[i][0] = S.getX(i);

            System.out.println("P" + i + "(" + S.getX(i) + ", " + S.getY(i) + ")" );

            a[i][1] = S.getY(i);
            a[i][2] = 1.0;
        }

        int l[] = new int[3];
        Gauss(3, a, l);

        double[] b = new double[3];
        for (int i = 0; i < 3; ++i) {
            b[i] = D.getX(i);
        }

        double[] x = new double[3];
        solve(3, a, l, b, x);

        double[] by = new double[3];
        for (int i = 0; i < 3; ++i) {
            by[i] = D.getY(i);
        }

        double[] y = new double[3];
        solve(3, a, l, by, y);

        // System.out.println("Affine:\t" + x[0] + ", " + x[1] + ", " + x[2] );
        // System.out.println("\t" + y[0] + ", " + y[1] + ", " + y[2] );

        AffineTransform af =
                new AffineTransform(x[0], y[0], x[1], y[1], x[2], y[2]);
        GeneralPath destPath = new GeneralPath(GeneralPath.WIND_EVEN_ODD);

        destPath.moveTo((float) D.getX(0), (float) D.getY(0));
        destPath.lineTo((float) D.getX(1), (float) D.getY(1));
        destPath.lineTo((float) D.getX(2), (float) D.getY(2));
        destPath.lineTo((float) D.getX(0), (float) D.getY(0));
        Graphics2D g2 = dest.createGraphics();

        // set up an alpha value for compositing as an example
        AlphaComposite ac =
                AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) 0.5);
        g2.setComposite(ac);

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, ALIASING);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, INTERPOLATION);
        g2.clip(destPath);
        g2.setTransform(af);
        g2.drawImage(src, 0, 0, null);
        g2.dispose();
    } //End warpTriangle()

    private static void Gauss(int n, double[][] a, int[] l) {
        /****************************************************
         a is a n x n matrix and l is an int array of length n
         l is used as an index array that will determine the order of
         elimination of coefficients
         All array indexes are assumed to start at 0
         ******************************************************/
        double[] s = new double[n];  // scaling factor
        int i, j = 0, k;
        double r, rmax, smax, xmult;
        for (i = 0; i < n; ++i) {
            l[i] = i;
            smax = 0;
            for (j = 0; j < n; ++j)
                smax = Math.max(smax, Math.abs(a[i][j]));
            s[i] = smax;
        }

        i = n - 1;
        for (k = 0; k < (n - 1); ++k) {
            --j;
            rmax = 0;
            for (i = k; i < n; ++i) {
                r = Math.abs(a[l[i]][k] / s[l[i]]);
                if (r > rmax) {
                    rmax = r;
                    j = i;
                }
            }
            int temp = l[j];
            l[j] = l[k];
            l[k] = temp;
            for (i = k + 1; i < n; ++i) {
                xmult = a[l[i]][k] / a[l[k]][k];
                a[l[i]][k] = xmult;
                for (j = k + 1; j < n; ++j) {
                    a[l[i]][j] = a[l[i]][j] - xmult * a[l[k]][j];
                }
            }
        }
    } //End Gauss()

    private static void solve(int n, double[][] a, int[] l, double[] b, double[] x) {
        /*********************************************************
         a and l have previously been passed to Gauss() b is the product of
         a and x. x is the 1x3 matrix of coefficients to solve for
         *************************************************************/
        int i, k;
        double sum;
        for (k = 0; k < (n - 1); ++k) {
            for (i = k + 1; i < n; ++i) {
                b[l[i]] -= a[l[i]][k] * b[l[k]];
            }
        }
        x[n - 1] = b[l[n - 1]] / a[l[n - 1]][n - 1];

        for (i = n - 2; i >= 0; --i) {
            sum = b[l[i]];
            for (int j = i + 1; j < n; ++j) {
                sum = sum - a[l[i]][j] * x[j];
            }
            x[i] = sum / a[l[i]][i];
        }
    } //End solve()
}