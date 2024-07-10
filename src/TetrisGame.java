import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.Random;
import javax.swing.*;

public class TetrisGame extends JPanel {
    private static final int VERTICAL = 20;
    private static final int SIDE = 10;
    private static final int SIZE = 30;
    private static final int INITIAL_SPEED = 500; // initial delay in milliseconds
    private static final int MIN_SPEED = 100; // minimum delay in milliseconds
    private static final int SPEED_INCREMENT = 5;

    private int[][] foundation = new int[VERTICAL + 2][SIDE + 2];
    private Mino currentMino, nextMino;
    private int rotation, y, x;
    private int speed;
    private Timer timer;
    private JLabel timerLabel;
    private JLabel scoreLabel;
    private JLabel nextMinoLabel;
    private JLabel warningLabel;
    private JLabel speedLabel;
    private long startTime;
    private int score;
    private boolean canSwap;

    public TetrisGame(JLabel timerLabel, JLabel scoreLabel, JLabel nextMinoLabel, JLabel warningLabel, JLabel speedLabel) {
        this.timerLabel = timerLabel;
        this.scoreLabel = scoreLabel;
        this.nextMinoLabel = nextMinoLabel;
        this.warningLabel = warningLabel;
        this.speedLabel = speedLabel;
        this.score = 0;
        this.speed = INITIAL_SPEED;
        this.canSwap = true;

        setPreferredSize(new Dimension(SIDE * SIZE, VERTICAL * SIZE));
        setBackground(Color.BLACK);

        for (int i = 0; i < VERTICAL + 2; i++) {
            foundation[i][0] = 8;
            foundation[i][SIDE + 1] = 8;
        }
        for (int i = 0; i < SIDE + 2; i++) {
            foundation[VERTICAL + 1][i] = 8;
        }

        timer = new Timer(speed, e -> {
            moveMino(1, 0, 0);
            repaint();
        });

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT:
                        moveMino(0, -1, 0);
                        break;
                    case KeyEvent.VK_RIGHT:
                        moveMino(0, 1, 0);
                        break;
                    case KeyEvent.VK_DOWN:
                        moveMino(1, 0, 0);
                        break;
                    case KeyEvent.VK_UP:
                        spinMino();
                        break;
                    case KeyEvent.VK_SPACE:
                        swapMino();
                        break;
                }
                repaint();
            }
        });
        setFocusable(true);
        initializeGame();
        timer.start();

        startTime = System.currentTimeMillis();
        Timer displayTimer = new Timer(1000, e -> updateTimer());
        displayTimer.start();
    }

    private void updateTimer() {
        long elapsed = (System.currentTimeMillis() - startTime) / 1000;
        timerLabel.setText("Time: " + elapsed + "s");
        if (elapsed % 10 == 0 && speed > MIN_SPEED) {
            speed -= SPEED_INCREMENT;
            timer.setDelay(speed);
        }
        int bpm = (60000 / speed); // Calculate blocks per minute
        speedLabel.setText("Speed: " + bpm + " BPM");
    }

    private void updateScore(int linesCleared) {
        score += linesCleared * 100;
        scoreLabel.setText("Score: " + score);
    }

    private void initializeGame() {
        score = 0;
        startTime = System.currentTimeMillis();
        speed = INITIAL_SPEED;
        timer.setDelay(speed);
        for (int i = 1; i <= VERTICAL; i++) {
            for (int j = 1; j <= SIDE; j++) {
                foundation[i][j] = 7;
            }
        }
        nextMino = generateMino();
        createMino();
    }

    private Mino generateMino() {
        Random rand = new Random();
        switch (rand.nextInt(7)) {
            case 0:
                return new IMino();
            case 1:
                return new JMino();
            case 2:
                return new LMino();
            case 3:
                return new OMino();
            case 4:
                return new SMino();
            case 5:
                return new TMino();
            case 6:
                return new ZMino();
            default:
                return new IMino();
        }
    }

    private void createMino() {
        currentMino = nextMino;
        nextMino = generateMino();
        rotation = 0;
        y = 0;
        x = 4;
        canSwap = true;
        updateNextMinoDisplay();
    }

    private void updateNextMinoDisplay() {
        nextMinoLabel.setText("");
        nextMinoLabel.repaint();
        ImageIcon icon = new ImageIcon(createImage(nextMino));
        nextMinoLabel.setIcon(icon);
        warningLabel.setText(canSwap ? "" : "Cannot swap!");
    }

    private Image createImage(Mino mino) {
        int[][] shape = mino.getShape(0);
        int size = 4;
        BufferedImage image = new BufferedImage(size * SIZE, size * SIZE, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();
        g.setColor(mino.getColor());
        int offsetX = (size - shape[0].length) * SIZE / 2;
        int offsetY = (size - shape.length) * SIZE / 2;
        for (int i = 0; i < shape.length; i++) {
            for (int j = 0; j < shape[i].length; j++) {
                if (shape[i][j] == 1) {
                    g.fillRect(offsetX + j * SIZE, offsetY + i * SIZE, SIZE, SIZE);
                    g.setColor(Color.BLACK);
                    g.drawRect(offsetX + j * SIZE, offsetY + i * SIZE, SIZE, SIZE);
                    g.setColor(mino.getColor());
                }
            }
        }
        g.dispose();
        return image;
    }

    private void drawMino(Graphics g) {
        int[][] shape = currentMino.getShape(rotation);
        g.setColor(currentMino.getColor());
        for (int i = 0; i < shape.length; i++) {
            for (int j = 0; j < shape[i].length; j++) {
                if (shape[i][j] == 1) {
                    g.fillRect((x + j) * SIZE, (y + i) * SIZE, SIZE, SIZE);
                    g.setColor(Color.BLACK);
                    g.drawRect((x + j) * SIZE, (y + i) * SIZE, SIZE, SIZE); // Draw outline
                    g.setColor(currentMino.getColor());
                }
            }
        }
    }

    private void drawFoundation(Graphics g) {
        for (int i = 0; i < VERTICAL; i++) {
            for (int j = 0; j < SIDE; j++) {
                if (foundation[i + 1][j + 1] != 7) {
                    g.setColor(new Color(Mino.COLORS[foundation[i + 1][j + 1]]));
                    g.fillRect(j * SIZE, i * SIZE, SIZE, SIZE);
                    g.setColor(Color.BLACK);
                    g.drawRect(j * SIZE, i * SIZE, SIZE, SIZE); // Draw outline
                }
            }
        }
    }

    private void moveMino(int dy, int dx, int dmode) {
        if (canMove(dy, dx, dmode)) {
            y += dy;
            x += dx;
            rotation = (rotation + dmode) % 4;
        } else if (dy == 1) {
            addToFoundation();
            createMino();
        }
    }

    private boolean canMove(int dy, int dx, int dmode) {
        int newRotation = (rotation + dmode) % 4;
        int[][] shape = currentMino.getShape(newRotation);
        for (int i = 0; i < shape.length; i++) {
            for (int j = 0; j < shape[i].length; j++) {
                if (shape[i][j] == 1) {
                    int ny = y + i + dy;
                    int nx = x + j + dx;
                    if (foundation[ny + 1][nx + 1] != 7) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private void spinMino() {
        moveMino(0, 0, 1);
    }

    private void swapMino() {
        if (canSwap) {
            Mino temp = currentMino;
            currentMino = nextMino;
            nextMino = temp;
            rotation = 0;
            y = 0;
            x = 4;
            canSwap = false;
            updateNextMinoDisplay();
        }
    }

    private void addToFoundation() {
        int[][] shape = currentMino.getShape(rotation);
        for (int i = 0; i < shape.length; i++) {
            for (int j = 0; j < shape[i].length; j++) {
                if (shape[i][j] == 1) {
                    foundation[y + i + 1][x + j + 1] = currentMino.getForm();
                }
            }
        }
        int linesCleared = clearLines();
        updateScore(linesCleared);
        if (isGameOver()) {
            timer.stop();
            showGameOverDialog();
        }
    }

    private int clearLines() {
        int linesCleared = 0;
        for (int i = 1; i <= VERTICAL; i++) {
            boolean full = true;
            for (int j = 1; j <= SIDE; j++) {
                if (foundation[i][j] == 7) {
                    full = false;
                    break;
                }
            }
            if (full) {
                linesCleared++;
                for (int k = i; k > 1; k--) {
                    for (int j = 1; j <= SIDE; j++) {
                        foundation[k][j] = foundation[k - 1][j];
                    }
                }
                for (int j = 1; j <= SIDE; j++) {
                    foundation[1][j] = 7;
                }
            }
        }
        return linesCleared;
    }

    private boolean isGameOver() {
        for (int i = 1; i <= SIDE; i++) {
            if (foundation[1][i] != 7) {
                return true;
            }
        }
        return false;
    }

    private void showGameOverDialog() {
        int option = JOptionPane.showOptionDialog(this, "GAME OVER\nScore: " + score, "Game Over",
                JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null,
                new String[]{"Restart", "Main Menu", "Quit"}, "Restart");
        switch (option) {
            case JOptionPane.YES_OPTION:
                restartGame();
                break;
            case JOptionPane.NO_OPTION:
                showMainMenu();
                break;
            case JOptionPane.CANCEL_OPTION:
                System.exit(0);
                break;
        }
    }

    private void restartGame() {
        initializeGame();
        timer.start();
    }

    private void showMainMenu() {
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
        frame.getContentPane().removeAll();
        frame.getContentPane().add(new MainMenuPanel(frame, INITIAL_SPEED));
        frame.pack();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawFoundation(g);
        drawMino(g);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Tetris");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        MainMenuPanel mainMenuPanel = new MainMenuPanel(frame, INITIAL_SPEED);
        frame.getContentPane().add(mainMenuPanel);

        frame.pack();
        frame.setVisible(true);
    }
}

class MainMenuPanel extends JPanel {
    private static final int SIZE = 30;

    public MainMenuPanel(JFrame frame, int initialSpeed) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.BLACK);

        JLabel titleLabel = new JLabel("Tetris");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton startButton = new JButton("Start Game");
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        startButton.addActionListener(e -> {
            frame.getContentPane().removeAll();
            JLabel timerLabel = new JLabel("Time: 0s");
            JLabel scoreLabel = new JLabel("Score: 0");
            JLabel nextMinoLabel = new JLabel();
            JLabel warningLabel = new JLabel("", SwingConstants.CENTER);
            JLabel speedLabel = new JLabel("Speed: " + "BPM", SwingConstants.CENTER);
            warningLabel.setForeground(Color.RED);
            speedLabel.setForeground(Color.WHITE);

            timerLabel.setForeground(Color.WHITE);
            scoreLabel.setForeground(Color.WHITE);
            timerLabel.setOpaque(true);
            scoreLabel.setOpaque(true);
            timerLabel.setBackground(Color.BLACK);
            scoreLabel.setBackground(Color.BLACK);

            TetrisGame gamePanel = new TetrisGame(timerLabel, scoreLabel, nextMinoLabel, warningLabel, speedLabel);

            JPanel nextMinoPanel = new JPanel(new BorderLayout());
            nextMinoPanel.add(new JLabel("Next Mino:", SwingConstants.CENTER), BorderLayout.NORTH);
            nextMinoPanel.add(nextMinoLabel, BorderLayout.CENTER);
            nextMinoPanel.add(warningLabel, BorderLayout.SOUTH);
            nextMinoPanel.setBackground(Color.BLACK);
            nextMinoPanel.setBorder(BorderFactory.createLineBorder(Color.WHITE));
            nextMinoPanel.setPreferredSize(new Dimension(4 * SIZE, 4 * SIZE));

            JPanel speedPanel = new JPanel(new BorderLayout());
            speedPanel.add(speedLabel, BorderLayout.CENTER);
            speedPanel.setBackground(Color.BLACK);

            JPanel sidePanel = new JPanel();
            sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS));
            sidePanel.setBackground(Color.BLACK);
            sidePanel.add(nextMinoPanel);
            sidePanel.add(speedPanel);

            frame.getContentPane().add(gamePanel, BorderLayout.CENTER);
            frame.getContentPane().add(timerLabel, BorderLayout.NORTH);
            frame.getContentPane().add(scoreLabel, BorderLayout.SOUTH);
            frame.getContentPane().add(sidePanel, BorderLayout.EAST);
            frame.pack();
            gamePanel.requestFocusInWindow();
        });

        JButton controlsButton = new JButton("Controls");
        controlsButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        controlsButton.addActionListener(e -> JOptionPane.showMessageDialog(this,
                "Controls:\n" +
                        "Left Arrow: Move Left\n" +
                        "Right Arrow: Move Right\n" +
                        "Down Arrow: Move Down\n" +
                        "Up Arrow: Rotate\n" +
                        "Space: Swap Mino"));

        JButton quitButton = new JButton("Quit");
        quitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        quitButton.addActionListener(e -> System.exit(0));

        add(Box.createRigidArea(new Dimension(0, 50)));
        add(titleLabel);
        add(Box.createRigidArea(new Dimension(0, 20)));
        add(startButton);
        add(Box.createRigidArea(new Dimension(0, 20)));
        add(controlsButton);
        add(Box.createRigidArea(new Dimension(0, 20)));
        add(quitButton);
    }
}
