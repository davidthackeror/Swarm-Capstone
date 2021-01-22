import javax.swing.*;
import java.awt.*;

/**
 * Project: Swarm Capstone
 * : Creates and animates on the animationThread and JPanel
 *
 * @author David Thacker
 * Date: 21 Jan 21
 * Class: Capstone
 */
@SuppressWarnings("serial")
public class AnimationArea extends JPanel {

    private int height;  // dot's position
    private int width;

    // constructor for AnimationArea objects
    public AnimationArea() {
        super();    // constructor for JPanel
        this.height = 1000;
       this.width = 1000;
       this.setSize(this.width, this.height);
    }

    // animate() is called as separate thread
    //    via AnimationThread object
    public void animate() {

        this.setVisible(true);
        Battle.moveDrones();
        this.repaint();

    } // end animate() method

    // paint() is called automatically by the JVM
    public void paint(Graphics g) {
        g.clearRect(0,0, Main.SIZE, Main.SIZE);

        int rows = 20;
        int cols = 30;
        int width = getSize().width;
        int height = getSize().height;

        // draw the rows
        int rowHt = height / (rows);
        for (int i = 0; i < rows; i++) {
            g.setColor(Color.lightGray);
            g.drawLine(0, i * rowHt, width, i * rowHt);
        }
        // draw the columns
        int rowWid = width / (cols);
        for (int i = 0; i < cols; i++) {
            g.setColor(Color.lightGray);
            g.drawLine(i * rowWid, 0, i * rowWid, height);
        }
        // clear the background
        Battle.drawSwarm(g);

    } // end paint()

} // end AnimationArea class
