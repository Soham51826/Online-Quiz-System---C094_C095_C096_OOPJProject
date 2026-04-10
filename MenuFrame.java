import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.List;

/**
 * Main Menu Screen.
 */
public class MenuFrame extends JFrame {
    private User currentUser;
    private JComboBox<String> categoryBox;

    public MenuFrame(User user) {
        this.currentUser = user;
        setTitle("Quiz System - Main Menu");
        setSize(450, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel welcomeLabel = new JLabel("Hello, " + user.getUsername(), SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        add(welcomeLabel, gbc);

        JLabel catLabel = new JLabel("Select Category:");
        gbc.gridy = 1; gbc.gridwidth = 1;
        add(catLabel, gbc);

        categoryBox = new JComboBox<>();
        categoryBox.addItem("All Categories");
        try {
            for (String cat : FileManager.getCategories()) {
                categoryBox.addItem(cat);
            }
        } catch (IOException e) {
            // Silently fail or log
        }
        gbc.gridx = 1;
        add(categoryBox, gbc);

        JButton takeQuizBtn = new JButton("Take Quiz");
        JButton createQuizBtn = new JButton("Create Quiz");
        JButton leaderboardBtn = new JButton("Leaderboard");
        JButton historyBtn = new JButton("My History");
        JButton adminBtn = new JButton("Admin Panel");
        JButton exitBtn = new JButton("Exit");

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2; add(takeQuizBtn, gbc);
        gbc.gridy = 3; add(createQuizBtn, gbc);
        gbc.gridy = 4; add(leaderboardBtn, gbc);
        gbc.gridy = 5; add(historyBtn, gbc);
        gbc.gridy = 6; add(adminBtn, gbc);
        gbc.gridy = 7; add(exitBtn, gbc);

        takeQuizBtn.addActionListener(e -> startQuiz());
        createQuizBtn.addActionListener(e -> {
            new CreateQuizFrame(currentUser).setVisible(true);
            this.dispose();
        });
        leaderboardBtn.addActionListener(e -> {
            new LeaderboardFrame(currentUser).setVisible(true);
            this.dispose();
        });
        historyBtn.addActionListener(e -> {
            new HistoryFrame(currentUser).setVisible(true);
            this.dispose();
        });
        adminBtn.addActionListener(e -> {
            String pass = JOptionPane.showInputDialog(this, "Enter Admin Password:", "Admin Access", JOptionPane.QUESTION_MESSAGE);
            if ("admin123".equals(pass)) {
                new AdminFrame(currentUser).setVisible(true);
                this.dispose();
            } else if (pass != null) {
                JOptionPane.showMessageDialog(this, "Incorrect Password!", "Access Denied", JOptionPane.ERROR_MESSAGE);
            }
        });
        exitBtn.addActionListener(e -> System.exit(0));
    }

    private void startQuiz() {
        try {
            List<Question> questions = FileManager.loadQuestions();
            String selectedCategory = (String) categoryBox.getSelectedItem();

            if (!selectedCategory.equals("All Categories")) {
                questions.removeIf(q -> !q.getCategory().equals(selectedCategory));
            }

            if (questions.isEmpty()) {
                throw new QuizException("No questions available for this category!");
            }
            new QuizFrame(new QuizService(currentUser, questions)).setVisible(true);
            this.dispose();
        } catch (IOException | QuizException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
