
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Project: Mini - MASSIVE
 * : The Orc Soldier Sub-Class
 *
 * @author David Thacker
 * Date: 22 Sept 19
 * Class: CS330
 */
class DJI extends Drone {

    static String name= "DJI";



    DJI(int minHealth, int maxHealth, int minSpeed, int maxSpeed, int minCourage, int maxCourage, int size, int minAttack, int maxAttack){
        super();
        this.setxPos(1);
        this.setyPos(1);
        this.setHealth(DJIStats[0]);
        this.setAttack(Army.getRandomNumberInRange(DJIStats[1], DJIStats[2]));
        this.setSpeed(Army.getRandomNumberInRange(DJIStats[3], DJIStats[4]));
        this.setCourage(Army.getRandomNumberInRange(DJIStats[5], DJIStats[6]));
        this.setSize(DJIStats[7]);
        this.setRange(DJIStats[8]);
    }

    /**
     * getName()
     *
     * @return name of the warrior generated by this subclass
     */
    public String getName() { return DJI.name;
    }

    /**
     * move() - Moves the warrior in the direction of the target x and y in proportion to it's speed
     *
     * @param xTarget The x coordinate of the target enemy soldier
     * @param yTarget The y coordinate of the target enemy soldier
     */
    @Override
    public void move(int xTarget, int yTarget) {
        Vector330Class unitVector = new Vector330Class(xTarget- this.getxPos(), yTarget - this.getyPos());
        Vector330Class unit = unitVector.normalize();
        double xShift = (unit.getX() * this.getSpeed());
        double yShift = (unit.getY() * this.getSpeed());
        this.setxPos((int) (xShift + this.getxPos()));
        this.setyPos((int) (yShift+ this.getyPos()));

    }

    /**
     * draw() - puts the selected warrior on the screen with its assigned x, y, size, and color
     * @param g The graphics window to be written to
     */
    @Override
    public void draw(Graphics2D g) {
        {
            BufferedImage img = null;
            try {
                img = ImageIO.read(new File("drone.png"));
            } catch (IOException e) {
            }

            g.setColor(this.getColor());
            g.setColor(Color.magenta);
            g.drawImage(img, this.getxPos()-this.getSize(), this.getyPos()-this.getSize(), null);
            //g.drawImage(img, this.getxPos()-this.getSize(), this.getyPos()-this.getSize(), null);
            g.drawString(name, this.getxPos(), this.getyPos());

        }
    }
}