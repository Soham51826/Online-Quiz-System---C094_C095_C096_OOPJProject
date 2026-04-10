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

    private JProgressBar progressBar;

    public QuizFrame(QuizService service) {
        this.quizService = service;
        setTitle("Taking Quiz...");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(15, 15));

        JPanel topPanel = new JPanel(new GridLayout(2, 1));
        JPanel infoPanel = new JPanel(new BorderLayout());
        statusLabel = new JLabel("Question 1 / " + quizService.getTotalQuestions());
        timerLabel = new JLabel("Time Left: 10s", SwingConstants.RIGHT);
        timerLabel.setForeground(Color.RED);
        timerLabel.setFont(new Font("Arial", Font.BOLD, 14));
        infoPanel.add(statusLabel, BorderLayout.WEST);
        infoPanel.add(timerLabel, BorderLayout.EAST);
        
        progressBar = new JProgressBar(0, quizService.getTotalQuestions());
        progressBar.setValue(0);
        progressBar.setStringPainted(true);

        topPanel.add(infoPanel);
        topPanel.add(progressBar);
        add(topPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        questionLabel = new JLabel("Question Text");
        questionLabel.setFont(new Font("Arial", Font.BOLD, 18));
        questionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(questionLabel);
        centerPanel.add(Box.createVerticalStrut(30));

        optionsButtons = new JRadioButton[4];
        buttonGroup = new ButtonGroup();
        for (int i = 0; i < 4; i++) {
            optionsButtons[i] = new JRadioButton();
            optionsButtons[i].setFont(new Font("Arial", Font.PLAIN, 16));
            optionsButtons[i].setAlignmentX(Component.CENTER_ALIGNMENT);
            buttonGroup.add(optionsButtons[i]);
            centerPanel.add(optionsButtons[i]);
            centerPanel.add(Box.createVerticalStrut(10));
        }
        add(new JScrollPane(centerPanel), BorderLayout.CENTER);

        nextButton = new JButton("Next Question");
        nextButton.setFont(new Font("Arial", Font.BOLD, 14));
        nextButton.setPreferredSize(new Dimension(150, 40));
        JPanel southPanel = new JPanel();
        southPanel.add(nextButton);
        add(southPanel, BorderLayout.SOUTH);

        nextButton.addActionListener(e -> handleNext());

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                int confirm = JOptionPane.showConfirmDialog(QuizFrame.this, 
                    "Are you sure you want to exit the quiz? Your progress will be lost.", 
                    "Exit Quiz", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    if (timer != null) timer.stop();
                    new MenuFrame(quizService.getCurrentUser()).setVisible(true);
                    QuizFrame.this.dispose();
                }
            }
        });

        startTimer();
        displayNextQuestion();
    }

    private void startTimer() {
        if (timer != null && timer.isRunning()) timer.stop();
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
            questionLabel.setText("<html><body style='width: 400px; text-align: center;'>" + currentQuestion.getQuestionText() + "</body></html>");
            String[] options = currentQuestion.getOptions();
            for (int i = 0; i < 4; i++) {
                optionsButtons[i].setText(options[i]);
                optionsButtons[i].setSelected(false);
            }
            buttonGroup.clearSelection();
            
            int currentIndex = quizService.getCurrentIndex();
            statusLabel.setText("Question " + currentIndex + " / " + quizService.getTotalQuestions());
            progressBar.setValue(currentIndex - 1);
            
            timeLeft = 10;
            timerLabel.setText("Time Left: 10s");
            timer.start();
        } else {
            progressBar.setValue(quizService.getTotalQuestions());
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
