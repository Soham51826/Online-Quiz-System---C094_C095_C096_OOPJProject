import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The Quiz Interface with Timer.
 */
public class QuizFrame extends JFrame {
    private QuizService quizService;
    private Question currentQuestion;
    private JLabel questionLabel, timerLabel, statusLabel;
    private JRadioButton[] optionsButtons;
    private ButtonGroup buttonGroup;
    private JButton nextButton;
    private Timer timer;
    private int timeLeft = 10;

    public QuizFrame(QuizService service) {
        this.quizService = service;
        setTitle("Taking Quiz...");
        setSize(600, 450);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel topPanel = new JPanel(new BorderLayout());
        statusLabel = new JLabel("Question 1 / " + quizService.getTotalQuestions());
        timerLabel = new JLabel("Time Left: 10s", SwingConstants.RIGHT);
        timerLabel.setForeground(Color.RED);
        timerLabel.setFont(new Font("Arial", Font.BOLD, 14));
        topPanel.add(statusLabel, BorderLayout.WEST);
        topPanel.add(timerLabel, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        questionLabel = new JLabel("Question Text");
        questionLabel.setFont(new Font("Arial", Font.BOLD, 16));
        centerPanel.add(Box.createVerticalStrut(20));
        centerPanel.add(questionLabel);
        centerPanel.add(Box.createVerticalStrut(20));

        optionsButtons = new JRadioButton[4];
        buttonGroup = new ButtonGroup();
        for (int i = 0; i < 4; i++) {
            optionsButtons[i] = new JRadioButton();
            buttonGroup.add(optionsButtons[i]);
            centerPanel.add(optionsButtons[i]);
        }
        add(centerPanel, BorderLayout.CENTER);

        nextButton = new JButton("Next");
        add(nextButton, BorderLayout.SOUTH);

        nextButton.addActionListener(e -> handleNext());

        startTimer();
        displayNextQuestion();
    }

    private void startTimer() {
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timeLeft--;
                timerLabel.setText("Time Left: " + timeLeft + "s");
                if (timeLeft <= 0) {
                    timer.stop();
                    handleNext(); // Auto-next if time runs out
                }
            }
        });
    }

    private void displayNextQuestion() {
        if (quizService.hasMoreQuestions()) {
            currentQuestion = quizService.getNextQuestion();
            questionLabel.setText("Q: " + currentQuestion.getQuestionText());
            String[] options = currentQuestion.getOptions();
            for (int i = 0; i < 4; i++) {
                optionsButtons[i].setText(options[i]);
                optionsButtons[i].setSelected(false);
            }
            buttonGroup.clearSelection();
            statusLabel.setText("Question " + quizService.getCurrentIndex() + " / " + quizService.getTotalQuestions());
            timeLeft = 10;
            timerLabel.setText("Time Left: 10s");
            timer.start();
        } else {
            showResult();
        }
    }

    private void handleNext() {
        timer.stop();
        int selected = -1;
        for (int i = 0; i < 4; i++) {
            if (optionsButtons[i].isSelected()) {
                selected = i;
                break;
            }
        }
        quizService.submitAnswer(selected, currentQuestion.getCorrectOptionIndex());
        displayNextQuestion();
    }

    private void showResult() {
        new ResultFrame(quizService).setVisible(true);
        this.dispose();
    }
}
