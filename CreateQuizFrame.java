import javax.swing.*;
import java.awt.*;
import java.io.IOException;

/**
 * Screen for creating new questions.
 */
public class CreateQuizFrame extends JFrame {
    private User currentUser;
    private JTextField categoryField, questionField, opt1Field, opt2Field, opt3Field, opt4Field, correctIdxField;

    public CreateQuizFrame(User user) {
        this.currentUser = user;
        setTitle("Create Quiz Question");
        setSize(500, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        addLabelAndField("Category:", categoryField = new JTextField(30), gbc, 0);
        addLabelAndField("Question:", questionField = new JTextField(30), gbc, 1);
        addLabelAndField("Option 1:", opt1Field = new JTextField(20), gbc, 2);
        addLabelAndField("Option 2:", opt2Field = new JTextField(20), gbc, 3);
        addLabelAndField("Option 3:", opt3Field = new JTextField(20), gbc, 4);
        addLabelAndField("Option 4:", opt4Field = new JTextField(20), gbc, 5);
        addLabelAndField("Correct Option (1-4):", correctIdxField = new JTextField(5), gbc, 6);

        JButton saveBtn = new JButton("Save Question");
        JButton backBtn = new JButton("Back to Menu");

        gbc.gridy = 7; gbc.gridx = 0; add(saveBtn, gbc);
        gbc.gridx = 1; add(backBtn, gbc);

        saveBtn.addActionListener(e -> saveQuestion());
        backBtn.addActionListener(e -> {
            new MenuFrame(currentUser).setVisible(true);
            this.dispose();
        });
    }

    private void addLabelAndField(String labelText, JTextField field, GridBagConstraints gbc, int row) {
        gbc.gridy = row;
        gbc.gridx = 0;
        add(new JLabel(labelText), gbc);
        gbc.gridx = 1;
        add(field, gbc);
    }

    private void saveQuestion() {
        try {
            String cat = categoryField.getText().trim();
            String q = questionField.getText().trim();
            String o1 = opt1Field.getText().trim();
            String o2 = opt2Field.getText().trim();
            String o3 = opt3Field.getText().trim();
            String o4 = opt4Field.getText().trim();
            String cStr = correctIdxField.getText().trim();

            if (cat.isEmpty() || q.isEmpty() || o1.isEmpty() || o2.isEmpty() || o3.isEmpty() || o4.isEmpty() || cStr.isEmpty()) {
                throw new QuizException("All fields are mandatory!");
            }

            int cIdx = Integer.parseInt(cStr) - 1; // 0-indexed
            if (cIdx < 0 || cIdx > 3) {
                throw new QuizException("Correct option must be between 1 and 4.");
            }

            Question question = new Question(cat, q, new String[]{o1, o2, o3, o4}, cIdx);
            FileManager.saveQuestion(question);
            JOptionPane.showMessageDialog(this, "Question Saved Successfully!");
            clearFields();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Correct option must be a number (1-4).", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IOException | QuizException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearFields() {
        categoryField.setText("");
        questionField.setText("");
        opt1Field.setText("");
        opt2Field.setText("");
        opt3Field.setText("");
        opt4Field.setText("");
        correctIdxField.setText("");
    }
}
