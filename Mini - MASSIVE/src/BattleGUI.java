import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
    /**
     * the combobox containing all the armies in this simulation
     */
    private JComboBox armyComboBox;
    /**
     * deletes the selected army from armyComboBox
     */
    private JButton delete;
    /**
     * adds a selected army with attributes from sliders and name and alliance from joptionpanes
     */
    private JButton addArmyButton;
    /**
     * the slider to select the size of a warrior in an army
     */
    private JSlider sizeSlider;
    /**
     * the slider to select the health of a warrior in an army
     */
    private JSlider healthSlider;
    /**
     * the text describing the size slider
     */
    private JLabel sizeSliderText;
    /**
     * the text describing the health slider
     */
    private JLabel healthSliderText;
    /**
     * the text describing the select army combobox
     */
    private JLabel selectArmyText;
    /**
     * the button that changes the stats of an already existing army based off the sliders values
     */
    private JButton changeButton;
    /**
     * the slider to select the speed of a warrior in an army
     */
    private JSlider maxSpeedSlider;
    /**
     * the text describing the speed slider
     */
    private JLabel maxSpeedSliderTxt;
    /**
     * the button that restarts and makes a new simulation
     */
    private JButton restart;

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
        /**
         * Creates the JComboBox for all armies in the battle
         */
        armyComboBox.addActionListener(e -> {
            if (!animationThread.getToggle()) {
                animationThread.toggleAnimation();
            }
            pauseOrStart.setText("Start");
            Object item = armyComboBox.getSelectedItem();
            Army curArmy = ((ComboArmyItem) item).getValue();
            if (armyComboBox.getSelectedIndex() != -1) {
                delete.setEnabled(true);
                addArmyButton.setEnabled(true);
                maxSpeedSlider.setEnabled(true);
                healthSlider.setEnabled(true);
                sizeSlider.setEnabled(true);
            }

        });
        /**
         * Deletes the selected army from the armyComboBox
         */
        delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Object item = armyComboBox.getSelectedItem();
                Army curArmy = ((ComboArmyItem) item).getValue();
                if (battle.getArmies().size() > 2) {
                    int answer = JOptionPane.showConfirmDialog(null, "Okay to delete army: " + curArmy.getArmyName() + "?");
                    if (answer == JOptionPane.YES_OPTION && battle.getArmies().size() >= 2) {
                        battle.getArmies().remove(curArmy);
                        armyComboBox.removeItem(item);
                        armyComboBox.repaint();

                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Cannot have less than 2 armies");
                }
            }
        });
        /**
         * adds an army with selected attributes of size, speed, health, along with a name and alliance number
         */
        addArmyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int size = sizeSlider.getValue();
                int health = healthSlider.getValue();
                int speed = maxSpeedSlider.getValue();
                String name = JOptionPane.showInputDialog("Please enter a name");
                String allianceNumber = JOptionPane.showInputDialog("Please enter an alliance number");
                int ally = Integer.parseInt(allianceNumber);
                if (ally > 2) {
                    ally = 2;
                }
                if (name != null) {

                    battle.addArmy(ally, name, battle.getArmies());
                    for (Warrior s : battle.getArmies().get(battle.getArmies().size() - 1).soldiers) {
                        s.setSize(size);
                        s.setSize(health);
                        s.setSize(speed);
                    }
                }
                armyComboBox.addItem(new ComboArmyItem(name, battle.getArmies().get(battle.getArmies().size() - 1)));
                battlePanel.repaint();


            }
        });


        sizeSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                size = sizeSlider.getValue();
            }
        });
        healthSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                health = healthSlider.getValue();

            }
        });
        maxSpeedSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                speed = maxSpeedSlider.getValue();
            }
        });
        changeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                Object item = armyComboBox.getSelectedItem();
                Army curArmy = ((ComboArmyItem) item).getValue();
                for (Warrior s : curArmy.soldiers) {
                    s.setSize(size);
                    s.setHealth(health);
                    s.setSpeed(speed);

                }
                battlePanel.repaint();

            }
        });
        restart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getHello().setVisible(false);
                OptionPanes.optionPanes();
                Battle battle = new Battle();
                BattleGUI battleGUI = new BattleGUI();
                battleGUI.setBattle(battle);
                JFrame jFrame = new JFrame("This is a battle");
                jFrame.setContentPane(battleGUI.getHello());
                jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                jFrame.pack();
                jFrame.setVisible(true);
                AnimationThread animationThread = new AnimationThread(battleGUI.getAnimationArea());
                animationThread.start();
                animationThread.toggleAnimation();
                battleGUI.setAnimationThread(animationThread);
                for (Army a : battle.getArmies()) {
                    battleGUI.getArmyComboBox().addItem(new ComboArmyItem(a.getArmyName(), a));
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
     * Fetches the address of the armyComboBox
     *
     * @return the armyComboBox
     */
    public JComboBox getArmyComboBox() {
        return armyComboBox;
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
