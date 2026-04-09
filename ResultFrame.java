import javax.swing.*;
import java.awt.*;
import java.io.IOException;

/**
 * Result Screen.
 */
public class ResultFrame extends JFrame {
    private QuizService quizService;

    public ResultFrame(QuizService service) {
        this.quizService = service;
        setTitle("Quiz Results");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel resultLabel = new JLabel("Quiz Completed!");
        resultLabel.setFont(new Font("Arial", Font.BOLD, 20));
        gbc.gridy = 0; add(resultLabel, gbc);

        JLabel scoreLabel = new JLabel("Your Score: " + service.getScore() + " / " + service.getTotalQuestions());
        scoreLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        gbc.gridy = 1; add(scoreLabel, gbc);

        JLabel percentLabel = new JLabel(String.format("Percentage: %.2f%%", service.getPercentage()));
        percentLabel.setFont(new Font("Arial", Font.BOLD, 16));
        percentLabel.setForeground(service.getPercentage() >= 50 ? Color.GREEN.darker() : Color.RED);
        gbc.gridy = 2; add(percentLabel, gbc);

        JButton menuBtn = new JButton("Back to Menu");
        gbc.gridy = 3; add(menuBtn, gbc);

        menuBtn.addActionListener(e -> {
            new MenuFrame(service.getCurrentUser()).setVisible(true);
            this.dispose();
        });

        saveResult();
    }

    private void saveResult() {
        try {
            FileManager.saveScore(quizService.getCurrentUser().getUsername(), quizService.getScore(), quizService.getTotalQuestions());
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Failed to save score: " + e.getMessage());
        }
    }
}
