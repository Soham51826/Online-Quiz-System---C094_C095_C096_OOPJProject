import java.io.*;
import java.util.*;

/**
 * Handles all file-related operations (Questions and Scores).
 */
public class FileManager {
    private static final String QUESTIONS_FILE = "questions.txt";
    private static final String SCORES_FILE = "scores.txt";

    /**
     * Saves a new question to the file.
     */
    public static void saveQuestion(Question question) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(QUESTIONS_FILE, true))) {
            bw.write(question.toString());
            bw.newLine();
        }
    }

    /**
     * Loads all questions from the file.
     */
    public static List<Question> loadQuestions() throws IOException {
        List<Question> questions = new ArrayList<>();
        File file = new File(QUESTIONS_FILE);
        if (!file.exists()) return questions;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                Question q = Question.fromString(line);
                if (q != null) questions.add(q);
            }
        }
        return questions;
    }

    /**
     * Saves a user's score to the file.
     */
    public static void saveScore(String username, int score, int total) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(SCORES_FILE, true))) {
            bw.write(username + "|" + score + "|" + total);
            bw.newLine();
        }
    }

    /**
     * Loads scores and returns a sorted list for the leaderboard.
     */
    public static List<String[]> loadScores() throws IOException {
        List<String[]> scores = new ArrayList<>();
        File file = new File(SCORES_FILE);
        if (!file.exists()) return scores;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 3) scores.add(parts);
            }
        }
        // Sort by score in descending order
        scores.sort((a, b) -> Integer.compare(Integer.parseInt(b[1]), Integer.parseInt(a[1])));
        return scores;
    }
}
