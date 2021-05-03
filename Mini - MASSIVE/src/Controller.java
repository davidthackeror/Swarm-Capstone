import java.util.ArrayList;

/**
 * Project: Swarm Capstone
 * : Controls which algorithm a drone executes
 *
 * @author David Thacker
 * @author Manny Riolo
 * @author Jesse Sidhu
 * @author Josiah Goosen
 * @author Luke Ruan
 * @author Michael Shumate
 * @author Addison Brown
 * Date: 21 Jan 21
 * Class: Capstone
 */
public class Controller {

    int algoChoice;

    /**
     * Control execution of drone algo's passing commands to functions in flight
     * @param swarms the arrayList of swarms
     */
    public static void Cont(ArrayList<Swarm> swarms) {
        //placeholder

        //Figures out which algorithm to call in flight
        for (int i = 0; i < swarms.size(); i++) {
            switch (swarms.get(i).getSwarmAlgo()) {
                case 0: //Flanking algorithm before initializing
                    Flight.LeeroyJenkins(swarms, swarms.get(i));
                    break;

                case 1:
                    swarms.get(i).setSwarmAlgo(3);
                    Flight.flank(swarms, 0, swarms.get(i));
                    break;
                case 2:
                    Flight.fireTeam(swarms, swarms.get(i));
                    break;
                case 3: //Flanking algorithm after initializing
                    swarms.get(i).setSwarmAlgo(4);
                    Flight.flank(swarms, 1, swarms.get(i));
                    break;
                case 4:
                    Flight.flank(swarms, 2, swarms.get(i));
                    break;
                case 5:
                    Flight.brainSwarm(swarms, swarms.get(i));
                    break;
                case 6:
                    Flight.StutterStep(swarms, swarms.get(i));
                default:
                    //goto algo 0
            }
        }

    }
}
