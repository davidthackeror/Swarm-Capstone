
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Project: Swarm Capstone
 * : The DJI Drone Sub-Class (not actually a DJI drone)
 *
 * @author David Thacker
 * @author Manny Riolo
 * @author Jesse Sidhu
 * @author Josiah Goosen
 * @author Luke Ruan
 * @author Michael Shumate
 * @author Addison Brown
 * Date: 21 Jan 21
 * Class: Capstone
 */
class DJI extends Drone {

    DJI(int minHealth, int maxHealth, int minSpeed, int maxSpeed, int minCourage, int maxCourage, int size, int minAttack, int maxAttack, int range, String name){
        super();
        this.setxPos(1);
        this.setyPos(1);
        this.setzPos(1);
        this.setposAchieved(0);
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
     * @param zTarget The z coordinate of the target enemy soldier
     */
    @Override
    public void move(int xTarget, int yTarget, int zTarget) {
        //System.out.println(this.getName() + " targetZ " + (zTarget - this.getzPos()));
        //System.out.println(this.getName() + " nonunit: " + (xTarget- this.getxPos()) + " " + (yTarget - this.getyPos()) + " " + (zTarget - this.getzPos()));

        Vector330Class unitVector = new Vector330Class(xTarget- this.getxPos(), yTarget - this.getyPos(), zTarget - this.getzPos());
        Vector330Class unit = unitVector.normalize();
        //System.out.println(this.getName() + " unit: " + unit.getX() + " " + unit.getY() + " " + unit.getZ());
        double xShift = (unit.getX() * this.getSpeed());
        double yShift = (unit.getY() * this.getSpeed());
        double zShift = (unit.getZ() * this.getSpeed());
        this.setxPos((int) (xShift + this.getxPos()));
        this.setyPos((int) (yShift + this.getyPos()));
        this.setzPos((int) (zShift + this.getzPos()));

    }

    /**
     * draw() - puts the selected warrior on the screen with its assigned x, y, size, and color
     * @param g The graphics window to be written to
     */
    @Override
    public void draw(Graphics2D g) {
        {
            BufferedImage img = null;
//            try {
//                String opacityNumber;
//                if (this.getzPos() > 0 && this.getzPos() <= Main.CEILING / 20) {
//                    opacityNumber = "5";
//                } else if (this.getzPos() <= (Main.CEILING / 20) * 2) {
//                    opacityNumber = "10";
//                } else if (this.getzPos() <= (Main.CEILING / 20) * 3) {
//                    opacityNumber = "15";
//                } else if (this.getzPos() <= (Main.CEILING / 20) * 4) {
//                    opacityNumber = "20";
//                } else if (this.getzPos() <= (Main.CEILING / 20) * 5) {
//                    opacityNumber = "25";
//                } else if (this.getzPos() <= (Main.CEILING / 20) * 6) {
//                    opacityNumber = "30";
//                } else if (this.getzPos() <= (Main.CEILING / 20) * 7) {
//                    opacityNumber = "35";
//                } else if (this.getzPos() <= (Main.CEILING / 20) * 8) {
//                    opacityNumber = "40";
//                } else if (this.getzPos() <= (Main.CEILING / 20) * 9) {
//                    opacityNumber = "45";
//                } else if (this.getzPos() <= (Main.CEILING / 20) * 10) {
//                    opacityNumber = "50";
//                } else if (this.getzPos() <= (Main.CEILING / 20) * 11) {
//                    opacityNumber = "55";
//                } else if (this.getzPos() <= (Main.CEILING / 20) * 12) {
//                    opacityNumber = "60";
//                } else if (this.getzPos() <= (Main.CEILING / 20) * 13) {
//                    opacityNumber = "65";
//                } else if (this.getzPos() <= (Main.CEILING / 20) * 14) {
//                    opacityNumber = "70";
//                } else if (this.getzPos() <= (Main.CEILING / 20) * 15) {
//                    opacityNumber = "75";
//                } else if (this.getzPos() <= (Main.CEILING / 20) * 16) {
//                    opacityNumber = "80";
//                } else if (this.getzPos() <= (Main.CEILING / 20) * 17) {
//                    opacityNumber = "85";
//                } else if (this.getzPos() <= (Main.CEILING / 20) * 18) {
//                    opacityNumber = "90";
//                } else if (this.getzPos() <= (Main.CEILING / 20) * 19) {
//                    opacityNumber = "95";
//                } else if (this.getzPos() <= (Main.CEILING / 20) * 20) {
//                    opacityNumber = "100";
//                } else {
//                    opacityNumber = "100";
//                }
////                img = ImageIO.read(new File("drone.png"));
//            }
////            } catch (IOException ignored) {
////            }
            try {
                img = ImageIO.read(new File("Mini - MASSIVE/drone.png"));
            }catch (IOException ignored){}
            g.setColor(this.getColor());
            g.drawRect(this.getxPos()-this.getSize(), this.getyPos()-this.getSize(), img.getWidth(), img.getHeight());
            g.drawImage(img, this.getxPos()-this.getSize(), this.getyPos()-this.getSize(), null);
            g.drawString(this.getName(), this.getxPos(), this.getyPos() - 15);
            g.drawString(String.valueOf(this.getzPos()), this.getxPos(), this.getyPos() - 30);

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
                    img = ImageIO.read(new File("Mini - MASSIVE/explosion.jpg")); //relative file path
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