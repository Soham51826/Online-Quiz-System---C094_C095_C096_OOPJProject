import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.util.List;

/**
 * Leaderboard Screen.
 */
public class LeaderboardFrame extends JFrame {
    private User currentUser;

    public LeaderboardFrame(User user) {
        this.currentUser = user;
        setTitle("Quiz System - Leaderboard");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Top Scores", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        add(titleLabel, BorderLayout.NORTH);

        String[] columnNames = {"Rank", "Username", "Score", "Total"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(model);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.setRowHeight(25);

        try {
            List<String[]> scores = FileManager.loadScores();
            int rank = 1;
            for (String[] score : scores) {
                model.addRow(new Object[]{rank++, score[0], score[1], score[2]});
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error loading leaderboard: " + e.getMessage());
        }

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        JButton backBtn = new JButton("Back to Menu");
        add(backBtn, BorderLayout.SOUTH);

        backBtn.addActionListener(e -> {
            new MenuFrame(currentUser).setVisible(true);
            this.dispose();
        });
    }
}
