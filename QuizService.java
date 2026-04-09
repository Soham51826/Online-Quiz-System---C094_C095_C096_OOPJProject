import java.util.*;

/**
 * Manages the logic of the Quiz session.
 */
public class QuizService {
    private List<Question> questions;
    private int currentQuestionIndex;
    private int score;
    private User currentUser;

    public QuizService(User user, List<Question> questions) {
        this.currentUser = user;
        this.questions = new ArrayList<>(questions);
        Collections.shuffle(this.questions); // Shuffle questions
        this.currentQuestionIndex = 0;
        this.score = 0;
    }

    public boolean hasMoreQuestions() {
        return currentQuestionIndex < questions.size();
    }

    public Question getNextQuestion() {
        if (hasMoreQuestions()) {
            return questions.get(currentQuestionIndex++);
        }
        return null;
    }

    public void submitAnswer(int selectedIndex, int correctIndex) {
        if (selectedIndex == correctIndex) {
            score++;
        }
    }

    public int getScore() {
        return score;
    }

    public int getTotalQuestions() {
        return questions.size();
    }

    public double getPercentage() {
        if (questions.isEmpty()) return 0;
        return (double) score / questions.size() * 100;
    }

    public User getCurrentUser() {
        return currentUser;
    }
    
    public int getCurrentIndex() {
        return currentQuestionIndex;
    }
}
