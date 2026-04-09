import javax.swing.SwingUtilities;

/**
 * Entry point for the Advanced Online Quiz System.
 */
public class Main {
    public static void main(String[] args) {
        // Run the GUI on the Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> {
            new LoginFrame().setVisible(true);
        });
    }
}
