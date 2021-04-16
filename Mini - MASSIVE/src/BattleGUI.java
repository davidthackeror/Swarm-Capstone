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
    private JLabel one;
    private JLabel two;

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

                if (pauseOrStart.getText().equals("Start")) {
                    pauseOrStart.setText("Pause");
                } else {
                    pauseOrStart.setText("Start");
                }
                animationThread.toggleAnimation();
            }
        });

        updateButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (redTeam.isSelected()){
                    System.out.println("red");
                    if (algorithmSelector.getSelectedItem().equals("Leeroy Jenkins") && updateButton1.isValid()){
                        System.out.println("Leroy Jenkins");
                        for (int i = 0; i < Battle.getSwarms().size(); i++) {
                            if(Battle.getSwarms().get(i).drones.get(0).getColor() == Color.RED){
                                Battle.getSwarms().get(i).setSwarmAlgo(0);
                            }
                        }
                        one.setText("Red Team Algorithm: Leeroy Jenkins");
                    } else if (algorithmSelector.getSelectedItem().equals("Flanking") && updateButton1.isValid()){
                        System.out.println("Red Flanking");
                        for (int i = 0; i < Battle.getSwarms().size(); i++) {
                            if(Battle.getSwarms().get(i).drones.get(0).getColor() == Color.RED){
                                Battle.getSwarms().get(i).setSwarmAlgo(1);
                            }
                        }
                        one.setText("Red Team Algorithm: Flanking");
                    } else if (algorithmSelector.getSelectedItem().equals("Fire Teams") && updateButton1.isValid()){
                        System.out.println("Red Fire Teams");
                        for (int i = 0; i < Battle.getSwarms().size(); i++) {
                            if(Battle.getSwarms().get(i).drones.get(0).getColor() == Color.RED){
                                Battle.getSwarms().get(i).setSwarmAlgo(2);
                            }
                        }
                        one.setText("Red Team Algorithm: Fire Teams");
                    } else if (algorithmSelector.getSelectedItem().equals("Stutter Step") && updateButton1.isValid()){
                        System.out.println("Red Stutter Step");
                        for (int i = 0; i < Battle.getSwarms().size(); i++) {
                            if(Battle.getSwarms().get(i).drones.get(0).getColor() == Color.RED){
                                Battle.getSwarms().get(i).setSwarmAlgo(5);
                            }
                        }
                        one.setText("Red Team Algorithm: Stutter Step");
                    }
                } else if (blueTeam.isSelected()){
                    System.out.println("blue");
                    if (algorithmSelector.getSelectedItem().equals("Leeroy Jenkins") && updateButton1.isValid()){
                        System.out.println("Leroy Jenkins");
                        for (int i = 0; i < Battle.getSwarms().size(); i++) {
                            if(Battle.getSwarms().get(i).drones.get(0).getColor() == Color.BLUE){
                                Battle.getSwarms().get(i).setSwarmAlgo(0);
                            }
                        }
                        two.setText("Blue Team Algorithm: Leeroy Jenkins");
                    } else if (algorithmSelector.getSelectedItem().equals("Flanking") && updateButton1.isValid()){
                        System.out.println("Blue Flanking");
                        for (int i = 0; i < Battle.getSwarms().size(); i++) {
                            if(Battle.getSwarms().get(i).drones.get(0).getColor() == Color.BLUE){
                                Battle.getSwarms().get(i).setSwarmAlgo(1);
                            }
                        }
                        two.setText("Blue Team Algorithm: Flanking");
                    } else if (algorithmSelector.getSelectedItem().equals("Fire Teams") && updateButton1.isValid()){
                        System.out.println("Blue Fire Teams");
                        for (int i = 0; i < Battle.getSwarms().size(); i++) {
                            if(Battle.getSwarms().get(i).drones.get(0).getColor() == Color.BLUE){
                                Battle.getSwarms().get(i).setSwarmAlgo(2);
                            }
                        }
                        two.setText("Blue Team Algorithm: Fire Teams");
                    }  else if (algorithmSelector.getSelectedItem().equals("Stutter Step") && updateButton1.isValid()){
                        System.out.println("Blue Stutter Step");
                        for (int i = 0; i < Battle.getSwarms().size(); i++) {
                            if(Battle.getSwarms().get(i).drones.get(0).getColor() == Color.BLUE){
                                Battle.getSwarms().get(i).setSwarmAlgo(5);
                            }
                        }
                        one.setText("Blue Team Algorithm: Stutter Step");
                    }
                }
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
