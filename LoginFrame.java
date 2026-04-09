import javax.swing.*;
import java.awt.*;

/**
 * Login Screen for the Quiz System.
 */
public class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JButton loginButton;

    public LoginFrame() {
        setTitle("Quiz System - Login");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel welcomeLabel = new JLabel("Welcome to Advanced Quiz System");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(welcomeLabel, gbc);

        JLabel userLabel = new JLabel("Enter Username:");
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        add(userLabel, gbc);

        usernameField = new JTextField(15);
        gbc.gridx = 1;
        add(usernameField, gbc);

        loginButton = new JButton("Login");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        add(loginButton, gbc);

        loginButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            if (username.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Username cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                User user = new User(username);
                new MenuFrame(user).setVisible(true);
                this.dispose();
            }
        });
    }
}
