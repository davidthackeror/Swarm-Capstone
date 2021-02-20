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

    public static void flank(ArrayList<Swarm> swarms) {
        Battle.droneDamage(swarms);
        for (Object army : swarms) {
            System.out.println("Shit");
            Swarm Attackers = (Swarm) army;
            for (int i = 0; i < Attackers.drones.size() / 2; i++) {
                if (Attackers.drones.get(i).isAlive() && Attackers.drones.get(i).isMoving()) {
                    if (Battle.outOfBounds(Attackers.drones.get(i))) {
                        Attackers.drones.get(i).setAlive(false);
                    } else {
                        int[] movementCoords = Battle.flankLeft(Attackers, i);
                        Attackers.drones.get(i).move(movementCoords[0], movementCoords[1], movementCoords[2]);
                    }
                }
            }
            for (int i = Attackers.drones.size() / 2; i < Attackers.drones.size(); i++) {
                if (Attackers.drones.get(i).isAlive() && Attackers.drones.get(i).isMoving()) {
                    if (Battle.outOfBounds(Attackers.drones.get(i))) {
                        Attackers.drones.get(i).setAlive(false);
                    } else {
                        int[] movementCoords = Battle.flankRight(Attackers, i);
                        Attackers.drones.get(i).move(movementCoords[0], movementCoords[1], movementCoords[2]);
                    }
                }
            }
        }
    }

    public static void fireTeam(ArrayList<Swarm> swarms, Swarm Attackers) {
        Battle.droneDamage(swarms);
        int size = Attackers.drones.size();

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

        aggregate(firstQuarter, Attackers);
        aggregate(secondQuarter, Attackers);
        aggregate(thirdQuarter, Attackers);
        aggregate(fourthQuarter, Attackers);

        if(checkAlive(swarms, Attackers)){
            for (Drone d: Attackers.drones) {
                d.setMoving(false);
                try {
                    gameOver(Attackers);
                }catch (Exception ignored){}
            }
        }

    }

    public static void gameOver(Swarm Victors) throws InterruptedException {
        System.out.println(Victors.getArmyName() + " has won!");
    }

    public static boolean checkAlive(ArrayList<Swarm> swarms, Swarm Attackers){
        for (Swarm s: swarms) {
            if(s != Attackers){
                if(s.numAlive() == 0){
                    return true;
                }
            }
        }
        return false;
    }


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

    public static int differenceInPosition(int x1, int x2) {
        return Math.abs(x1 - x2);
    }

    public static void aggregate(ArrayList<Drone> quarter, Swarm Attackers) {
        int xCompilation = 0;
        int yCompilation = 0;

        for (Drone e : quarter) {
            xCompilation = xCompilation + e.getxPos();
            yCompilation = yCompilation + e.getyPos();
        }
        int xAggregate = xCompilation / quarter.size();
        int yAggregate = yCompilation / quarter.size();

        for (Drone e : quarter) {
            if (checkLocationAggregate(quarter)) {
                modifiedLeeroyJenkins(quarter, Attackers);
            } else {
                e.move(xAggregate, yAggregate, e.getzPos());

            }
        }
    }

    public static void modifiedLeeroyJenkins(ArrayList<Drone> quarter, Swarm Attackers){
        for (Drone e: quarter) {
            if (e.isAlive() && e.isMoving()) {
                if (Battle.outOfBounds(e)) {
                   e.setAlive(false);
                } else {
                    for (int i = 0; i < Attackers.drones.size(); i++) {
                        if(Attackers.drones.get(i) == e){
                            int[] movementCoords = Battle.enemyDetection(Attackers, i);
                            e.move(movementCoords[0], movementCoords[1], movementCoords[2]);
                        }
                    }
                }
            }
        }


    }

}
