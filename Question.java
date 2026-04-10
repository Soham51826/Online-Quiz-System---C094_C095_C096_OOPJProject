import java.util.Arrays;

/**
 * Represents a Question in the Quiz System.
 * Demonstrates: Encapsulation.
 */
public class Question {
    private String category;
    private String questionText;
    private String[] options;
    private int correctOptionIndex;

    public Question(String category, String questionText, String[] options, int correctOptionIndex) {
        this.category = category;
        this.questionText = questionText;
        this.options = options;
        this.correctOptionIndex = correctOptionIndex;
    }

    public String getCategory() {
        return category;
    }

    public String getQuestionText() {
        return questionText;
    }

    public String[] getOptions() {
        return options;
    }

    public int getCorrectOptionIndex() {
        return correctOptionIndex;
    }

    @Override
    public String toString() {
        return category + "|" + questionText + "|" + String.join("|", options) + "|" + correctOptionIndex;
    }

    /**
     * Factory method to create a Question object from a formatted string.
     */
    public static Question fromString(String line) {
        String[] parts = line.split("\\|");
        if (parts.length < 7) return null; // Updated for category
        String cat = parts[0];
        String text = parts[1];
        String[] opts = Arrays.copyOfRange(parts, 2, 6);
        int correctIndex = Integer.parseInt(parts[6]);
        return new Question(cat, text, opts, correctIndex);
    }
}
