import javax.swing.*;

public class evacuationSim {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Peninsula Evacuation Simulation");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(new evacuationPanel());
        frame.pack();
        frame.setVisible(true);
    }
}
