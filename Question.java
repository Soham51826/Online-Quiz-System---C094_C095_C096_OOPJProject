import java.util.Arrays;

/**
 * Represents a Question in the Quiz System.
 * Demonstrates: Encapsulation.
 */
public class Question {
    private String questionText;
    private String[] options;
    private int correctOptionIndex;

    public Question(String questionText, String[] options, int correctOptionIndex) {
        this.questionText = questionText;
        this.options = options;
        this.correctOptionIndex = correctOptionIndex;
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
        return questionText + "|" + String.join("|", options) + "|" + correctOptionIndex;
    }

    /**
     * Factory method to create a Question object from a formatted string.
     */
    public static Question fromString(String line) {
        String[] parts = line.split("\\|");
        if (parts.length < 6) return null;
        String text = parts[0];
        String[] opts = Arrays.copyOfRange(parts, 1, 5);
        int correctIndex = Integer.parseInt(parts[5]);
        return new Question(text, opts, correctIndex);
    }
}
