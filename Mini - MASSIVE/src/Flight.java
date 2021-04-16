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

    //TODO: smartly choose who climbs
    //This algorithm send drones forward to flight the enemy while fire teams climb to gain a height advantage
    public static void brainSwarm(ArrayList<Swarm> swarms, Swarm Attackers) {
        Battle.droneDamage(swarms);
        int size = Attackers.drones.size();
        int[] indexArray =  new int[Attackers.drones.size()];
        int[] zCoordinateArray = new int[Attackers.drones.size()];

        //Seperate drones in one swarm into quartiles
        ArrayList<Drone> firstSixth = new ArrayList<Drone>();
        ArrayList<Drone> secondSixth = new ArrayList<Drone>();
        ArrayList<Drone> thirdSixth = new ArrayList<Drone>();
        ArrayList<Drone> fourthSixth = new ArrayList<Drone>();
        ArrayList<Drone> fifthSixth = new ArrayList<Drone>();
        ArrayList<Drone> sixSixth = new ArrayList<Drone>();

        for (int i = 0; i < Attackers.drones.size(); i++) {
            indexArray[i] = i;
            zCoordinateArray[i] = Attackers.drones.get(i).getzPos();
        }
            
        for(int i = 0; i < Attackers.drones.size(); i++){ 
            for (int j = 0; j < Attackers.drones.size(); j++){
                if(zCoordinateArray[i] < zCoordinateArray[j])
                    {
                        int tempZ = zCoordinateArray[i];
                        int tempI = indexArray[i];
                        zCoordinateArray[i] = zCoordinateArray[j];
                        indexArray[i] = indexArray[j];
                        zCoordinateArray[j] = tempZ;
                        indexArray[j] = tempI;
                    }
                }
            }
        
        //for(int i = 0; i < Attackers.drones.size(); i++){
          //  System.out.println("Z Coordinate: " + zCoordinateArray[i] + "Index: " + indexArray[i]);
        //}

        for (int i = 0; i < size; i++) {
            if (i < size / 6) {
                firstSixth.add(Attackers.drones.get(indexArray[i]));
            } else if (i >= size / 6 && i < (size / 6) * 2) {
                secondSixth.add(Attackers.drones.get(indexArray[i]));
            } else if (i >= (size / 6) * 2 && i < (size / 2 )) {
                thirdSixth.add(Attackers.drones.get(indexArray[i]));
            } else if (i >= (size / 2) && i < ((size / 2) + (size / 6))) {
                fourthSixth.add(Attackers.drones.get(indexArray[i]));
            } else if (i >= ((size / 2) + (size / 6)) && i < ((size / 2) + ((size / 6) * 2))) {
                fifthSixth.add(Attackers.drones.get(indexArray[i]));
            } else {
                sixSixth.add(Attackers.drones.get(indexArray[i]));
            }
        }

        //Have the Drones group together, and wait for others, or move if all are nearby
        solve(firstSixth, 1, Attackers);
        solve(secondSixth, 2, Attackers);
        solve(thirdSixth, 3, Attackers);
        solve(fourthSixth, 4, Attackers);
        solve(fifthSixth, 5, Attackers);
        solve(sixSixth, 6, Attackers);

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

    public static void StutterStep(ArrayList<Swarm> swarms, Swarm Attackers) {
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
        aggregateStutter(firstQuarter, Attackers);
        aggregateStutter(secondQuarter, Attackers);
        aggregateStutter(thirdQuarter, Attackers);
        aggregateStutter(fourthQuarter, Attackers);

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

    public static void aggregateStutter(ArrayList<Drone> quarter, Swarm Attackers) {
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
                modifiedStutterStep(quarter, Attackers);
            } else {
                e.move(xAggregate, yAggregate, e.getzPos());

            }
        }
    }

    /**
     * Directs movement of fireteams, can direct them to move to the same point if to far away, or perform solution to find optimal place to move
     *
     * @param sixth   the fireteam to move
     * @param team   the number of the fireteam to move
     * @param Attackers the entire swarm of attackers, from which the fireteam came
     */
    public static void solve(ArrayList<Drone> sixth, int team, Swarm Attackers) {
        int xCompilation = 0;
        int yCompilation = 0;

        //formula for rough midpoint is (x1 + x2 + ... xi)/n
        for (Drone e : sixth) {
            xCompilation = xCompilation + e.getxPos();
            yCompilation = yCompilation + e.getyPos();
        }
        int xAggregate = xCompilation / sixth.size();
        int yAggregate = yCompilation / sixth.size();

        //for each drone in the fireteam check if they are close enough, if true leeroyJenkins, if not move to midpoint
        for (Drone e : sixth) {
            if (checkLocationAggregate(sixth)) {



                //Code to find optimal solution
                if (e.isAlive() && e.isMoving()) {
                    if (Battle.outOfBounds(e)) {
                        e.setAlive(false);
                    } else {
                        for (int i = 0; i < Attackers.drones.size(); i++) {
                            if (Attackers.drones.get(i) == e) {
                                int[] movementCoords = Battle.enemyDetection(Attackers, i);



                                if (team == 1) {
                                    e.move(movementCoords[0], movementCoords[1], movementCoords[2]); // leeroy
                                } else if (team == 2){
                                    e.move(movementCoords[0], movementCoords[1] + 50, movementCoords[2]); //go high
                                } else if (team == 3){
                                    e.move(movementCoords[0], movementCoords[1] - 50, movementCoords[2]); // go lower
                                }

                                int[] highestAttackCoords = Battle.highestEnemyDetection(Attackers);
                                double tangentXY = Math.sqrt(Math.pow(Math.abs(highestAttackCoords[0] - Attackers.drones.get(i).getxPos()), 2) + Math.pow(Math.abs(highestAttackCoords[1] - Attackers.drones.get(i).getyPos()), 2));
                                double tangentXYZ = Math.sqrt(tangentXY + Math.pow(Math.abs(highestAttackCoords[2] - Attackers.drones.get(i).getzPos()), 2));

                                //change number here to change the range of attack
                                if (tangentXYZ < 250){ //if the drones can attack
                                        if (team == 4 || team == 5 || team == 6) {
                                            e.move(highestAttackCoords[0], highestAttackCoords[1], highestAttackCoords[2]); //go destroy target
                                        }
                                } else { //if the drones are not in an optimal attack range
                                    if (team == 4) {
                                        e.move(highestAttackCoords[0], highestAttackCoords[1], Main.CEILING); // go to ceiling
                                    } else if (team == 5) {
                                        e.move(highestAttackCoords[0], highestAttackCoords[1] + 50, Main.CEILING); // go high ceiling
                                    } else if (team == 6) {
                                        e.move(highestAttackCoords[0], highestAttackCoords[1] - 50, Main.CEILING); // go lower ceiling
                                    }
                                }

                            }
                        }
                    }
                }





            } else {
                if (team == 4 || team == 5 || team == 6){
                    e.move(xAggregate, yAggregate, Main.CEILING);
                } else {
                    e.move(xAggregate, yAggregate, e.getzPos());
                }
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

    public static void modifiedStutterStep(ArrayList<Drone> quarter, Swarm Attackers) {
        for (Drone e : quarter) {
            if (e.isAlive() && e.isMoving()) {
                if (Battle.outOfBounds(e)) {
                    e.setAlive(false);
                } else {
                    for (int i = 0; i < Attackers.drones.size(); i++) {
                        if (Attackers.drones.get(i) == e) {
                            if(magnitude(e,Attackers,i)<200) {
                                int[] movementCoords = Battle.enemyDetection(Attackers, i);
                                e.move((-1*movementCoords[0]), (-1*movementCoords[1]), (-1*movementCoords[2]));
                            }
                            else {
                                int[] movementCoords = Battle.enemyDetection(Attackers, i);
                                e.move(movementCoords[0], movementCoords[1], movementCoords[2]);
                            }
                        }
                    }
                }
            }
        }
    }

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
}
