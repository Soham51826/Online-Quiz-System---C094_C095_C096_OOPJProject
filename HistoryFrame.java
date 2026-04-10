import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.util.List;

/**
 * Personal History Screen for a User.
 */
public class HistoryFrame extends JFrame {
    private User currentUser;
    private DefaultTableModel model;

    public HistoryFrame(User user) {
        this.currentUser = user;
        setTitle("Quiz System - My History");
        setSize(500, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("My Past Scores", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(titleLabel, BorderLayout.NORTH);

        String[] columnNames = {"Date/Time", "Score", "Total"};
        model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable table = new JTable(model);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        loadHistory();

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout());
        JButton clearBtn = new JButton("Clear History");
        JButton backBtn = new JButton("Back to Menu");
        
        bottomPanel.add(clearBtn);
        bottomPanel.add(backBtn);
        add(bottomPanel, BorderLayout.SOUTH);

        clearBtn.addActionListener(e -> clearHistory());
        backBtn.addActionListener(e -> {
            new MenuFrame(currentUser).setVisible(true);
            this.dispose();
        });
    }

    private void loadHistory() {
        model.setRowCount(0);
        try {
            List<String[]> allScores = FileManager.loadScores();
            for (String[] score : allScores) {
                if (score[0].equalsIgnoreCase(currentUser.getUsername())) {
                    String timestamp = (score.length >= 4) ? score[3] : "N/A";
                    model.addRow(new Object[]{timestamp, score[1], score[2]});
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error loading history: " + e.getMessage());
        }
    }

    private void clearHistory() {
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to clear your entire history?", "Confirm Clear", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                FileManager.clearUserHistory(currentUser.getUsername());
                loadHistory();
                JOptionPane.showMessageDialog(this, "History cleared successfully.");
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error clearing history: " + e.getMessage());
            }
        }
    }
}
