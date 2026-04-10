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
     * Saves a user's score to the file with a timestamp.
     */
    public static void saveScore(String username, int score, int total) throws IOException {
        String timestamp = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date());
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(SCORES_FILE, true))) {
            bw.write(username + "|" + score + "|" + total + "|" + timestamp);
            bw.newLine();
        }
    }

    /**
     * Loads scores and returns a sorted list for the leaderboard.
     * Score format: username|score|total|timestamp
     */
    public static List<String[]> loadScores() throws IOException {
        List<String[]> scores = new ArrayList<>();
        File file = new File(SCORES_FILE);
        if (!file.exists()) return scores;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length >= 3) {
                    scores.add(parts);
                }
            }
        }
        // Sort by score in descending order, handle possible number format issues
        scores.sort((a, b) -> {
            try {
                int scoreA = Integer.parseInt(a[1]);
                int scoreB = Integer.parseInt(b[1]);
                return Integer.compare(scoreB, scoreA);
            } catch (NumberFormatException e) {
                return 0;
            }
        });
        return scores;
    }

    /**
     * Returns the best score for a specific user.
     * @return int array [maxScore, totalAtMax] or null if no attempts
     */
    public static int[] getUserBestScore(String username) throws IOException {
        List<String[]> allScores = loadScores();
        int maxScore = -1;
        int totalAtMax = 0;
        for (String[] s : allScores) {
            if (s[0].equalsIgnoreCase(username)) {
                try {
                    int sc = Integer.parseInt(s[1]);
                    if (sc > maxScore) {
                        maxScore = sc;
                        totalAtMax = Integer.parseInt(s[2]);
                    }
                } catch (NumberFormatException ignored) {}
            }
        }
        return (maxScore != -1) ? new int[]{maxScore, totalAtMax} : null;
    }

    /**
     * Clears all scores for a specific user.
     */
    public static void clearUserHistory(String username) throws IOException {
        List<String[]> allScores = new ArrayList<>();
        File file = new File(SCORES_FILE);
        if (!file.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length >= 1 && !parts[0].equalsIgnoreCase(username)) {
                    allScores.add(parts);
                }
            }
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(SCORES_FILE))) {
            for (String[] s : allScores) {
                bw.write(String.join("|", s));
                bw.newLine();
            }
        }
    }

    /**
     * Overwrites the questions file with a given list of questions.
     */
    public static void overwriteQuestions(List<Question> questions) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(QUESTIONS_FILE))) {
            for (Question q : questions) {
                bw.write(q.toString());
                bw.newLine();
            }
        }
    }

    /**
     * Loads all unique categories from the questions file.
     */
    public static Set<String> getCategories() throws IOException {
        Set<String> categories = new TreeSet<>();
        List<Question> questions = loadQuestions();
        for (Question q : questions) {
            categories.add(q.getCategory());
        }
        return categories;
    }
}
