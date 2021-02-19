import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

/**
 * Project: Swarm Capstone
 * : Organizes groups of drones
 *
 * @author David Thacker
 * Date: 21 Jan 21
 * Class: Capstone
 */
public class Swarm {

    private int allianceNum;
    private String armyName;
    public int swarmAlgo;
    ArrayList<Drone> drones = new ArrayList<Drone>();


    Swarm(int allianceNum, int numDJI, String armyName, int swarmAlgo) {
        this.allianceNum = allianceNum;
        for (int i = 0; i < numDJI; i++) {
            String name = armyName + allianceNum + ";" + i;
            drones.add(addAttributes(name, allianceNum));
        }
        setAllianceColor(allianceNum, drones);
        this.armyName = armyName;
        this.swarmAlgo = swarmAlgo;
    }

    private Drone addAttributes(String name, int allianceNum){
        Drone drone = new DJI(30,40,5,15,1,2,1,2, 10, 200, name);
        int suggestedX = generateX(allianceNum);
        int suggestedY = generateY(allianceNum);
        int suggestedZ = generateZ(allianceNum);
        drone.setxPos(suggestedX);
        drone.setyPos(suggestedY);
        drone.setzPos(suggestedZ);
        return drone;
    }

    /**
     * setAllianceColor() assigns the colors of the alliance to all warriors in that alliance
     *
     * @param allianceNum the selected alliance between 0 and 3 (max of 4 armies)
     * @param soldiers    the soldiers of the army to have their color set
     */
    private static void setAllianceColor(int allianceNum, ArrayList<Drone> soldiers){
        switch (allianceNum) {
            case 0:
                for (Drone drone : soldiers) {
                    drone.setColor(Color.RED);
                }
                break;
//            case 2:
//                for (Drone soldier : soldiers) {
//                    soldier.setColor(Color.BLACK);
//                }
//                break;
            case 1:
                for (Drone drone : soldiers) {
                    drone.setColor(Color.BLUE);
                }
                break;
//            case 3:
//                for (Drone drone : soldiers) {
//                    drone.setColor(Color.YELLOW);
//                }
//                break;
            default:
                for (Drone drone : soldiers) {
                    drone.setColor(Color.GREEN);
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
    static int generateX(int allianceNum){
        switch (allianceNum) {
            case 2:
            case 0:
                int rand = 0;
                while (1 > rand) {
                    rand = getRandomNumberInRange(10,(Main.SIZE * Main.ratioX)/2) - 50;
                }
                return rand;
            case 3:
            case 1:
                rand = 99999999;
                while ((Main.SIZE * Main.ratioX) < rand) {
                    rand = getRandomNumberInRange((Main.SIZE * Main.ratioX / 2) + 50, (Main.SIZE * Main.ratioX));
                }
                return rand - 50;
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
    static int generateY(int allianceNum){
        switch (allianceNum) {
            case 2:
            case 0:
                int rand = 0;
                while (1 > rand) {
                    rand = getRandomNumberInRange(10,(Main.SIZE * Main.ratioY)/2) - 50;
                }
                return rand;
            case 3:
            case 1:
                rand = 9999999;
                while ((Main.SIZE * Main.ratioY) < rand) {
                    rand = getRandomNumberInRange((Main.SIZE * Main.ratioY / 2) + 50, (Main.SIZE * Main.ratioY));
                }
                return rand - 50;
            default:
                return 0;
        }
    }

    /**
     * generateZ() creates a random y value based on the spawn point of its alliance
     *
     * @param allianceNum the selected alliance between 0 and 3 (max of 4 armies)
     * @return a random z value based on the spawn points for that alliance number
     */
    static int generateZ(int allianceNum){
        switch (allianceNum) {
            case 2:
            case 0:
                int rand = 0;
                while (1 > rand) {
                    rand = getRandomNumberInRange(0, Main.CEILING);
                }
                return rand;
            case 3:
            case 1:
                rand = 9999999;
                while (Main.CEILING < rand) {
                    rand = getRandomNumberInRange(0, Main.CEILING);
                }
                return rand - 50;
            default:
                return 0;
        }
    }

    public int getSwarmAlgo(){
        return swarmAlgo;
    }

    public void setSwarmAlgo(int x){
        swarmAlgo = x;
    }

    /**
     * getAllianceNum() fetches a armies alliance number
     * @return the armies alliance number
     */
    public int getAllianceNum() {
        return allianceNum;
    }


    /**
     * numAlive() fetches the number of warriors in an army with a health greater than 0
     * @return the number of soldiers with health greater than 0
     */
    int numAlive(){
        int numAlive = 0;
        for (Drone soldier : drones) {
            if (soldier.isAlive()) {
                numAlive++;
            }
        }
        return numAlive;
    }

    //TODO: Add Z Coordinate
    //TODO: Display Z Coordinate
    //TODO: Fix Collision Avoidance Algo, could be altitude deconfliction
    //TODO: Drone Fire Teams
    //TODO: Drone RADAR Sensors (change omnipotent features)    
    //TODO: Weapon Types and Ammunition
    //TODO: Flight Time / Battery Life
    //TODO: Wind -> Vector Added or Against Movement Vector
    //TODO: Obstacles, mountains, trees, etc
    //TODO: Different Maps
    //TODO: Attribute Selection GUI



}