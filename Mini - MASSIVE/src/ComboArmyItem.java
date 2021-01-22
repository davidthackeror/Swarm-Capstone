/**
 * Project: Mini - MASSIVE
 * : Type of class that armies are inserted into the JComboBox armyComboBox
 *
 * @author David Thacker
 * Date: 22 Sept 19
 * Class: CS330
 */
public class ComboArmyItem {
    private String key;
    private Swarm value;

    public ComboArmyItem(String key, Swarm value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public String toString() {
        return key;
    }

    /**
     * fetches the army stored in this class
     *
     * @return the army stored inside
     */
    public String getKey() {
        return key;
    }

    /**
     * fetches the name of the comboitem
     *
     * @return the name of the army
     */
    public Swarm getValue() {
        return value;
    }

}
