import javax.sound.sampled.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

/**
 * Project: Mini - MASSIVE
 * : Controls execution of the battle
 *
 * @author David Thacker
 * Date: 22 Sept 19
 * Class: CS330
 */
class Battle {
    /**
     * the relative path to the death sound
     */
    static String fileName = "Minecraft-death-sound.wav";
    /**
     * the armies arraylist containing all armys and therefore warriors
     */
    private static ArrayList<Army> armies = new ArrayList<>();


    Battle() {
        OptionPanes.optionPanes();
        addArmy(0, "allies", armies);
        addArmy(1, "axis", armies);

    }



    /**
     * adds an army with predetermined warrior allotments and the following attributes
     *
     * @param allianceNumber the number the army associates its alliance with
     * @param name           the name of that army
     * @param armies         the arraylist containing all armys
     */
    static void addArmy(int allianceNumber, String name, ArrayList<Army> armies) {
        armies.add(OptionPanes.armySize(allianceNumber, name));
    }

    public ArrayList<Army> getArmies() {
        return armies;
    }

    /**
     * calls drawWarriors while also passing the armies array contained within
     * @param g the graphics panel to be drawn on
     */
    static void drawArmy(Graphics g) {
        Battle.drawWarriors((Graphics2D) g, armies);
    }
    /**
     * detectEnemy() uses the Vector330Class to determine the enemy closest to a selected warrior
     * and store the closest warrior, its magnitude, and army in the array
     *
     * @param attacker  the specific warrior to detect the closest enemy for
     * @param defenders the entire enemy army
     */
    private static void detectEnemy(Drone attacker, Army defenders, int[] minArray) {
        int index = -1;
        int minimumDistance = 1000;
        Vector330Class calcVector = new Vector330Class();
        //for every defender in the army check to see their distance
        for (int j = 0; j < defenders.soldiers.size(); j++) {
            //check to see if that selected soldier is alive
            if (defenders.soldiers.get(j).isAlive()) {
                //calculate a vector for the difference in positions between the warrior and the selected defender
                calcVector.setX(defenders.soldiers.get(j).getxPos() - attacker.getxPos());
                calcVector.setY(defenders.soldiers.get(j).getyPos() - attacker.getyPos());
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
    private static boolean outOfBounds(Drone attacker) {
        return attacker.getxPos() > Main.SIZE || attacker.getyPos() > Main.SIZE || attacker.getxPos() < 0 || attacker.getyPos() < 0;
    }

    /**
     * moveWarriors() will move all warriors of the respective armies closer to closest enemy detected
     *
     */
    static void moveWarriors() {
        warriorDamage(armies);
        Random rand = new Random();
        for (Object army : armies) {
            Army Attackers = (Army) army;
            for (int i = 0; i < Attackers.soldiers.size(); i++) {
                if (Attackers.soldiers.get(i).isAlive() && Attackers.soldiers.get(i).isMoving()) {
                    if (outOfBounds(Attackers.soldiers.get(i))) {
                        Attackers.soldiers.get(i).setAlive(false);
                    } else {
                        //get the closest enemy
                        int[] soldierArray = Attackers.soldiers.get(i).getMinArray();
                        int index = soldierArray[0];
                        //if detectEnemy comes back with a -1 then there are no more alive enemies
                        try {
                            if (soldierArray[0] == -1) {
                                throw new Exception("That does not exist.");
                            }
                        } catch (Exception e) {
                            System.out.println("An index of the nearest soldier was not found");
                            break;
                        }
                        Army axis = armies.get(soldierArray[2]);
                        //move the soldier towards their target
                        Attackers.soldiers.get(i).move(axis.soldiers.get(index).getxPos(), axis.soldiers.get(index).getyPos());
                    }
                }
            }
        }
    }

    /**
     * drawWarriors() puts the alive warriors of each army on screen in accordance with their x and y positions
     *
     * @param g      the graphics window to draw to
     * @param armies a array list containing all the armies in play
     */
    private static void drawWarriors(Graphics2D g, ArrayList<Army> armies) {

        for (Army army : armies) {
            for (int i = 0; i < army.soldiers.size(); i++) {
                if (army.soldiers.get(i).isAlive()) {
                    army.soldiers.get(i).draw(g);
                    army.soldiers.get(i).drawFire(g);
                }
                else{
                    army.soldiers.get(i).drawExplosion(g);
                }
            }
        }
    }

    /**
     * warriorDamage() goes through all armies in play and deals damage to those alive
     *
     * @param armies a array list containing all the armies in play
     */
    private static void warriorDamage(ArrayList<Army> armies) {

        Random rand = new Random();
        for (Object army : armies) {
            Army Attackers = (Army) army;
            for (int i = 0; i < Attackers.soldiers.size(); i++) {
                if (Attackers.soldiers.get(i).isAlive()) {
                    int[] intArray = new int[4];
                    intArray[0] = -1; //index of lowest
                    intArray[1] = 1000; //magnitude of lowest
                    intArray[2] = -1; //army of lowest
                    for (Object o : armies) {
                        Army enemyArmy = (Army) o;
                        if (enemyArmy.getAllianceNum() != Attackers.getAllianceNum()) {
                            detectEnemy(Attackers.soldiers.get(i), enemyArmy, intArray);
                        }
                    }
                    try {
                        if (intArray[0] == -1) {
                            throw new Exception("That does not exist.");
                        }
                    } catch (Exception e) {
                        System.out.println("An index of the nearest soldier was not found");
                        break;
                    }
                    Army Defenders = (Army) armies.get(intArray[2]);
                    if (magnitude(Attackers.soldiers.get(i), Defenders, intArray[0]) <= Attackers.soldiers.get(i).getRange() + Attackers.soldiers.get(i).getSize() && Defenders.soldiers.get(intArray[0]).isAlive()) {
                        //determine if attacker has missed the defender
                        if (!(rand.nextInt(100) <= 100 * Attackers.soldiers.get(i).getAttack() / (Attackers.soldiers.get(i).getAttack() + Defenders.soldiers.get(intArray[0]).getAttack()))) {
                            //stop them from moving so that they can shoot or attack
                            Attackers.soldiers.get(i).setMoving(false);
                            Attackers.soldiers.get(i).setFireX(Defenders.soldiers.get(intArray[0]).getxPos());
                            Attackers.soldiers.get(i).setFireY(Defenders.soldiers.get(intArray[0]).getyPos());
                            Attackers.soldiers.get(i).setFiring(true);
                            Defenders.soldiers.get(intArray[0]).setHealth(Defenders.soldiers.get(intArray[0]).getHealth() - Attackers.soldiers.get(i).getAttack());

                            //show how much damage was done
                            System.out.println(Attackers.soldiers.get(i).getName() + " just dealt " + (Attackers.soldiers.get(i).getAttack()) +
                                    " damage to " + Defenders.soldiers.get(intArray[0]).getName() + " in army " + intArray[2]);

                            if (Defenders.soldiers.get(intArray[0]).getHealth() <= 0) {
                                playSound();
                                Defenders.soldiers.get(intArray[0]).setAlive(false);
                                Defenders.soldiers.get(intArray[0]).setHealth(0);
                                System.out.println(Attackers.soldiers.get(i).getName() + " " + i + " just killed " + Defenders.soldiers.get(intArray[0]).getName() + " " + intArray[0] + " in army " + intArray[2]);
                            }
                        } else {
                            //print out a missed message
                            System.out.println(Attackers.soldiers.get(i).getName() + " has just missed " + Defenders.soldiers.get(intArray[0]).getName());
                        }
                    } else {
                        Attackers.soldiers.get(i).setMoving(true);
                    }
                    Attackers.soldiers.get(i).setMinArray(intArray);
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
    private static double magnitude(Drone soldier, Army defender, int index) {
        double distance;
        Vector330Class calcVector = new Vector330Class();
        calcVector.setX(soldier.getxPos() - defender.soldiers.get(index).getxPos());
        calcVector.setY(soldier.getyPos() - defender.soldiers.get(index).getyPos());
        distance = calcVector.magnitude();
        return distance;
    }
}