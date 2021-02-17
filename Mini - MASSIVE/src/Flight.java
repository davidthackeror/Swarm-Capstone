import java.util.ArrayList;
import java.util.Random;

public class Flight {

    public static void LeeroyJenkins(ArrayList<Swarm> swarms){
            Battle.droneDamage(swarms);
            Random rand = new Random();
            for (Object army : swarms)
            {
                Swarm Attackers = (Swarm) army;
                for (int i = 0; i < Attackers.drones.size(); i++)
                {
                    if (Attackers.drones.get(i).isAlive() && Attackers.drones.get(i).isMoving())
                    {
                        if (Battle.outOfBounds(Attackers.drones.get(i)))
                        {
                            Attackers.drones.get(i).setAlive(false);
                        }
                        else{
                            int[] movementCoords = Battle.enemyDetection(Attackers, i);
                            Attackers.drones.get(i).move(movementCoords[0], movementCoords[1], movementCoords[2]);
                        }
                    }
                }
            }
    }
}
