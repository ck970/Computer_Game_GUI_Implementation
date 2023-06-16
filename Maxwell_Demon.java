import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Maxwell extends JFrame implements ActionListener{

    // Maxwell also owns the VIEW objects -- displaying the particles
    JFrame gameWindow;
    JPanel topPanel;
    JPanel scorePanel;
    JPanel currentScore;
    JPanel highScore;
    JPanel buttonPanel;
    JLabel currentScoreLabel;
    JLabel highScoreLabel;
    JButton addParticlesButton;
    JButton resetGameButton;


    JPanel tempPanel;
    JPanel tempLeftPanel;
    JPanel tempRightPanel;
    JLabel tempLeft;
    JLabel tempRight;

    Game gamePanel;

    // Maxwell owns the MODEL objects -- the particles
    hotParticle[] hotParticles;
    coldParticle[] coldParticles;
    int hotParticlesCount;
    int coldParticlesCount;

    double delta = 0.05; // time step in seconds

    Timer tick;

    public static void main(String[] args) {
        Maxwell testMaxwellDemon = new Maxwell();
    }

    public Maxwell() {
        gameWindow = new JFrame();
        gameWindow.setTitle("Maxwell's Demon");
        gameWindow.setSize(550, 650);
        gameWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameWindow.setBackground(Color.white);

        tick = new Timer((int)100, this);
        tick.start();
        
        gamePanel = new Game();
        gameWindow.add(gamePanel);

        buttonPanel = new JPanel();
		buttonPanel.setBackground( Color.gray);
        addParticlesButton = new JButton("Add Particles");
        addParticlesButton.addActionListener(this);
        resetGameButton = new JButton("Reset Game");
        resetGameButton.addActionListener(this);
        buttonPanel.add(addParticlesButton);
        buttonPanel.add(resetGameButton);

        buttonPanel.setLayout( new GridLayout(1, 2) );
        buttonPanel.add(addParticlesButton, BorderLayout.WEST);
        buttonPanel.add(resetGameButton, BorderLayout.EAST);
        gameWindow.add(buttonPanel, BorderLayout.PAGE_START);

        tempPanel = new JPanel();
        tempPanel.setBackground( Color.gray);
        tempLeftPanel = new JPanel();
        tempLeftPanel.setBackground( Color.gray);
        tempRightPanel = new JPanel();
        tempRightPanel.setBackground( Color.gray);
        tempLeft = new JLabel("Temperature:");
        tempRight = new JLabel("Temperature:");
        tempLeftPanel.add(tempLeft);
        tempRightPanel.add(tempRight);
        tempPanel.add(tempLeftPanel);
        tempPanel.add(tempRightPanel);

        tempPanel.setLayout( new GridLayout(1, 2) );
        tempPanel.add(tempLeftPanel, BorderLayout.WEST);
        tempPanel.add(tempRightPanel, BorderLayout.EAST);
        gameWindow.add(tempPanel, BorderLayout.PAGE_END);

        // Initialize the model
        hotParticlesCount = 0;
        coldParticlesCount = 0;
        hotParticles = new hotParticle[1000];
        coldParticles = new coldParticle[1000];
        addParticles();

        gameWindow.addMouseListener(new MouseAdapter(){
            public void mousePressed(MouseEvent e) {
                gamePanel.addWall();
                gamePanel.repaint();
            }
            public void mouseReleased(MouseEvent e) {
                gamePanel.removeWall();
                gamePanel.repaint();
            }
        });

        // Create the play area

        gameWindow.setVisible(true);
    }
    // Particle is part of the MODEL -- an encapsulation of a single particle
    public class Particle {

        double x, y;
        double vx, vy;
        double oldx, oldy;

        public Particle() {
            x = (int) (Math.random() * 400 + 100); // [100, 500)
            y = (int) (Math.random() * 400 + 100);

            vx = (int) (Math.random() * 100 - 50); // [-50, 50)
            vy = (int) (Math.random() * 100 - 50);
        }

        public Particle(int xClick, int yClick) {
            x = xClick;
            y = yClick;

            vx = (int) (Math.random() * 100 - 50); // [-50, 50)
            vy = (int) (Math.random() * 100 - 50);
        }

        public void move(double delta) {
            oldx = x;
            oldy = y;
            x += vx * delta;
            y += vy * delta;
            stayOnScreen();
        }

        public double getX(){
            return x;
        }

        public void stayOnScreen() {
            // Check bounces off each edge of screen
            if (x < 8)
                vx *= -1;
            if (x > 530)
                vx *= -1;
            if (y < 8)
                vy *= -1;
            if (y > 555)
                vy *= -1;
            if (gamePanel.wallPresent) {
                if (x > 258 && x < 265){
                    vx*=-1;
                }
                else if (x > 272 && x < 279){
                    vx*=-1;
                }
            }
        }
    }

    public class hotParticle extends Particle {
        public hotParticle() {
            x = (int) (Math.random() * 400 + 100); // [100, 500)
            y = (int) (Math.random() * 400 + 100);

            vx = (int) (Math.random() * 25 + 140); // [140, 165)
            vy = (int) (Math.random() * 25 + 140);
        }

        public hotParticle(int xClick, int yClick) {
            x = xClick;
            y = yClick;

            vx = (int) (Math.random() * 25 + 140); // [140, 165)
            vy = (int) (Math.random() * 25 + 140);
        }

        public void drawMe(Graphics g) {
            g.setColor(Color.red);
            g.fillOval((int) (x - 2), (int) (y - 2), 5, 5);
        }
    }

    public class coldParticle extends Particle {
        public coldParticle() {
            x = (int) (Math.random() * 400 + 100); // [100, 500)
            y = (int) (Math.random() * 400 + 100);

            vx = (int) (Math.random() * 25 + 80); // [80, 115)
            vy = (int) (Math.random() * 25 + 80);
        }

        public coldParticle(int xClick, int yClick) {
            x = xClick;
            y = yClick;

            vx = (int) (Math.random() * 25 + 80); // [80, 115)
            vy = (int) (Math.random() * 25 + 80);
        }

        public void drawMe(Graphics g) {
            g.setColor(Color.blue);
            g.fillOval((int) (x - 2), (int) (y - 2), 5, 5);
        }
    }
    
    public void moveParticles(double delta) {
        for (int i = 0; i < hotParticlesCount; i++) {
            coldParticles[i].move(delta);
            hotParticles[i].move(delta);
        }
    }

    public void addParticles() {
        int xRight = (int) (Math.random() * 245 + 275); // [275, 520)
        int yRight = (int) (Math.random() * 515 + 5); // [5, 520)
        int xLeft = (int) (Math.random() * 240 + 5); // [5, 250)
        int yLeft = (int) (Math.random() * 515 + 5); // [5, 520)
        hotParticles[hotParticlesCount++] = new hotParticle(xRight, yRight);
        hotParticles[hotParticlesCount++] = new hotParticle(xLeft, yLeft);
        coldParticles[coldParticlesCount++] = new coldParticle(xRight, yRight);
        coldParticles[coldParticlesCount++] = new coldParticle(xLeft, yLeft);
    }

    public double[] getTemperature() {
        double[] temperature = new double[2];
        double leftTemp = 0;
        double rightTemp = 0;
        
        int coldLeftCount = 0;
        int coldRightCount = 0;
        int hotLeftCount = 0;
        int hotRightCount = 0;

        for (int i = 0; i < hotParticlesCount; i++){
            if (hotParticles[i].getX() < 267){
                hotLeftCount++;
            }
            else {
                hotRightCount++;
            }
        }
        for (int i = 0; i < coldParticlesCount; i++){
            if (coldParticles[i].getX() < 267){
                coldLeftCount++;
            }
            else {
                coldRightCount++;
            }
        }

        if (coldLeftCount == 0 && hotLeftCount == 0){
            leftTemp = 0;
        }
        else {
            // Calculate average squared velocity of particles in left chamber
            double leftVx = 0;
            for (int i = 0; i < coldParticlesCount; i++){
                if (coldParticles[i].getX() < 268){
                    leftVx += Math.pow(coldParticles[i].vx, 2);
                }
            }
            for (int i = 0; i < hotParticlesCount; i++){
                if (hotParticles[i].getX() < 268){
                    leftVx += Math.pow(hotParticles[i].vx, 2);
                }
            }
            leftVx /= (coldLeftCount + hotLeftCount);
            leftTemp = leftVx;
        }

        if (coldRightCount == 0 && hotRightCount == 0){
            rightTemp = 0;
        }
        else {
            // Calculate average squared velocity of particles in right chamber
            double rightVx = 0;
            for (int i = 0; i < coldParticlesCount; i++){
                if (coldParticles[i].getX() > 268){
                    rightVx += Math.pow(coldParticles[i].vx, 2);
                }
            }
            for (int i = 0; i < hotParticlesCount; i++){
                if (hotParticles[i].getX() > 268){
                    rightVx += Math.pow(hotParticles[i].vx, 2);
                }
            }
            rightVx /= (coldRightCount + hotRightCount);
            rightTemp = rightVx;
        }

        temperature[0] = leftTemp;
        temperature[1] = rightTemp;

        return temperature;
    }

    public void setTemperature(){
        double[] temperature = getTemperature();
        tempLeft.setText(String.valueOf(temperature[0] + "°F"));
        tempRight.setText(String.valueOf(temperature[1] + "°F"));
    }

    // // Game is part of the VIEW -- it displays the current particle positions
    public class Game extends JPanel {

        private boolean wallPresent = true;

        public void addWall() {
            wallPresent = true;
        }

        public void removeWall() {
            wallPresent = false;
        }

        public boolean wallPresent() {
            return wallPresent;
        }

        @Override
        public void paintComponent(Graphics g) {
                // Jpanel.paintComponent is too slow, so we just draw a white rectangle over everything
            super.paintComponent(g);
            g.setColor(Color.white);
            g.fillRect(0,0,550,565);
            for (int i = 0; i < hotParticlesCount; i++) {
                coldParticles[i].drawMe(g);
                hotParticles[i].drawMe(g);
            }
            if (wallPresent) {
                g.setColor(Color.black);
                // g.fillRect(265, 0, 8, 565);
                g.drawLine(269, 0, 269, 565);
                g.drawLine(269, 0, 269, 565);
                g.drawLine(269, 0, 269, 565);
            }
            else {
                g.setColor(Color.white);
                // g.fillRect(0,0,550,565);
                g.drawLine(269, 0, 269, 565);
                g.drawLine(269, 0, 269, 565);
                g.drawLine(269, 0, 269, 565);
            }
        }
    }

    // Animator is the CONTROLLER -- it responds to events
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == tick) {
            moveParticles(0.05);
            setTemperature();
            gamePanel.repaint();
        }
        else if (e.getSource() == resetGameButton){
            hotParticle[] delete_hot = new hotParticle[1000];
            coldParticle[] delete_cold = new coldParticle[1000];
            hotParticles = delete_hot;
            coldParticles = delete_cold;
            hotParticlesCount = 0;
            coldParticlesCount = 0;
            addParticles();
            gamePanel.addWall();
        }
        else if (e.getSource() == addParticlesButton){
            addParticles();
        }
        gamePanel.repaint();

    }

}
