import javax.swing.*;


/**
 * Project: Swarm Capstone
 * : The Room Where It Happens
 *
 * @author David Thacker
 * Date: 21 Jan 21
 * Class: Capstone
 */
public class Test {
    //determines the size of the graphics display window
    static final int ratioX = 16;
    static final int ratioY = 9;
    static final int SIZE = 100;
    //the time to wait between drawing on screen and moving to next tick
    private static final int TIME_STEP = 100;

    public static void main(String[] args) {

        //call for preset values or not user selected values for each warrior class
        Battle battle = new Battle();
        for (int droneIdentifer = 0; droneIdentifer < Battle.numDrones; droneIdentifer++) { //
            System.out.println("Team 1 (x & y): " + Battle.getSwarms().get(0).drones.get(droneIdentifer).getxPos() + ", " + Battle.getSwarms().get(0).drones.get(droneIdentifer).getyPos());
            System.out.println("Team 2 (x & y): " + Battle.getSwarms().get(1).drones.get(droneIdentifer).getxPos() + ", " + Battle.getSwarms().get(1).drones.get(droneIdentifer).getyPos());
            //Battle.checkCollision(Battle.getSwarms().get(0), Battle.getSwarms().get(0).drones.get(droneIdentifer));
            assert(Battle.checkCollision(Battle.getSwarms().get(0), Battle.getSwarms().get(0).drones.get(droneIdentifer)) == true): "collision";
            //TODO: test cases

        }
        BattleGUI battleGUI = new BattleGUI();
        battleGUI.setBattle(battle);
        JFrame jFrame = new JFrame("DRONE SWARMING");
        jFrame.setContentPane(battleGUI.getHello());
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.pack();
        jFrame.setVisible(true);
        AnimationThread animationThread = new AnimationThread(battleGUI.getAnimationArea());
        animationThread.start();
        animationThread.toggleAnimation();
        battleGUI.setAnimationThread(animationThread);
    }

    //TODO: Fix screen size
}

