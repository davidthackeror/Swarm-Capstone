import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Project: Swarm Capstone
 * : The GUI for displaying drone simulations
 *
 * @author David Thacker
 * Date: 21 Jan 21
 * Class: Capstone
 */

public class BattleGUI {
    /**
     * The panel running the battle simulation
     */
    private JPanel battlePanel;
    /**
     * the overarching panel all other panels run atop of
     */
    private JPanel Hello;
    /**
     * the button starts or pauses the simulation
     */
    private JButton pauseOrStart;
    private JRadioButton redTeam;
    private JRadioButton blueTeam;
    private JComboBox algorithmSelector;
    private JButton updateButton1;

    /**
     * the animationthread battle is ran in
     */
    private AnimationThread animationThread;
    /**
     * a battle class containing the armies and soldiers to do battle
     */
    private Battle battle;
    /**
     * the animation area to be drawn in
     */
    private AnimationArea animationArea;


    /**
     * the size from a jslider
     */
    private int size;
    /**
     * the speed from a jslider
     */
    private int speed;
    /**
     * the health from a jslider
     */
    private int health;

    public BattleGUI() {
        pauseOrStart.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e the event to be processed
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                if (redTeam.isSelected()){
                    System.out.println("red");
                    if (algorithmSelector.getSelectedItem().equals("Leroy Jenkins")){
                        System.out.println("Leroy Jenkins");
                        for (int i = 0; i < Battle.getSwarms().size(); i++) {
                            if(Battle.getSwarms().get(i).drones.get(0).getColor() == Color.RED){
                                Battle.getSwarms().get(i).setSwarmAlgo(0);
                            }
                        }
                    } else if (algorithmSelector.getSelectedItem().equals("Algorithm 2")){
                        System.out.println("Algorithm 2");
                    }
                } else if (blueTeam.isSelected()){
                    System.out.println("blue");
                    if (algorithmSelector.getSelectedItem().equals("Leroy Jenkins")){
                        System.out.println("Leroy Jenkins");
                    } else if (algorithmSelector.getSelectedItem().equals("Algorithm 2")){
                        System.out.println("Algorithm 2");
                    }
                }

                if (pauseOrStart.getText().equals("Start")) {
                    pauseOrStart.setText("Pause");
                } else {
                    pauseOrStart.setText("Start");
                }
                animationThread.toggleAnimation();
            }
        });
    }


    /**
     * Generated JVM command run automagically, instantiates the animation area and battle panel
     */
    private void createUIComponents() {
        this.battlePanel = new AnimationArea();
        this.animationArea = (AnimationArea) this.battlePanel;

    }

    /**
     * sets the battle to be run in this instance of the simulation
     *
     * @param battle a battle containing all armies and warriors
     */
    public void setBattle(Battle battle) {
        this.battle = battle;
    }

    /**
     * The overarching panel the GUI runs off of
     *
     * @return the Hello panel
     */
    public JPanel getHello() {
        return Hello;
    }

    /**
     * Fetches the animation area
     *
     * @return the animation area
     */
    public AnimationArea getAnimationArea() {
        return animationArea;
    }


    /**
     * sets the animation thread
     *
     * @param animationThread the animation thread to be insantiated in this instance of BattleGUI
     */
    public void setAnimationThread(AnimationThread animationThread) {
        this.animationThread = animationThread;
    }
}
