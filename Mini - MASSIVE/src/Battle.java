import javax.sound.sampled.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.math.*;


/**
 * Project: Swarm Capstone
 * : Controls execution of the battle
 *
 * @author David Thacker
 * Date: 21 Jan 21
 * Class: Capstone
 */
class Battle {
    /**
     * the relative path to the death sound
     */
    static String fileName = "Explosion.wav";

    /**
     * the armies arraylist containing all armys and therefore warriors
     */
    private static ArrayList<Swarm> swarms = new ArrayList<>();

    //TODO: Placeholder for a gui assignment of the # of drones.
    public static int numDrones = 5;

    private static int collisionRadius = 300;
    //TODO: do drone attributes in a different location
    /**
     * sets the attributes of the drones, should be refactored
     *
     * @param array stores attributes of the drones in an array
     */
    static void setDroneArrayAttributes(int[] array) {
        array[0] = 50;//health
        array[1] = 10;//minAttack
        array[2] = 20;//maxAttack
        array[3] = 4;//minSpeed
        array[4] = 6;//maxSpeed
        array[5] = 10;//minCourage
        array[6] = 30;//maxCourage
        array[7] = 10;//Size
        array[8] = 200;//Range

    }

    Battle() {
        //TODO: Change ALgo Num
        addSwarm(0, "allies", swarms, 0);
        addSwarm(1, "axis", swarms, 1);

    }

    /**
     * adds an army with predetermined warrior allotments and the following attributes
     *
     * @param allianceNumber the number the army associates its alliance with
     * @param name           the name of that army
     * @param swarms         the arraylist containing all armys
     */
    static void addSwarm(int allianceNumber, String name, ArrayList<Swarm> swarms, int algoNum) {
        swarms.add(armySize(allianceNumber, name, algoNum));
    }

    static ArrayList<Swarm> getSwarms(){
        return swarms;
    }
    /**
     * calls drawWarriors while also passing the armies array contained within
     * @param g the graphics panel to be drawn on
     */
    static void drawSwarm(Graphics g) {
        Battle.drawDrone((Graphics2D) g, swarms);
    }

    /**
     * detectEnemy() uses the Vector330Class to determine the enemy closest to a selected warrior
     * and store the closest warrior, its magnitude, and army in the array
     *
     * @param attacker  the specific warrior to detect the closest enemy for
     * @param defenders the entire enemy army
     */
    private static void detectEnemy(Drone attacker, Swarm defenders, int[] minArray) {
        int index = -1;
        int minimumDistance = 1000;
        Vector330Class calcVector = new Vector330Class();
        //for every defender in the army check to see their distance
        for (int j = 0; j < defenders.drones.size(); j++) {
            //check to see if that selected soldier is alive
            if (defenders.drones.get(j).isAlive()) {
                //calculate a vector for the difference in positions between the warrior and the selected defender
                calcVector.setX(defenders.drones.get(j).getxPos() - attacker.getxPos());
                calcVector.setY(defenders.drones.get(j).getyPos() - attacker.getyPos());
                //check to see if that magnitude is the smallest yet
                if (calcVector.magnitude() < minArray[1]) {
                    minArray[2] = defenders.getAllianceNum();
                    minArray[1] = (int) calcVector.magnitude();
                    minArray[0] = j;
                }
            }
        }
    }

    /**
     * outOfBounds() detects if a specific warrior is out of the bounds of the graphics window
     *
     * @param attacker the warrior to check its position
     * @return a true if out of bounds
     */

    static boolean outOfBounds(Drone attacker) {
        return attacker.getxPos() > Main.SIZE * Main.ratioX || attacker.getyPos() > Main.SIZE * Main.ratioY || attacker.getxPos() < 0 || attacker.getyPos() < 0;
    }

    /**
     * moveWarriors() will move all warriors of the respective armies closer to closest enemy detected
     *
     */
    static void moveDrones()
    {
        Controller.Cont(swarms);
    }

    static int[] enemyDetection(Swarm Attackers, int i){
        int[] coords = new int[3];
        int[] soldierArray = Attackers.drones.get(i).getMinArray();
        int index = soldierArray[0];
        //if detectEnemy comes back with a -1 then there are no more alive enemies
        try
        {
            if (soldierArray[0] == -1)
            {
                throw new Exception("That does not exist.");
            }
        }
        catch (Exception ignored)
        {
        }
        Swarm axis = swarms.get(soldierArray[2]);
        coords[0] = axis.drones.get(index).getxPos();
        coords[1] = axis.drones.get(index).getyPos();
        coords[2] = axis.drones.get(index).getzPos();
        System.out.println("Target; " + coords[0] + " , " + coords[1] + " , " + coords[2] );
        return coords;
    }

    static boolean checkCollision(Swarm friendlies, Drone avoidance){
        int radiusSquared = collisionRadius * collisionRadius;
        for (int i = 0; i < friendlies.drones.size(); i++) {
            if(friendlies.drones.get(i).isAlive()){
                Drone comparison = friendlies.drones.get(i);
                int firstHalf = radiusCalculation(avoidance.getxPos(), avoidance.getyPos(), comparison.getxPos(), comparison.getyPos());
                if(firstHalf < radiusSquared){
                    //System.out.println("Collision detected between " + avoidance.getName() + " and " + comparison.getName());
                    return true;
                }
            }
        }
        return false;
    }

    static int radiusCalculation(int x1, int y1, int x2, int y2){
        int xCalc = (x1 - x2) * (x1 - x2);
        int yCalc = (y1 - y2) * (y1 - y2);
        return xCalc + yCalc;
    }


    /**
     * drawWarriors() puts the alive warriors of each army on screen in accordance with their x and y positions
     *
     * @param g      the graphics window to draw to
     * @param armies a array list containing all the armies in play
     */
    private static void drawDrone(Graphics2D g, ArrayList<Swarm> armies) {

        for (Swarm swarm : armies) {
            for (int i = 0; i < swarm.drones.size(); i++) {
                if (swarm.drones.get(i).isAlive()) {
                    swarm.drones.get(i).draw(g);
                    swarm.drones.get(i).drawFire(g);
                    swarm.drones.get(i).locationTracking();
                    swarm.drones.get(i).drawTracking(g);
                }
                else{
                    swarm.drones.get(i).drawExplosion(g);
                }
                swarm.drones.get(i).locationTracking();
                swarm.drones.get(i).drawTracking(g);
            }
        }
    }

    /**
     * warriorDamage() goes through all armies in play and deals damage to those alive
     *
     * @param armies a array list containing all the armies in play
     */
     static void droneDamage(ArrayList<Swarm> armies) {
        Random rand = new Random();
        for (Object army : armies) {
            Swarm Attackers = (Swarm) army;
            for (int i = 0; i < Attackers.drones.size(); i++) {
                if (Attackers.drones.get(i).isAlive()) {
                    int[] intArray = new int[4];
                    intArray[0] = -1; //index of lowest
                    intArray[1] = 1000; //magnitude of lowest
                    intArray[2] = -1; //army of lowest
                    for (Object o : armies) {
                        Swarm enemySwarm = (Swarm) o;
                        if (enemySwarm.getAllianceNum() != Attackers.getAllianceNum()) {
                            detectEnemy(Attackers.drones.get(i), enemySwarm, intArray);
                        }
                    }
                    try {
                        if (intArray[0] == -1) {
                            throw new Exception("That does not exist.");
                        }
                    } catch (Exception e) {
                        break;
                    }
                    Swarm Defenders = (Swarm) armies.get(intArray[2]);

                    //TODO: make this a test case
                    if (magnitude(Attackers.drones.get(i), Defenders, intArray[0]) <= Attackers.drones.get(i).getRange() + Attackers.drones.get(i).getSize() && Defenders.drones.get(intArray[0]).isAlive()) {
                        //determine if attacker has missed the defender
                        if (!(rand.nextInt(100) <= 100 * Attackers.drones.get(i).getAttack() / (Attackers.drones.get(i).getAttack() + Defenders.drones.get(intArray[0]).getAttack()))) {
                            //stop them from moving so that they can shoot or attack
                            Attackers.drones.get(i).setMoving(false);
                            Attackers.drones.get(i).setFireX(Defenders.drones.get(intArray[0]).getxPos());
                            Attackers.drones.get(i).setFireY(Defenders.drones.get(intArray[0]).getyPos());
                            Attackers.drones.get(i).setFiring(true);

                            //TODO: make test case
                            Defenders.drones.get(intArray[0]).setHealth(Defenders.drones.get(intArray[0]).getHealth() - Attackers.drones.get(i).getAttack()); //removes health

                            //show how much damage was done
                            System.out.println(Attackers.drones.get(i).getName() + " just dealt " + (Attackers.drones.get(i).getAttack()) +
                                    " damage to " + Defenders.drones.get(intArray[0]).getName() + " in army " + intArray[2]);

                            if (Defenders.drones.get(intArray[0]).getHealth() <= 0) {
                                playSound();
                                Defenders.drones.get(intArray[0]).setAlive(false);
                                Defenders.drones.get(intArray[0]).setHealth(0);
                                System.out.println(Attackers.drones.get(i).getName() + " " + i + " just killed " + Defenders.drones.get(intArray[0]).getName() + " " + intArray[0] + " in army " + intArray[2]);
                            }
                        } else {
                            //print out a missed message
                            System.out.println(Attackers.drones.get(i).getName() + " has just missed " + Defenders.drones.get(intArray[0]).getName());
                        }
                    } else {
                        Attackers.drones.get(i).setMoving(true);
                    }
                    Attackers.drones.get(i).setMinArray(intArray);
                }

            }
        }
    }

    /**
     * plays the minecraft death noise when a soldier is killed
     */
    private static void playSound() {
        //creates a noise when a warrior dies
        try {
            File soundFile = new File(fileName);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();
        } catch (UnsupportedAudioFileException e) {
            System.out.println("Audio file not supported, make sure its a wav");
        } catch (IOException e) {
            System.out.println("IO exception");
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    /**
     * magnitude() calculates the distance from a selected soldier to the closest enemy
     *
     * @param soldier  the selected warrior to compare its distance from a selected enemy
     * @param defender an army containing enemy warriors
     * @param index    the location of the closest enemy to the selected warrior
     * @return the distance between a warrior and a selected enemy
     */
    private static double magnitude(Drone soldier, Swarm defender, int index) {
        double distance;
        double deltaZ;
        Vector330Class calcVector = new Vector330Class();
        calcVector.setX(soldier.getxPos() - defender.drones.get(index).getxPos());
        calcVector.setY(soldier.getyPos() - defender.drones.get(index).getyPos());
        deltaZ = Math.abs(soldier.getzPos() - defender.drones.get(index).getzPos());
        distance = Math.sqrt(deltaZ * deltaZ) + (calcVector.magnitude() + calcVector.magnitude());
        return distance;
    }

    /**
     * armySize takes in a (currently) class variable and uses that number to set the number of drones in an army
     *
     * @param allianceNumber the identifier of a swarm
     * @param name the name of the swarm
     * @return a new swarm, or collection of drones
     */
    static Swarm armySize(int allianceNumber, String name, int algoNum) {
        if (name.equals("null")) {
            return new Swarm(allianceNumber, 0, name, 0);

        } else {
            setDroneArrayAttributes(Drone.DJIStats);

            return new Swarm(allianceNumber, numDrones, name, algoNum);
        }
    }


}