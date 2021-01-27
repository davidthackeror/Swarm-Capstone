import javax.swing.*;

/**
 * Project: Swarm Capstone
 * : The Room Where It Happens
 *
 * @author David Thacker
 * Date: 21 Jan 21
 * Class: Capstone
 */
public class Main {
    //determines the size of the graphics display window
    static final int ratioX = 16;
    static final int ratioY = 9;
    static final int SIZE = 100;
    //the time to wait between drawing on screen and moving to next tick
    private static final int TIME_STEP = 100;

    public static void main(String[] args) {

        //call for preset values or not user selected values for each warrior class
        Battle battle = new Battle();
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

