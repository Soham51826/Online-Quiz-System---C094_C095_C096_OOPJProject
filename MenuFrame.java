import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.List;

/**
 * Main Menu Screen.
 */
public class MenuFrame extends JFrame {
    private User currentUser;

    public MenuFrame(User user) {
        this.currentUser = user;
        setTitle("Quiz System - Main Menu");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(5, 1, 10, 10));

        JLabel welcomeLabel = new JLabel("Hello, " + user.getUsername(), SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        add(welcomeLabel);

        JButton takeQuizBtn = new JButton("Take Quiz");
        JButton createQuizBtn = new JButton("Create Quiz");
        JButton leaderboardBtn = new JButton("Leaderboard");
        JButton exitBtn = new JButton("Exit");

        add(takeQuizBtn);
        add(createQuizBtn);
        add(leaderboardBtn);
        add(exitBtn);

        takeQuizBtn.addActionListener(e -> startQuiz());
        createQuizBtn.addActionListener(e -> {
            new CreateQuizFrame(currentUser).setVisible(true);
            this.dispose();
        });
        leaderboardBtn.addActionListener(e -> {
            new LeaderboardFrame(currentUser).setVisible(true);
            this.dispose();
        });
        exitBtn.addActionListener(e -> System.exit(0));
    }

    private void startQuiz() {
        try {
            List<Question> questions = FileManager.loadQuestions();
            if (questions.isEmpty()) {
                throw new QuizException("No questions available! Please create some first.");
            }
            new QuizFrame(new QuizService(currentUser, questions)).setVisible(true);
            this.dispose();
        } catch (IOException | QuizException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
