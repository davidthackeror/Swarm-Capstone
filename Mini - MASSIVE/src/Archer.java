import java.awt.*;

/**
 * Project: Mini - MASSIVE
 * : The Archer Soldier Sub-Class
 *
 * @author David Thacker
 * Date: 22 Sept 19
 * Class: CS330
 */
class Archer extends Warrior{

    private static String name = "Archer";

    Archer() {
        super();
        this.setxPos(1);
        this.setyPos(1);
        this.setHealth(archerStats[0]);
        this.setAttack(Army.getRandomNumberInRange(archerStats[1], archerStats[2]));
        this.setSpeed(Army.getRandomNumberInRange(archerStats[3], archerStats[4]));
        this.setCourage(Army.getRandomNumberInRange(archerStats[5], archerStats[6]));
        this.setSize(archerStats[7]);
        this.setRange(archerStats[8]);
    }

    /**
     * getName()
     *
     * @return name of the warrior generated by this subclass
     */
    public String getName() { return Archer.name;
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

            g.setColor(this.getColor());
            g.fillOval(this.getxPos()-this.getSize(), this.getyPos()-this.getSize(),
                    this.getSize()*2, this.getSize()*2);
            g.setColor(Color.magenta);

            g.drawString("Archer", this.getxPos(), this.getyPos());

        }
    }
}