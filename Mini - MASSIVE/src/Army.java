import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

/**
 * Project: Mini - MASSIVE
 * : Organizes groups of warriors
 * @author David Thacker
 * Date: 22 Sept 19
 * Class: CS330
 */
public class Army{

    private int allianceNum;
    private String armyName;
    ArrayList<Warrior> soldiers = new ArrayList<Warrior>();


    Army(int allianceNum, int numArchers, int numKnights, int numDragons, int numOrcs, int numOgres, String armyName) {
        this.allianceNum = allianceNum;
        for (int i = 0; i < numArchers; i++) {
            soldiers.add(new Archer());
            soldiers.get(soldiers.size() - 1).setxPos(generateX(allianceNum));
            soldiers.get(soldiers.size() - 1).setyPos(generateY(allianceNum));
        }
        for (int i = 0; i < numKnights; i++) {
            soldiers.add(new Knight());
            soldiers.get(soldiers.size() - 1).setxPos(generateX(allianceNum));
            soldiers.get(soldiers.size() - 1).setyPos(generateY(allianceNum));
        }
        for (int i = 0; i < numOrcs; i++) {
            soldiers.add(new Orc(50, 250, 3, 6, 10, 60, 20, 40, 50));
            soldiers.get(soldiers.size() - 1).setxPos(generateX(allianceNum));
            soldiers.get(soldiers.size() - 1).setyPos(generateY(allianceNum));
        }
        for (int i = 0; i < numOgres; i++) {
            soldiers.add(new Ogre(100, 250, 3, 12, 10, 60, 20, 40, 50));
            soldiers.get(soldiers.size() - 1).setxPos(generateX(allianceNum));
            soldiers.get(soldiers.size() - 1).setyPos(generateY(allianceNum));
        }
        for (int i = 0; i < numDragons; i++) {
            soldiers.add(new Dragon());
            soldiers.get(soldiers.size() - 1).setxPos(generateX(allianceNum));
            soldiers.get(soldiers.size() - 1).setyPos(generateY(allianceNum));
        }
        setAllianceColor(allianceNum, soldiers);
        this.armyName = armyName;
    }

    /**
     * setAllianceColor() assigns the colors of the alliance to all warriors in that alliance
     *
     * @param allianceNum the selected alliance between 0 and 3 (max of 4 armies)
     * @param soldiers    the soldiers of the army to have their color set
     */
    private static void setAllianceColor(int allianceNum, ArrayList<Warrior> soldiers){
        switch (allianceNum) {
            case 0:
                for (Warrior warrior : soldiers) {
                    warrior.setColor(Color.BLACK);
                }
                break;
            case 2:
                for (Warrior soldier : soldiers) {
                    soldier.setColor(Color.BLUE);
                }
                break;
            case 1:
                for (Warrior warrior : soldiers) {
                    warrior.setColor(Color.YELLOW);
                }
                break;
            case 3:
                for (Warrior warrior : soldiers) {
                    warrior.setColor(Color.RED);
                }
                break;
            default:
                for (Warrior warrior : soldiers) {
                    warrior.setColor(Color.GREEN);
                }
                break;
        }
    }

    /**
     * getRandomNumberInRange() gets a random number bounded between the selected min and max
     *
     * @param min the minimum number to have the range bounded by
     * @param max the maximum number to have the range bounded by
     * @return the generated random number
     */
    static int getRandomNumberInRange(int min, int max) {

        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }

        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }

    /**
     * generateX() creates a random x value based on the spawn point of its alliance
     *
     * @param allianceNum the selected alliance between 0 and 3 (max of 4 armies)
     * @return a random x value based on the spawn points for that alliance number
     */
    private int generateX(int allianceNum){
        Random rand = new Random();
        switch (allianceNum) {
            case 0:
            case 2:
                return getRandomNumberInRange(40,Main.SIZE/2);
            case 1:
            case 3:
                return getRandomNumberInRange(Main.SIZE /2, Main.SIZE);
            default:
                return 0;
        }

    }

    /**
     * generateY() creates a random y value based on the spawn point of its alliance
     *
     * @param allianceNum the selected alliance between 0 and 3 (max of 4 armies)
     * @return a random y value based on the spawn points for that alliance number
     */
    private int generateY(int allianceNum){
        Random rand = new Random();
        switch (allianceNum) {
            case 0:
            case 3:
                return getRandomNumberInRange(40,Main.SIZE/2);
            case 1:
            case 2:
                return getRandomNumberInRange(Main.SIZE /2, Main.SIZE);
            default:
                return 0;
        }
    }

    /**
     * getAllianceNum() fetches a armies alliance number
     * @return the armies alliance number
     */
    public int getAllianceNum() {
        return allianceNum;
    }

    /**
     * setAllianceNum() sets a armies alliance number
     * @param allianceNum the new alliance number to be assigned
     */
    public void setAllianceNum(int allianceNum) {
        this.allianceNum = allianceNum;
    }

    /**
     * numAlive() fetches the number of warriors in an army with a health greater than 0
     * @return the number of soldiers with health greater than 0
     */
    int numAlive(){
        int numAlive = 0;
        for (Warrior soldier : soldiers) {
            if (soldier.isAlive()) {
                numAlive++;
            }
        }
        return numAlive;
    }

    public String getArmyName() {
        return armyName;
    }

    public void setArmyName(String armyName) {
        this.armyName = armyName;
    }
}