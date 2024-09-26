import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;

public class Draw202 extends JPanel {
    static int smooth = 10000;

    /* Unless the laser is parallel to a line (wall), it will eventually hit it.
     * This method determines the closer intersection, which will be on the
     * wall the laser should bounce off. */
    static boolean closer(double m1, double mL, double x, double y) {
        double x1 = (((m1 * 500) - (mL * x) + y - 700) / (m1 - mL));

        if (m1 < 0) {
            if (500 < x1 && x1 < 800) {
                return true;
            } else {
                return false;
            }
        } else {
            if (200 < x1 && x1 < 500) {
                return true;
            } else {
                return false;
            }
        }
    }
    

    public static void main(String[] args) {
        
        JFrame frame = new JFrame();
        Container pane = frame.getContentPane();
        pane.setLayout(new BorderLayout());

        // slider to control horizontal rotation
        JSlider angleSlider = new JSlider(60 * smooth, 120 * smooth, 60 * smooth);
        pane.add(angleSlider, BorderLayout.SOUTH);
        JSlider depthSlider = new JSlider(SwingConstants.VERTICAL, 1, 1000, 5);
        pane.add(depthSlider, BorderLayout.EAST);

        JPanel renderPanel = new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                double theta = Math.toRadians((double) angleSlider.getValue() / smooth);
                int depth = depthSlider.getValue();

                Graphics2D g2 = (Graphics2D) g;
                g2.translate(-100, -100);

                g2.setStroke(new BasicStroke(3));

                super.paintComponent(g);
                g2.setColor(Color.BLUE);

                // triangle
                g2.drawLine(200, 700 - (int) (300 * Math.sqrt(3)), 800, //wall 1
                        700 - (int) (300 * Math.sqrt(3)));
                g2.drawLine(500, 700, 800, 700 - (int) (300 * Math.sqrt(3))); //wall 2
                g2.drawLine(500, 700, 200, 700 - (int) (300 * Math.sqrt(3))); //wall 3

                //laser
                g2.setColor(Color.RED);
                g2.setStroke(new BasicStroke(2));

                double x = 500; // starts through bottom point
                double y = 700;
                int lastBounce = 1; // hits wall 1 first

                // slopes
                double m1 = 0; // of 1
                double m2 = (700 - (700 - (300 * Math.sqrt(3)))) / (500 - 800); // of 2
                double m3 = (700 - (700 - (300 * Math.sqrt(3)))) / (500 - 200); // of 3

                double mL = -Math.tan(theta); // initial of the laser

                // initial intersection pt
                double newX = (((m1 * 200) - (mL * x) + y - (700 - (300 * Math.sqrt(3))))
                        / (m1 - mL));
                double newY = mL * (newX - x) + y;
                // System.out.println(angleSlider.getValue() / smooth);

                if (angleSlider.getValue() / smooth == 90) {
                    g2.drawLine(500, 700, 500, (int) (700 - (300 * Math.sqrt(3))));
                } else if (angleSlider.getValue() / smooth == 60) {
                    g2.drawLine(500, 700, 800, (int) (700 - (300 * Math.sqrt(3))));
                } else if (angleSlider.getValue() / smooth == 120) {
                    g2.drawLine(500, 700, 200, (int) (700 - (300 * Math.sqrt(3))));
                }

                else {
                    for (int b = 0; b < depth; b++) {
                        // b = how many bounces it will draw
                        g2.drawLine((int) x, (int) y, (int) newX, (int) newY);
                        x = newX;
                        y = newY;

                        // Find wall laser intersects 
                        if (lastBounce == 1) {
                            mL = -mL;
                            if (closer(m2, mL, x, y)) {
                                lastBounce = 2;
                            } else {
                                lastBounce = 3;
                            }

                        } else if (lastBounce == 2) {
                            mL = Math.tan((2 * Math.atan(m2)) - Math.atan(mL));
                            if (closer(m3, mL, x, y)) {
                                lastBounce = 3;
                            } else {
                                lastBounce = 1;
                            }
                        } else if (lastBounce == 3) {
                            mL = Math.tan((2 * Math.atan(m3)) - Math.atan(mL));
                            if (closer(m2, mL, x, y)) {
                                lastBounce = 2;
                            } else {
                                lastBounce = 1;
                            }
                        }

                        // find point on wall / update x and y to new point
                        if (lastBounce == 1) {
                            newX = (((m1 * 200) - (mL * x) + y
                                    - (700 - (300 * Math.sqrt(3)))) / (m1 - mL));
                            newY = mL * (newX - x) + y;

                        } else if (lastBounce == 2) {
                            newX = (((m2 * 500) - (mL * x) + y - 700) / (m2 - mL));
                            newY = mL * (newX - x) + y;

                        } else if (lastBounce == 3) {
                            newX = (((m3 * 500) - (mL * x) + y - 700) / (m3 - mL));
                            newY = mL * (newX - x) + y;
                        }
                    }
                }
            }
        };

        pane.add(renderPanel, BorderLayout.CENTER);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(850, 800);
        frame.setVisible(true);

        angleSlider.addChangeListener(e -> renderPanel.repaint());
        depthSlider.addChangeListener(e -> renderPanel.repaint());
    }
}
