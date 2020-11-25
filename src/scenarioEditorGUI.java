import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class scenarioEditorGUI extends JFrame{
    private JButton button1;
    private JPanel contentPane;
    private JTextField xInputField;
    private JTextField yInputField;
    private JTextField zInputField;

    public scenarioEditorGUI() {
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                int xInput = Integer.parseInt(xInputField.getText());
                int yInput = Integer.parseInt(yInputField.getText());
                int zInput = Integer.parseInt(zInputField.getText());

                try {
                    BufferedReader in = new BufferedReader(new FileReader(".\\src\\test"));
                    String line = in.readLine();
                    BufferedWriter out = new BufferedWriter(new FileWriter(".\\src\\test"));
                    while(line != null) {
                        if (line.equals("Drone Coordinates:")) {
                            out.write(line + "\n");
                            line = in.readLine(); // Keep up with file writing
                            out.write("X: " + Integer.toString(xInput) + "\n");
                            line = in.readLine(); // Keep up with file writing
                            out.write("Y: " + Integer.toString(yInput) + "\n");
                            line = in.readLine(); // Keep up with file writing
                            out.write("Z: " + Integer.toString(zInput) + "\n");
                        }
                        // Further conditionals go here
                        line = in.readLine();
                    }
                    in.close();
                    out.close();
                    System.out.println("Successfully updated.");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("myGui");
        frame.setContentPane( new scenarioEditorGUI().contentPane);
        frame.setTitle("test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBounds(100, 100, 450, 297);
        frame.setVisible(true);
    }
}
