import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
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
    private JTextField searchField;
    private TableRowSorter<DefaultTableModel> sorter;

    public AdminFrame(User user) {
        this.currentUser = user;
        setTitle("Admin Panel - Manage Questions");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Top Panel for Search
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        topPanel.add(new JLabel("Search (Category/Question): "));
        searchField = new JTextField(25);
        topPanel.add(searchField);
        add(topPanel, BorderLayout.NORTH);

        String[] columnNames = {"Category", "Question", "Option 1", "Option 2", "Option 3", "Option 4", "Correct Index"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        questionTable = new JTable(tableModel);
        sorter = new TableRowSorter<>(tableModel);
        questionTable.setRowSorter(sorter);
        
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

        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { filter(); }
            @Override
            public void removeUpdate(DocumentEvent e) { filter(); }
            @Override
            public void changedUpdate(DocumentEvent e) { filter(); }

            private void filter() {
                String text = searchField.getText();
                if (text.trim().isEmpty()) {
                    sorter.setRowFilter(null);
                } else {
                    try {
                        sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                    } catch (java.util.regex.PatternSyntaxException e) {
                        // Ignore invalid regex
                    }
                }
            }
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

        // Convert view index to model index (important when filtered/sorted)
        int modelIndex = questionTable.convertRowIndexToModel(selectedRow);

        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this question?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                // Since modelIndex corresponds to the index in tableModel, 
                // and tableModel is populated from allQuestions in order,
                // we can reload and remove at modelIndex.
                List<Question> currentList = FileManager.loadQuestions();
                if (modelIndex >= 0 && modelIndex < currentList.size()) {
                    currentList.remove(modelIndex);
                    FileManager.overwriteQuestions(currentList);
                    loadTableData();
                    JOptionPane.showMessageDialog(this, "Question deleted successfully.");
                } else {
                    JOptionPane.showMessageDialog(this, "Error: Could not find question in list.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
