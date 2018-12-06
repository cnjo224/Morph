import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.image.BufferedImage;

public class MorphWindow extends JFrame {
    private Picture start, end, morph;
    private int r, c, currFrame, framesPerSecond, seconds;
    private Container C = getContentPane();
    private Timer animate;
    private JMenuItem status; //because it's needed inside an event listener

    public MorphWindow(PopupSettings settings, Picture start, Picture end) {
        super("Morph");
        this.start = start;
        this.end = end;
        r = start.getPoints().length;
        c = start.getPoints()[0].length;
        morph = new Picture(start.getPicture(), start.getPoints());
        morph.ignoreGrid();
        morph.repaint();

        addMenus(settings);
        framesPerSecond = settings.getTweenImageValue(); // Retrieve the values from the settings window
        seconds = settings.getSeconds();
        animate = new Timer(seconds*10, new ActionListener() { // Timer to allow the animation to be visible
            public void actionPerformed(ActionEvent e) {
                animation();
                if (currFrame > seconds*framesPerSecond) { // If we are creating more frames than we need, the animation must stop.
                    animate.stop();
                    currFrame = 1; // Reset the number of frames for future use
                    //status.setText("Status: Finished");
                }
            }
        });
        //animate.start();
        //add the Picture panel to the JFrame
        C.add(morph);
        setSize(650,700);
        setVisible(true);
    }

    private void addMenus(PopupSettings settings){
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
        int completed = currFrame / (framesPerSecond * seconds);
        int x, y, x1, x2, y1, y2;
        for (int i = 0; i < c; i++) {
            for (int j = 0; j < r; j++) {
                x1 = morph.getPoints()[i][j].getImgX(); // Get the x coordinate of the pixel the point is in (starting image)
                y1 = morph.getPoints()[i][j].getImgY(); // Get the y coordinate of the pixel the point is in (starting image)
                x2 = end.getPoints()[i][j].getImgX(); // Get the y coordinate of the pixel the point is in (ending image)
                y2 = end.getPoints()[i][j].getImgY(); // Get the y coordinate of the pixel the point is in (ending image)
                // Compute the difference of the points depending on the number of frames rendered so far (both for x and y)
                x = ((x2 - x1) * completed) + x1;
                y = ((y2 - y1) * completed) + y1;

                // Change the coordinates of x and y of the start panel
                morph.getPoints()[i][j].setImgX(x);
                morph.getPoints()[i][j].setImgY(y);
            }
        }
        setTiangles();
        // setRGB method: takes in x an y values to access the pixels, make sure to touch all pixels, taking the difference of source and destination images and applying it to the tween
        // Pass int from setRGB into color constructor to know what to do with the integer from getRGB

        for (int i = 0; i < morph.getPicture().getWidth(); i++) {
            for (int j = 0; j < morph.getPicture().getHeight(); j++) {
                int currPixel = morph.getPicture().getRGB(i, j);
                int endPixel = end.getPicture().getRGB(i, j);

                Color startImage = new Color(currPixel);
                Color endImage = new Color(endPixel);
                // cross dissolve formula
                int tweenImageR = startImage.getRed() + completed * (endImage.getRed() - startImage.getRed());
                int tweenImageG = startImage.getGreen() + completed * (endImage.getGreen() - startImage.getGreen());
                int tweenImageB = startImage.getBlue() + completed * (endImage.getBlue() - startImage.getBlue());
                int tweenImageAlp = startImage.getAlpha() + completed * (endImage.getAlpha() - startImage.getAlpha());
                Color tweenImage = new Color(tweenImageR, tweenImageG, tweenImageB, tweenImageAlp);
                morph.getPicture().setRGB(i, j, tweenImage.getRGB());
            } // TODO: check why the intensities are not blending and then do the morph
        }
        currFrame++; // Increase the number of frames rendered
        morph.ignoreGrid();
        morph.repaint(); //repaint each frame
    }//End animation()

    /* TODO:
        - Set morphing frames
        - Make reading file in/saving final file work
        - Grid sizes required (5X5, 10X10, 20X20)
        - Adjust image brightness
        - Constraint points in area and make them draggabble again
     */



    public void setTiangles() {
        int sx1, sx2, sx3, sx4, dx1, dx2, dx3, dx4;
        int sy1, sy2, sy3, sy4, dy1, dy2, dy3, dy4;
        for (int i = 0; i < c-1; i++) {
            for (int j = 0; j < r-1; j++) {
                if (i + 1 <= c || j + 1 <= r) {
                    // Source triangle (S) points
                    sx1 = start.getPoint(i, j).getImgX();
                    sy1 = start.getPoint(i, j).getImgY();
                    sx2 = start.getPoint(i + 1, j).getImgX();
                    sy2 = start.getPoint(i + 1, j).getImgY();
                    sx3 = start.getPoint(i, j + 1).getImgX();
                    sy3 = start.getPoint(i, j + 1).getImgY();
                    sx4 = start.getPoint(i + 1, j + 1).getImgX();
                    sy4 = start.getPoint(i + 1, j + 1).getImgY();

                    // Destination triangle (D) points
                    dx1 = morph.getPoint(i, j).getImgX();
                    dy1 = morph.getPoint(i, j).getImgY();
                    dx2 = morph.getPoint(i + 1, j).getImgX();
                    dy2 = morph.getPoint(i + 1, j).getImgY();
                    dx3 = morph.getPoint(i, j + 1).getImgX();
                    dy3 = morph.getPoint(i, j + 1).getImgY();
                    dx4 = morph.getPoint(i + 1, j + 1).getImgX();
                    dy4 = morph.getPoint(i + 1, j + 1).getImgY();

                    Triangle S = new Triangle(sx1, sy1, sx2, sy2, sx4, sy4);
                    Triangle D = new Triangle(dx1, dy1, dx2, dy2, dx4, dy4);
                    warpTriangle(morph.getPicture(), end.getPicture(), S, D, null, null);

                    S = new Triangle(sx1, sy1, sx3, sy3, sx4, sy4);
                    D = new Triangle(dx1, dy1, dx3, dy3, dx4, dy4);
                    warpTriangle(morph.getPicture(), end.getPicture(), S, D, null, null);
                }
            }
        }
    }

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
    public static void warpTriangle(BufferedImage src, BufferedImage dest, Triangle S,
                                    Triangle D, Object ALIASING, Object INTERPOLATION) {

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

        destPath.moveTo((float)D.getX(0), (float)D.getY(0));
        destPath.lineTo((float)D.getX(1), (float)D.getY(1));
        destPath.lineTo((float)D.getX(2), (float)D.getY(2));
        destPath.lineTo((float)D.getX(0), (float)D.getY(0));
        Graphics2D g2 = dest.createGraphics();

        // set up an alpha value for compositing as an example
        AlphaComposite ac =
                AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float)0.5);
        g2.setComposite(ac);

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, ALIASING);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, INTERPOLATION);
        g2.clip(destPath);
        g2.setTransform(af);
        g2.drawImage(src, 0, 0, null);
        g2.dispose();
    }

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
    }

    private static void solve(
            int n, double[][] a, int[] l, double[] b, double[] x) {
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
    }
}
