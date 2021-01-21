import java.awt.*;
import java.util.Random;
/**
 * Project: Mini - MASSIVE
 * : General category and attributes of the soldier sub-classes
 * @author David Thacker
 * Date: 22 Sept 19
 * Class: CS330
 */

public abstract class Drone {
    private String name;
    /**
     * the random seed variable
     */
    Random rand = new Random();

    /**
     * the attributes of an archer (health, min and max attack, min and max speed, min and max courage, size, and range
     */
    static int[] DJIStats = new int[10];

    /**
     * how far a warrior can attack from
     */
    private int range;
    /**
     * is the warrior supposed to be moving
     */
    private boolean isMoving;
    /**
     * is the warrior alive
     */
    private boolean isAlive;
    /**
     * the warriors x position
     */
    private int xPos;
    /**
     * the warriors y position
     */
    private int yPos;
    /**
     * the warriors total health at any given point
     */
    private int health;
    /**
     * the warriors total attack
     */
    private int attack;
    /**
     * how unlikely they are to run away from a battle
     */
    private int courage;
    /**
     * the speed at which the warrior can move across the graphics plain
     */
    private int speed;
    /**
     * the size of the circle drawn to represent the warrior
     */
    private int size;
    /**
     * the color of the warriors alliance (individually assigned for creativity later)
     */
    private Color color;

    private int fireX;
    private int fireY;
    private boolean isFiring;

    private int[] minArray = new int[3];

    Drone() {
        this.xPos = 0;
        this.yPos = 0;
        this.health = 0;
        this.attack = 0;
        this.courage = 0;
        this.speed = 0;
        this.size = 0;
        this.isFiring = false;
        this.isMoving = true;
        this.isAlive = true;
    }

    public Drone(int xPos, int yPos, int health, int attack, int courage, int speed, int size, Color color, boolean isMoving, boolean isAlive, String name){
        this.xPos = xPos;
        this.yPos = yPos;
        this.health = health;
        this.attack = attack;
        this.courage = courage;
        this.speed = speed;
        this.size = size;
        this.color = color;
        this.isFiring = false;
        this.isMoving = isMoving;
        this.isAlive = isAlive;
        this.name = name;

    }

    //begin abstract methods


    /**
     * Moves the warrior in the direction of the target x and y in proportion to it's speed
     *
     * @param xTarget The x coordinate of the target enemy soldier
     * @param yTarget The y coordinate of the target enemy soldier
     */
    public abstract void move(int xTarget, int yTarget);

    /**
     * puts the selected warrior on the screen with its assigned x, y, size, and color
     *
     * @param g The graphics window to be written to
     */
    public abstract void draw(Graphics2D g);
    public abstract void drawFire(Graphics2D g);
    public abstract void drawExplosion(Graphics2D g);

    //begin non abstract methods (getter and setter methods)

    /**
     * gets the x position of an object
     *
     * @return the x position of the object
     */
    int getxPos() {
        return xPos;
    }

    /**
     * sets the x position of an object
     *
     * @param xPos the x position to be inserted into the object
     */
    void setxPos(int xPos) {
        this.xPos = xPos;
    }

    /**
     * gets the y position of an object
     * @return the y position of the object
     */
    int getyPos() {
        return yPos;
    }

    /**
     * sets the y position of an object
     *
     * @param yPos the y position to be inserted into the object
     */
    void setyPos(int yPos) {
        this.yPos = yPos;
    }

    /**
     * gets the health of the object
     * @return the health of the object
     */
    int getHealth() {
        return health;
    }

    /**
     * set the health of an object
     *
     * @param health the health parameter to be inserted into the object
     */
    void setHealth(int health) {
        this.health = health;
    }

    /**
     * gets the attack of the object
     * @return the attack of the object
     */
    int getAttack() {
        return attack;
    }

    /**
     * set the attack of an object
     * @param attack the attack parameter to be inserted into the object
     */
    void setAttack(int attack) {
        this.attack = attack;
    }

    /**
     * gets the courage of the object
     * @return the courage of the object
     */
    int getCourage() {
        return courage;
    }

    /**
     * set the courage of an object
     *
     * @param courage the courage parameter to be inserted into the object
     */
    void setCourage(int courage) {
        this.courage = courage;
    }

    /**
     * gets the speed of the object
     * @return the speed of the object
     */
    int getSpeed() {
        return speed;
    }

    /**
     * set the speed of an object
     * @param speed the speed parameter to be inserted into the object
     */
    void setSpeed(int speed) {
        this.speed = speed;
    }

    /**
     * gets the size of the object
     * @return the size of the object
     */
    int getSize() {
        return size;
    }

    /**
     * set the size of an object
     * @param size the size parameter to be inserted into the object
     */
    void setSize(int size) {
        this.size = size;
    }

    /**
     * gets the range of the object
     * @return the range of the object
     */
    int getRange() {
        return this.range;
    }

    /**
     * set the range of an object
     * @param range the range parameter to be inserted into the object
     */
    void setRange(int range) {
        this.range = range;
    }

    /**
     * set the color of an object
     * @param color the color parameter to be inserted into the object
     */
    void setColor(Color color) {
        this.color = color;
    }

    /**
     * gets the color the object
     * @return the color of the object
     */
    Color getColor() {
        return this.color;
    }

    /**
     * sees if the object is moving
     * @return the moving status of the object
     */
    boolean isMoving() {
        return isMoving;
    }

    /**
     * sets the objects moving status
     * @param moving the true or false of whether the object is moving or not
     */
    void setMoving(boolean moving) {
        isMoving = moving;
    }

    /**
     * sees if the object is alive
     * @return the life status of the object
     */
    boolean isAlive() {
        return isAlive;
    }

    /**
     * sees if the object is alive
     * @param alive the boolean of whether the object is alive or not
     */
    void setAlive(boolean alive) {
        isAlive = alive;
    }

    void setFireX(int x){ fireX = x;}
    int getFireX(){return fireX;}

    void setFireY(int y){ fireY = y;}
    int getFireY(){return fireY;}

    String getName() { return name; }
    void setName(String b){name = b;}

    public int[] getMinArray() {
        return minArray;
    }

    public void setMinArray(int[] minArray) {
        this.minArray = minArray;
    }

    public boolean isFiring() {
        return isFiring;
    }

    public void setFiring(boolean firing) {
        isFiring = firing;
    }
}