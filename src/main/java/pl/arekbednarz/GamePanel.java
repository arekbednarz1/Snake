package pl.arekbednarz;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.*;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {

    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 25;
    static final int GAME_UNITS = (SCREEN_WIDTH*SCREEN_HEIGHT)/UNIT_SIZE;
    static final int DELAY = 75;
    final int x[] = new int[GAME_UNITS];
    final int y[] = new int[GAME_UNITS];
    int bodyParts = 6;
    int points;
    int pointX;
    int pointy;
    char direction = 'R';
    boolean running = false;
    Timer timer;
    Random random;

    GamePanel(){
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
    }
    public void startGame(){
        newBall();
        running= true;
        timer= new Timer(DELAY, this);
        timer.start();
    }
    public void paintComponent(Graphics graphics){
        super.paintComponent(graphics);
        draw(graphics);
    }
    public void draw(Graphics graphics){
        if (running) {
            for (int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++) {
                graphics.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
                graphics.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);
            }
            graphics.setColor(Color.red);
            graphics.fillOval(pointX, pointy, UNIT_SIZE, UNIT_SIZE);

            for (int i = 0; i < bodyParts; i++) {
                if (i == 0) {
                    graphics.setColor(Color.GREEN);
                    graphics.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                } else {
                    graphics.setColor(new Color(45, 180, 0));
                    graphics.setColor(new Color(random.nextInt(255),random.nextInt(255),random.nextInt(255)));
                    graphics.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
            }
            graphics.setColor(Color.RED);
            graphics.setFont(new Font("Ink Free",Font.BOLD,40));
            FontMetrics fontMetrics = getFontMetrics(graphics.getFont());
            graphics.drawString("SCORE: "+points,(SCREEN_WIDTH - fontMetrics.stringWidth("SCORE: "+points))/2,graphics.getFont().getSize());
        }else gameOver(graphics);
    }
    public void newBall(){
        pointX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
        pointy = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;

    }
    public void move(){
        for (int i = bodyParts; i >0 ; i--) {
            x[i]=x[i-1];
            y[i]=y[i-1];
        }
        switch (direction){
            case 'U':
                y[0] = y[0] - UNIT_SIZE;
                break;
            case 'D' :
                y[0] = y[0] + UNIT_SIZE;
                break;
            case 'L' :
                x[0] = x[0] - UNIT_SIZE;
                break;
            case 'R' :
                x[0] = x[0] + UNIT_SIZE;
                break;
        }
    }
    public void checkBall(){
        if((x[0] == pointX) && (y[0] == pointy)){
            bodyParts++;
            points++;
            newBall();
        }
    }
    public void checkCollisions(){
        for (int i = bodyParts; i >0 ; i--) {
            if ((x[0]==x[i]) &&(y[0]==y[i])){
                running = false;
            }
        }
        if (x[0]<0){
            running = false;
        }
        if (x[0]>SCREEN_WIDTH){
            running = false;
        }
        if (y[0]<0){
            running = false;
        }
        if (y[0]>SCREEN_HEIGHT){
            running = false;
        }
        if (!running){
            timer.stop();
        }
    }
    public void gameOver(Graphics graphics){
        graphics.setColor(Color.RED);
        graphics.setFont(new Font("Ink Free",Font.BOLD,40));
        FontMetrics fontMetrics1 = getFontMetrics(graphics.getFont());
        graphics.drawString("SCORE: "+points,(SCREEN_WIDTH - fontMetrics1.stringWidth("SCORE: "+points))/2,graphics.getFont().getSize());
        graphics.setColor(Color.BLUE);
        graphics.setFont(new Font("Ink Free",Font.BOLD,75));
        FontMetrics fontMetrics = getFontMetrics(graphics.getFont());
        graphics.drawString("GAME OVER",(SCREEN_WIDTH - fontMetrics.stringWidth("GAME OVER"))/2,SCREEN_HEIGHT/2);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running){
            move();
            checkBall();
            checkCollisions();
        }
        repaint();
    }

    public class MyKeyAdapter extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent event){
            switch(event.getKeyCode()){
                case KeyEvent.VK_LEFT :
                    if (direction != 'R'){
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT :
                    if (direction != 'L'){
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_UP :
                    if (direction != 'D'){
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if (direction != 'U'){
                        direction = 'D';
                    }
                    break;
            }
        }
    }
}
