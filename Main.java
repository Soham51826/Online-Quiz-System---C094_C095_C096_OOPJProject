import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Entry point for the Advanced Online Quiz System.
 */
public class Main {
    public static void main(String[] args) {
        // Run the GUI on the Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                // Ignore and use default look and feel
            }
            new LoginFrame().setVisible(true);
        });
    }
}
