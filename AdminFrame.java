import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Admin Screen for managing questions.
 */
public class AdminFrame extends JFrame {
    private User currentUser;
    private JTable questionTable;
    private DefaultTableModel tableModel;
    private List<Question> allQuestions;

    public AdminFrame(User user) {
        this.currentUser = user;
        setTitle("Admin Panel - Manage Questions");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        String[] columnNames = {"Category", "Question", "Option 1", "Option 2", "Option 3", "Option 4", "Correct Index"};
        tableModel = new DefaultTableModel(columnNames, 0);
        questionTable = new JTable(tableModel);
        loadTableData();

        JScrollPane scrollPane = new JScrollPane(questionTable);
        add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        JButton deleteBtn = new JButton("Delete Selected");
        JButton backBtn = new JButton("Back to Menu");
        bottomPanel.add(deleteBtn);
        bottomPanel.add(backBtn);
        add(bottomPanel, BorderLayout.SOUTH);

        deleteBtn.addActionListener(e -> deleteSelectedQuestion());
        backBtn.addActionListener(e -> {
            new MenuFrame(currentUser).setVisible(true);
            this.dispose();
        });
    }

    private void loadTableData() {
        try {
            allQuestions = FileManager.loadQuestions();
            tableModel.setRowCount(0);
            for (Question q : allQuestions) {
                String[] options = q.getOptions();
                tableModel.addRow(new Object[]{
                    q.getCategory(),
                    q.getQuestionText(),
                    options[0],
                    options[1],
                    options[2],
                    options[3],
                    q.getCorrectOptionIndex() + 1
                });
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error loading questions: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteSelectedQuestion() {
        int selectedRow = questionTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a question to delete.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this question?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            allQuestions.remove(selectedRow);
            try {
                saveAllQuestions();
                loadTableData();
                JOptionPane.showMessageDialog(this, "Question deleted successfully.");
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error saving questions: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void saveAllQuestions() throws IOException {
        // We need a way to overwrite the file with all questions
        // I'll add a method to FileManager for this
        FileManager.overwriteQuestions(allQuestions);
    }
}
