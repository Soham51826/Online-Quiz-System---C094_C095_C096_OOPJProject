import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.util.List;

/**
 * Personal History Screen for a User.
 */
public class HistoryFrame extends JFrame {
    private User currentUser;

    public HistoryFrame(User user) {
        this.currentUser = user;
        setTitle("Quiz System - My History");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("My Past Scores", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        add(titleLabel, BorderLayout.NORTH);

        String[] columnNames = {"Date/Time", "Score", "Total"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(model);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.setRowHeight(25);

        try {
            List<String[]> allScores = FileManager.loadScores();
            for (String[] score : allScores) {
                if (score[0].equals(currentUser.getUsername())) {
                    model.addRow(new Object[]{"N/A", score[1], score[2]});
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error loading history: " + e.getMessage());
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
