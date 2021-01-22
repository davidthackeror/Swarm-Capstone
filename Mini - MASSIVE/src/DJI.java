
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Project: Swarm Capstone
 * : The DJI Drone Sub-Class (fake)
 *
 * @author David Thacker
 * Date: 21 Jan 21
 * Class: Capstone
 */
class DJI extends Drone {



    DJI(int minHealth, int maxHealth, int minSpeed, int maxSpeed, int minCourage, int maxCourage, int size, int minAttack, int maxAttack, int range, String name){
        super();
        this.setxPos(1);
        this.setyPos(1);
        this.setHealth(DJIStats[0]);
        this.setAttack(Swarm.getRandomNumberInRange(DJIStats[1], DJIStats[2]));
        this.setSpeed(Swarm.getRandomNumberInRange(DJIStats[3], DJIStats[4]));
        this.setCourage(Swarm.getRandomNumberInRange(DJIStats[5], DJIStats[6]));
        this.setSize(DJIStats[7]);
        this.setRange(DJIStats[8]);
        this.setName(name);
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
            } catch (IOException ignored) {
            }

            g.setColor(this.getColor());
            g.drawRect(this.getxPos()-this.getSize(), this.getyPos()-this.getSize(), img.getWidth(), img.getHeight());
            g.drawImage(img, this.getxPos()-this.getSize(), this.getyPos()-this.getSize(), null);
            g.drawString(this.getName(), this.getxPos(), this.getyPos() - 15);

        }
    }

    /**
     * draws the laser coming from each drone
     * @param g the graphics window to be written to
     */
    @Override
    public void drawFire(Graphics2D g){
        if(this.isFiring()){
            g.drawLine(this.getxPos(), this.getyPos(), this.getFireX(), this.getFireY());
            this.setFiring(false);
        }

    }

    /**
     * draws the explosion of a drone once it dies, explosion lasts for 10 ticks
     * @param g the graphics window to be written to
     */
    @Override
    public void drawExplosion(Graphics2D g) {
        {
            //as long as drone is dead, and hasn't been dead for forever draw explosion
            if(this.getHealth() <= 0 && this.getHealth() >= -10){ //change the greaterthan or equal to change duration
                BufferedImage img = null;
                try {
                    img = ImageIO.read(new File("explosion.png")); //relative file path
                } catch (IOException ignored) {
                }

                g.setColor(this.getColor());
                g.drawRect(this.getxPos()-this.getSize(), this.getyPos()-this.getSize(), img.getWidth(), img.getHeight());
                g.drawImage(img, this.getxPos()-this.getSize(), this.getyPos()-this.getSize(), null);
                g.drawString(this.getName(), this.getxPos(), this.getyPos());
                this.setHealth(this.getHealth() - 1);
            }

        }
    }

}