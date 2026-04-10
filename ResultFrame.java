import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.io.IOException;
import java.util.List;

/**
 * Result Screen.
 */
public class ResultFrame extends JFrame {
    private QuizService quizService;

    public ResultFrame(QuizService service) {
        this.quizService = service;
        setTitle("Quiz Results");
        setSize(800, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel headerPanel = new JPanel(new GridLayout(3, 1));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        JLabel resultLabel = new JLabel("Quiz Completed!", SwingConstants.CENTER);
        resultLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerPanel.add(resultLabel);

        JLabel scoreLabel = new JLabel("Your Score: " + service.getScore() + " / " + service.getTotalQuestions(), SwingConstants.CENTER);
        scoreLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        headerPanel.add(scoreLabel);

        JLabel percentLabel = new JLabel(String.format("Percentage: %.2f%%", service.getPercentage()), SwingConstants.CENTER);
        percentLabel.setFont(new Font("Arial", Font.BOLD, 18));
        percentLabel.setForeground(service.getPercentage() >= 50 ? Color.GREEN.darker() : Color.RED);
        headerPanel.add(percentLabel);
        
        add(headerPanel, BorderLayout.NORTH);

        // Answers breakdown
        String[] columnNames = {"Question", "Your Answer", "Correct Answer"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        JTable table = new JTable(model);
        table.setRowHeight(40); // Increased height for HTML text
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        
        List<Question> questions = service.getAskedQuestions();
        List<Integer> answers = service.getUserAnswers();
        
        for (int i = 0; i < answers.size(); i++) {
            Question q = questions.get(i);
            int userAnsIdx = answers.get(i);
            String userAnsStr = (userAnsIdx >= 0 && userAnsIdx < 4) ? q.getOptions()[userAnsIdx] : "Timeout/None";
            String correctAnsStr = q.getOptions()[q.getCorrectOptionIndex()];
            model.addRow(new Object[]{"<html><body style='width: 300px;'>" + q.getQuestionText() + "</body></html>", userAnsStr, correctAnsStr});
        }

        // Custom renderer for color coding
        table.getColumnModel().getColumn(1).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                int userAnsIdx = answers.get(row);
                int correctAnsIdx = questions.get(row).getCorrectOptionIndex();
                
                if (!isSelected) {
                    if (userAnsIdx == correctAnsIdx) {
                        c.setBackground(new Color(200, 255, 200)); // Light Green
                        c.setForeground(new Color(0, 100, 0));
                    } else {
                        c.setBackground(new Color(255, 200, 200)); // Light Red
                        c.setForeground(new Color(150, 0, 0));
                    }
                }
                setHorizontalAlignment(JLabel.CENTER);
                return c;
            }
        });
        
        // Center the 'Correct Answer' column as well
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        JButton menuBtn = new JButton("Back to Menu");
        menuBtn.setFont(new Font("Arial", Font.BOLD, 14));
        menuBtn.setPreferredSize(new Dimension(150, 40));
        bottomPanel.add(menuBtn);
        add(bottomPanel, BorderLayout.SOUTH);

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