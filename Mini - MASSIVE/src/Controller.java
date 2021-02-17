import java.util.ArrayList;

public class Controller {

    int algoChoice;

    public static void Cont(ArrayList<Swarm> swarms) {
        //placeholder

        for (int i = 0; i < swarms.size(); i++) {
            switch (swarms.get(i).getSwarmAlgo()) {
                case 0:
                    Flight.LeeroyJenkins(swarms);
                    break;

                case 1:
                    //goto algo 1
                    break;
                case 2:
                    //goto algo 2
                    break;
                case 3:
                    //goto algo 3
                    break;
                default:
                    //goto algo 0
            }
        }

    }
}
