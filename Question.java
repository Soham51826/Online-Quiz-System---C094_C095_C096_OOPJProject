
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
        if (line == null || line.trim().isEmpty()) return null;
        String[] parts = line.split("\\|");
        if (parts.length < 7) return null; 
        
        try {
            String cat = parts[0].trim();
            String text = parts[1].trim();
            String[] opts = new String[4];
            for (int i = 0; i < 4; i++) {
                opts[i] = parts[i + 2].trim();
            }
            int correctIndex = Integer.parseInt(parts[6].trim());
            return new Question(cat, text, opts, correctIndex);
        } catch (Exception e) {
            return null;
        }
    }
}
