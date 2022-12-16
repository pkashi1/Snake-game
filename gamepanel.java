import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.Graphics;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.Timer;

import java.util.*;//for random function
// import java.util.Timer;

/**
 * gamepanel
 */
public class gamepanel extends JPanel implements ActionListener {

    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 50;
    static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE;
    static final int DELAY = 75; // higher the delay slower the game
    final int[] arrx = new int[GAME_UNITS];
    final int[] arry = new int[GAME_UNITS];
    int bodyParts = 6;
    int applesEaten = 0; // initial apples eaten is 0
    int appleX;//
    int appleY;
    char direction = 'R';
    boolean running = false;
    Timer timer;
    Random rand;

    gamepanel() {
        rand = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
    }

    /**
     * 
     */
    public void startGame() {
        newapple();
        running = true;
        timer = new Timer(DELAY, this);
        timer.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        if (running) {

            for (int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++) {
                g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
                g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);
            }
            g.setColor(Color.red);
            g.fillOval(appleX, appleY, UNIT_SIZE, HEIGHT);// Size of the apple
            for (int i = 0; i < bodyParts; i++) {
                if (i == 0) {
                    g.setColor(Color.green);
                    g.fillRect(arrx[i], arry[i], UNIT_SIZE, UNIT_SIZE);
                } else {
                    g.setColor(new Color(45, 180, 0));
                    g.fillRect(arrx[i], arry[i], UNIT_SIZE, UNIT_SIZE);
                }

            }
            g.setColor(Color.red);
            g.setFont(new Font("Ink Free", Font.BOLD, 75));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: " + applesEaten)) / 2,
                    g.getFont().getSize());
        } else {
            gameOver(g);
        }

    }

    public void newapple() {
        appleX = rand.nextInt((int) (SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
        appleY = rand.nextInt((int) (SCREEN_HEIGHT / UNIT_SIZE)) * UNIT_SIZE;
    }

    public void move() {
        for (int i = bodyParts; i > 0; i--) {
            arrx[i] = arrx[i - 1];
            arry[i] = arry[i - 1];
        }

        switch (direction) {
            case 'u':
                arry[0] = arry[0] - UNIT_SIZE;
                break;
            case 'D':
                arry[0] = arry[0] + UNIT_SIZE;
                break;
            case 'L':
                arrx[0] = arrx[0] - UNIT_SIZE;
                break;
            case 'R':
                arrx[0] = arrx[0] + UNIT_SIZE;
                break;

            default:
                break;
        }
    }

    public void checkApple() {
        if ((arrx[0] == appleX) && (arry[0] == appleY)) {
            bodyParts++;
            applesEaten++;
            newapple();
        }
    }

    public void checkCollisions() {
        // checks if head collides with body
        for (int i = bodyParts; i > 0; i--) {
            if ((arrx[0] == arrx[i]) && (arry[0] == arry[i])) {
                running = false;
            }
            // check if head touches left border
            if (arrx[0] < 0) {
                running = false;
            }
            // check if head touches right border
            if (arrx[0] > SCREEN_WIDTH) {
                running = false;
            }
            // check if head touches top border
            if (arry[0] < 0) {
                running = false;
            }
            // check if head touches bottom border
            if (arry[0] > SCREEN_HEIGHT) {
                running = false;
            }
            if (!running) {
                timer.stop();

            }
        }
    }

    public void gameOver(Graphics g) {
        // Score
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 75));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: " + applesEaten)) / 2,
                g.getFont().getSize());
        // Game over text
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 75));
        metrics = getFontMetrics(g.getFont());
        g.drawString("GAME OVER", (SCREEN_WIDTH - metrics.stringWidth("GAME OVER")) / 2, SCREEN_HEIGHT / 2);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
        if (running) {
            move();
            checkApple();
            checkCollisions();
        }
        repaint();
    }

    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if (direction != 'R') {
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if (direction != 'L') {
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if (direction != 'D') {
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if (direction != 'U') {
                        direction = 'D';
                    }
                    break;

                default:
                    break;
            }

        }
    }

}