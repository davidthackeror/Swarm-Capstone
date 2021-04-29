import javax.swing.*;
import java.io.*;
import java.util.ArrayList;

/**
 * Project: Swarm Capstone
 * : Creates and animates on the animationThread and JPanel
 *
 * @author David Thacker
 * Date: 21 Jan 21
 * Class: Capstone
 */
// AnimationThread - subclass of Thread to run the animation
public class AnimationThread extends Thread {

    private AnimationArea animationArea;  // points to JPanel subclass object
    private boolean stopper = false;    // toggles animation on and off
    private int animationDeathTimer = 60; // allows animations to subside once battle has ended
    private int ticks = 0;

    // constructor for AnimationThread

    /**
     * contructor for the AnimationThread
     * @param animationPanel the animationPanel to be instantiated
     */
    public AnimationThread(AnimationArea animationPanel) {
        animationArea = animationPanel;
    }

    // toggleAnimation() - toggles animation on and off
    public void toggleAnimation() {
        this.stopper = !this.stopper;  // flip the stopper attribute
    }

    public boolean getToggle() {
        return this.stopper;}

    public void battleSummary() throws IOException {
        ArrayList<Swarm> swarms = Battle.getSwarms();
        for (int i = 0; i < swarms.size(); i++) {
            Swarm currSwarm = swarms.get(i);
            int k;
            if (i == 0){
                k = 1;
            }
            else {
                k = 0;
            }
            Swarm otherSwarm = swarms.get(k);
            if (currSwarm.numAlive() == 0){
                animationDeathTimer--;
                if (animationDeathTimer == 0) {
                    System.out.println(
                            // Using ticks because speeds is affected by computer power but ticks are consistent
                            "Battle was completed in " + ticks + " ticks.\n" +
                                    currSwarm.getArmyName() + " was defeated.\n" +
                                    otherSwarm.numAlive() + " " + otherSwarm.getArmyName() + " remain.\n" +
                                    "Algo " + otherSwarm.swarmAlgo + " beat Algo " + currSwarm.swarmAlgo + ".");
                    toggleAnimation();
                    File tempFile = new File("src/results.txt");
                    if(!tempFile.exists()){
                        String addInfo = "Ticks;  Losing Team; Number of Drones Left Alive; Winning Drone Num; Losing Drone Num";
                        FileWriter fw = new FileWriter("src/results.txt", true);
                        BufferedWriter bw = new BufferedWriter(fw);
                        PrintWriter pw = new PrintWriter(bw);
                        String resultsString = ticks + ", " + currSwarm.getArmyName() + ", " + otherSwarm.numAlive() + ", " + findName(otherSwarm.swarmAlgo) + ", " + findName(currSwarm.swarmAlgo);
                        //BufferedWriter writer = new BufferedWriter(new FileWriter("src/results.txt", true));
                        pw.println(addInfo);
                        pw.println(resultsString);
                        pw.flush();

                        pw.close();
                        bw.close();
                        fw.close();
                    }else{
                        FileWriter fw = new FileWriter("src/results.txt", true);
                        BufferedWriter bw = new BufferedWriter(fw);
                        PrintWriter pw = new PrintWriter(bw);
                        String resultsString = ticks + ", " + currSwarm.getArmyName() + ", " + otherSwarm.numAlive() + ", " + findName(otherSwarm.swarmAlgo) + ", " + findName(currSwarm.swarmAlgo);
                        //BufferedWriter writer = new BufferedWriter(new FileWriter("src/results.txt", true));
                        pw.println(resultsString);
                        pw.flush();

                        pw.close();
                        bw.close();
                        fw.close();
                    }
                    this.stopper = true;
                    run();

                    //writer.close();
                }
            }
        }
    }
    public static String findName(int num){
        switch (num) {
            case 0: //Flanking algorithm before initializing
                return "Leeroy Jenkins";
            case 1:
            case 3:
            case 4:
                return "Flank";
            case 2:
                return "Fire Teams";
            case 5:
                return "Brain Swarm";
            case 6:
                return "Stutter Step";
            default:
                //goto algo 0
        }
        return "Unknown Algo";
    }

    // run() method is invoked automatically by the Java VM
    public void run() {
        while (true) {
            if (!stopper) {  // for toggling on/off the animation
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        animationArea.animate();  // runs the animation
                    } // end of run() method
                });
                ticks++;
                try {
                    battleSummary();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                sleep(100);  // pause between thread launches
            } catch (InterruptedException e) {
            }
        } // end of while loop

    } // end of run() method

} // end AnimationThread