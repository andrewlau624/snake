import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;

import javax.swing.JPanel;

public class GamePanel extends JPanel implements ActionListener{
	
	static final int SCREEN_WIDTH = 1000;
	static final int SCREEN_HEIGHT = 1000;
	static final int UNIT_SIZE = 25;
	static final int GAME_UNITS = (SCREEN_WIDTH*SCREEN_HEIGHT)/UNIT_SIZE;
	static final int DELAY = 50;
	int x[] = new int[GAME_UNITS];
	int y[] = new int[GAME_UNITS];
	int bodyParts = 2;
	int applesEaten;
	int appleX;
	int appleY;
	int mostEaten = 0;
	char dir = 'R';
	boolean running = false;
	Timer timer;
	Random random;
	
	GamePanel(){
		random = new Random();
		this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
		this.setBackground(Color.black);
		this.setFocusable(true);
		this.addKeyListener(new MyKeyAdapter());
		startGame();
	}
	
	public void startGame() {	
		applesEaten = 0;
		bodyParts = 2;
		dir = 'R';
		running = true;
		x = new int[GAME_UNITS];
		y = new int[GAME_UNITS];
		
		newApple();
		running = true;
		timer = new Timer(DELAY, this);
		timer.start();
	}
		
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
	}
	
	public void draw(Graphics g) {
		for(int i=0; i<SCREEN_HEIGHT/UNIT_SIZE; i++) {
			g.setColor(new Color(15,15,15));
			g.drawLine(i*UNIT_SIZE, 0, i*UNIT_SIZE, SCREEN_HEIGHT);
			g.drawLine(0, i*UNIT_SIZE, SCREEN_WIDTH, i*UNIT_SIZE);
		}
		
		g.setColor(Color.white);
		g.setFont(new Font( "SansSerif", Font.PLAIN, 18 ));
		g.drawString(""+applesEaten,SCREEN_WIDTH/2,SCREEN_HEIGHT/20);
		
		g.setColor(Color.white);
		g.setFont(new Font( "SansSerif", Font.PLAIN, 18 ));
		FontMetrics metrics = getFontMetrics(g.getFont());
		g.drawString("High Score:" + mostEaten,(SCREEN_WIDTH - metrics.stringWidth("High Score"))/2,SCREEN_HEIGHT/10);

		
		g.setColor(Color.red);
		g.fillRect(appleX, appleY, UNIT_SIZE, UNIT_SIZE);	
		
		for(int i=0; i<bodyParts; i++) {
			if(i==0) {
				g.setColor(Color.green);
				g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
			} else {
				if(i%2 == 0) {
				g.setColor(new Color(45,180,0));
				g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				} else {
					g.setColor(new Color(40,150,0));
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				}
			}
		}
	}
	
	public void newApple() {
		appleX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
		appleY = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;
	}
	
	public void move() {
		for(int i = bodyParts;i>0;i--) {
			x[i] = x[i-1];
			y[i] = y[i-1];
		}
			
			switch(dir) {
			case 'U':
				y[0] = y[0] - UNIT_SIZE;
				break;
			case 'D':
				y[0] = y[0] + UNIT_SIZE;
				break;
			case 'L':
				x[0] = x[0] - UNIT_SIZE;
				break;
			case 'R':
				x[0] = x[0] + UNIT_SIZE;
				break;		
			}
	}
	
	public void checkApple() {
		if((x[0] == appleX) && (y[0] == appleY)) {
			bodyParts++;
			applesEaten++;
			
			if(applesEaten > mostEaten) {
				mostEaten = applesEaten;
			}
			
			newApple();
		}
	}
	
	public void checkCollisions() {
		//Body Collision
		for(int i=bodyParts; i>0; i--) {
			if((x[0] == x[i]) && (y[0] == y[i])) {
				running = false;
			}
		}
		//Border
		if(x[0] < 0) {
			running = false;
		}
		if(y[0] < 0) {
			running = false;
		}
		if(x[0] > SCREEN_WIDTH) {
			running = false;
		}
		if(y[0] > SCREEN_HEIGHT) {
			running = false;
		}
		
		if(!running) {
			timer.stop();			
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(running) {
			move();
			checkApple();
			checkCollisions();
		}
		repaint();
		if(!running) startGame();
	}

	public class MyKeyAdapter extends KeyAdapter{
		@Override
		public void keyPressed(KeyEvent e) {
			switch(e.getKeyCode()) {
			case KeyEvent.VK_D:
				if(dir != 'L') {
					dir = 'R';
				}
				break;
			case KeyEvent.VK_A:
				if(dir != 'R') {
					dir = 'L';
				}
				break;
			case KeyEvent.VK_W:
				if(dir != 'D') {
					dir = 'U';
				}
				break;
			case KeyEvent.VK_S:
				if(dir != 'U') {
					dir = 'D';
				}
				break;
			}
		}
	}
}
