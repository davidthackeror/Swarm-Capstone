import java.util.ArrayList;
import java.util.Random;

public class Flight {

    public static void LeeroyJenkins(ArrayList<Swarm> swarms, Swarm Attackers) {
        Battle.droneDamage(swarms);
        for (int i = 0; i < Attackers.drones.size(); i++) {
            if (Attackers.drones.get(i).isAlive() && Attackers.drones.get(i).isMoving()) {
                if (Battle.outOfBounds(Attackers.drones.get(i))) {
                    Attackers.drones.get(i).setAlive(false);
                } else {
                    int[] movementCoords = Battle.enemyDetection(Attackers, i);
                    Attackers.drones.get(i).move(movementCoords[0], movementCoords[1], movementCoords[2]);
                }
            }
        }

    }

    public static void flank(ArrayList<Swarm> swarms, int check, Swarm Attackers) {
        Battle.droneDamage(swarms);
        //Drones flanking
        if (check == 0) { //only do this once
            //Determine if the drones is the draw in drone or not
            for (int i = 0; i < Attackers.drones.size(); i++) {
                if (Attackers.drones.get(i).getyPos() > ((Main.SIZE * Main.ratioY) / 2) - (Main.ratioY * 20) && Attackers.drones.get(i).getyPos() < ((Main.SIZE * Main.ratioY) / 2) + (Main.ratioY * 20)
                        && Attackers.drones.get(i).getxPos() > ((Main.SIZE * Main.ratioX) / 2) - (Main.ratioX * 20) && Attackers.drones.get(i).getxPos() < ((Main.SIZE * Main.ratioX) / 2) + (Main.ratioX * 20)) {
                    Attackers.drones.get(i).setposAchieved(4);
                }
            }
        }
        Vector330Class calcVector = new Vector330Class();
        //Every other time that the algorithm is called
        for (int i = 0; i < Attackers.drones.size(); i++) {
            if (Attackers.drones.get(i).isAlive() && Attackers.drones.get(i).isMoving()) {
                if (Battle.outOfBounds(Attackers.drones.get(i))) {
                    Attackers.drones.get(i).setAlive(false);
                } else {


                    if (Attackers.getAllianceNum() == 0 && (Attackers.drones.get(i).getxPos() < ((Main.SIZE * Main.ratioX) / 2) + 310 && Attackers.drones.get(i).getxPos() > ((Main.SIZE * Main.ratioX) / 2) + 290) &&
                            ((Attackers.drones.get(i).getyPos() > 40 && Attackers.drones.get(i).getyPos() < 60) || (Attackers.drones.get(i).getyPos() > Main.SIZE * Main.ratioY - 60 && Attackers.drones.get(i).getyPos() < Main.SIZE * Main.ratioY - 40))) {
                        Attackers.drones.get(i).setposAchieved(1);
                    } else if (Attackers.getAllianceNum() == 1 && (Attackers.drones.get(i).getxPos() < ((Main.SIZE * Main.ratioX) / 2) - 290 && Attackers.drones.get(i).getxPos() > ((Main.SIZE * Main.ratioX) / 2) - 310) &&
                            ((Attackers.drones.get(i).getyPos() > 40 && Attackers.drones.get(i).getyPos() < 60) || (Attackers.drones.get(i).getyPos() > Main.SIZE * Main.ratioY - 60 && Attackers.drones.get(i).getyPos() < Main.SIZE * Main.ratioY - 40))){
                        Attackers.drones.get(i).setposAchieved(1);
                    }


                    int[] movementCoords;

                    if (Attackers.drones.get(i).getposAchieved() == 0) {
                        movementCoords = Battle.flankLeft(Attackers, i);
                    } else if (Attackers.drones.get(i).getposAchieved() == 2) {
                        movementCoords = new int[]{25, Main.ratioY * Main.SIZE - 50, Attackers.drones.get(i).getzPos()}; //draw in position
                        if (Attackers.drones.get(i).getxPos() < 25 + 50 && Attackers.drones.get(i).getxPos() > (0)) { //check for at the draw in position
                            Attackers.drones.get(i).setposAchieved(1); //Set to go and go Leeroy Jenkins
                        }
                    } else {
                        movementCoords = Battle.enemyDetection(Attackers, i);
                    }
                    Attackers.drones.get(i).move(movementCoords[0], movementCoords[1], movementCoords[2]);
                }
            }

        }

    }


    /**
     * this algorithm separates drones into 4 fire teams that will act and move together - David & Manny
     *
     * @param swarms    a collection of all swarms in the battle
     * @param Attackers the swarm to aggregate the drones inside
     */
    public static void fireTeam(ArrayList<Swarm> swarms, Swarm Attackers) {
        Battle.droneDamage(swarms);
        int size = Attackers.drones.size();

        //Seperate drones in one swarm into quartiles
        ArrayList<Drone> firstQuarter = new ArrayList<Drone>();
        ArrayList<Drone> secondQuarter = new ArrayList<Drone>();
        ArrayList<Drone> thirdQuarter = new ArrayList<Drone>();
        ArrayList<Drone> fourthQuarter = new ArrayList<Drone>();

        for (int i = 0; i < size; i++) {
            if (i < size / 4) {
                firstQuarter.add(Attackers.drones.get(i));
            } else if (i >= size / 4 && i < size / 2) {
                secondQuarter.add(Attackers.drones.get(i));
            } else if (i >= size / 2 && i < ((size / 2) + size / 4)) {
                thirdQuarter.add(Attackers.drones.get(i));
            } else {
                fourthQuarter.add(Attackers.drones.get(i));
            }
        }

        //Have the Drones group together, and wait for others, or move if all are nearby
        aggregate(firstQuarter, Attackers);
        aggregate(secondQuarter, Attackers);
        aggregate(thirdQuarter, Attackers);
        aggregate(fourthQuarter, Attackers);

        //check if the enemy team has been destroyed
        if (checkAlive(swarms, Attackers)) {
            for (Drone d : Attackers.drones) {
                d.setMoving(false);
                try {
                    gameOver(Attackers);
                } catch (Exception ignored) {
                }
            }
        }

    }

    /**
     * Print a victory message
     *
     * @param Victors the swarm that won
     * @throws InterruptedException handles a thread.sleep if necessary, fucks with the GUI though
     */
    public static void gameOver(Swarm Victors) throws InterruptedException {
        System.out.println(Victors.getArmyName() + " has won!");
    }

    /**
     * helper function to determine if the enemy swarms have been deveated
     *
     * @param swarms    all the swarms in the battle
     * @param Attackers the hypothesized victorious swarm
     * @return true if the enemy has no drones left alive, otherwise false
     */
    public static boolean checkAlive(ArrayList<Swarm> swarms, Swarm Attackers) {
        for (Swarm s : swarms) {
            if (s != Attackers) {
                if (s.numAlive() == 0) {
                    return true;
                }
            }
        }
        return false;
    }


    /**
     * determines if the drones in a fireteam are close enough to each other to keep moving
     *
     * @param quarter the fireteam of drones
     * @return false if the drones are to far apart, otherwise true
     */
    public static boolean checkLocationAggregate(ArrayList<Drone> quarter) {
        for (Drone drone : quarter) {
            int xDiff = differenceInPosition(quarter.get(0).getxPos(), drone.getxPos());
            if (xDiff > 15 && drone != quarter.get(0)) {
                return false;
            }
            int yDiff = differenceInPosition(quarter.get(0).getyPos(), drone.getyPos());
            if (yDiff > 15 && drone != quarter.get(0)) {
                return false;
            }
        }
        return true;
    }

    /**
     * helper function to find the abs difference in the position of two drones
     *
     * @param x1 any coordinate of a drone
     * @param x2 the matching coordinate of another drone
     * @return the integer representing the abs value of their difference
     */
    public static int differenceInPosition(int x1, int x2) {
        return Math.abs(x1 - x2);
    }

    /**
     * Directs movement of fireteams, can direct them to move to the same point if to far away, or perform modified leeroyJenkins if they are close enough
     *
     * @param quarter   the fireteam to move
     * @param Attackers the entire swarm of attackers, from which the fireteam came
     */
    public static void aggregate(ArrayList<Drone> quarter, Swarm Attackers) {
        int xCompilation = 0;
        int yCompilation = 0;

        //formula for rough midpoint is (x1 + x2 + ... xi)/n
        for (Drone e : quarter) {
            xCompilation = xCompilation + e.getxPos();
            yCompilation = yCompilation + e.getyPos();
        }
        int xAggregate = xCompilation / quarter.size();
        int yAggregate = yCompilation / quarter.size();

        //for each drone in the fireteam check if they are close enough, if true leeroyJenkins, if not move to midpoint
        for (Drone e : quarter) {
            if (checkLocationAggregate(quarter)) {
                modifiedLeeroyJenkins(quarter, Attackers);
            } else {
                e.move(xAggregate, yAggregate, e.getzPos());

            }
        }
    }

    /**
     * modified version of leeroyJenkins to work with the fireteams, move all drones directly at closest enemy
     *
     * @param quarter   fireteam to be moved
     * @param Attackers the swarm from which the fireteams came from
     */
    public static void modifiedLeeroyJenkins(ArrayList<Drone> quarter, Swarm Attackers) {
        for (Drone e : quarter) {
            if (e.isAlive() && e.isMoving()) {
                if (Battle.outOfBounds(e)) {
                    e.setAlive(false);
                } else {
                    for (int i = 0; i < Attackers.drones.size(); i++) {
                        if (Attackers.drones.get(i) == e) {
                            int[] movementCoords = Battle.enemyDetection(Attackers, i);
                            e.move(movementCoords[0], movementCoords[1], movementCoords[2]);
                        }
                    }
                }
            }
        }


    }

}
