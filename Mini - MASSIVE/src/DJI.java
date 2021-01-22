
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



    DJI(int minHealth, int maxHealth, int minSpeed, int maxSpeed, int minCourage, int maxCourage, int size, int minAttack, int maxAttack, int range, String name){
        super();
        this.setxPos(1);
        this.setyPos(1);
        this.setHealth(DJIStats[0]);
        this.setAttack(Army.getRandomNumberInRange(DJIStats[1], DJIStats[2]));
        this.setSpeed(Army.getRandomNumberInRange(DJIStats[3], DJIStats[4]));
        this.setCourage(Army.getRandomNumberInRange(DJIStats[5], DJIStats[6]));
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
            } catch (IOException e) {
            }

            g.setColor(this.getColor());
            g.drawRect(this.getxPos()-this.getSize(), this.getyPos()-this.getSize(), img.getWidth(), img.getHeight());
            g.drawImage(img, this.getxPos()-this.getSize(), this.getyPos()-this.getSize(), null);
            g.drawString(this.getName(), this.getxPos(), this.getyPos() - 15);

        }
    }

    @Override
    public void drawFire(Graphics2D g){
        if(this.isFiring()){
            g.drawLine(this.getxPos(), this.getyPos(), this.getFireX(), this.getFireY());
            this.setFiring(false);
        }

    }

    @Override
    public void drawExplosion(Graphics2D g) {
        {
            if(this.getHealth() <= 0 && this.getHealth() >= -10){
                BufferedImage img = null;
                try {
                    img = ImageIO.read(new File("explosion.png"));
                } catch (IOException e) {
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