package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Ellipse2D;

import javax.swing.JPanel;
import javax.swing.Timer;

import shape.Block;
import shape.Trix;

/**
 * 
 * @author Benzolamps
 *
 */
public class DrawPanel extends JPanel implements KeyListener, MouseListener {
	private static final long serialVersionUID = -8812523143503801243L;
	
	private Timer timer1 = new Timer(1000, new MyAction1());
	private Timer timer2 = new Timer(70, new MyAction2());
	private Trix trix = Trix.ProduceOneTrix();
	private Trix nextTrix = Trix.ProduceOneTrix();
	private Block[][] blockBorder = new Block[17][27];
	private Block[][] blockNext = new Block[6][8];
	private Block[][] blockScore = new Block[16][8];
	private Block[][] blockCount = new Block[16][19];
	private Block[][] blockExtra = new Block[6][19];
	private Ellipse2D.Float[] e = new Ellipse2D.Float[4];
	private Color[] c1 = new Color[4], c2 = new Color[4];
	private boolean gameOver = false;
	private boolean paused = false;
	private long score = 0;
	private int scoreAddition = 0;
	private long combo = 0;
	private int h = -2;

	private class MyAction1 implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			if (!canMove(trix, 2)) {
				fall();
				int count = clear();
				if (count != 0) {
					combo += count;
					scoreAddition = (int) (Math.pow(2, count - 1) + (combo == count ? 0 : combo));
					score += scoreAddition;
				} else {
					combo = 0;
					scoreAddition = 0;
				}

				if (gameOver)
					return;
				trix = nextTrix.clone();
				nextTrix = Trix.ProduceOneTrix();
			} else {
				for (int i = 0; i < 4; i++) {
					trix.getBlock()[i].setN(trix.getBlock()[i].getN() + 1);
				}
			}
		}
	}

	private class MyAction2 implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			repaint();
			if (h == 0 && canMove(trix.rotate(), h)) {
				trix = trix.rotate();
			}

			if (Math.abs(h) == 1 && canMove(trix, h)) {
				for (Block b : trix.getBlock())
					b.setM(b.getM() + h);
			}
		}
	}

	public DrawPanel() {
		setLayout(null);
		for (int i = 0; i < 4; i++) {
			c1[i] = new Color(255 - 50 * i, 215, 235);
			e[i] = new Ellipse2D.Float(275, 165 + 65 * i, 50, 25);
			c2[i] = new Color(255 - 50 * i, 20, 235);
		}
		addMouseListener(this);
		initial();
		initialInfo();
		timer1.start();
		timer2.start();
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponents(g);
		Graphics2D g2D = (Graphics2D) g;
		g2D.setPaint(Color.WHITE);
		g2D.setFont(new Font("Consolas", Font.BOLD, 24));
		g2D.fillRect(0, 0, getWidth(), getHeight());
		drawFallingBlock(g2D);
		if (gameOver || paused) {
			Block[][] block = new Block[17][27];
			for (int i = 0; i < 17; i++) {
				for (int j = 0; j < 27; j++) {
					block[i][j] = (Block) blockBorder[i][j].clone();
					if (block[i][j].getColor() != Color.WHITE) {
						block[i][j].setColor(Color.BLACK);
					}
					if (j > 23 && j < 26 && i > 0 && i < 16) {
						block[i][j].setColor(Color.WHITE);
					}
				}
			}
			drawOneBlock(g2D, block);
			String str = null;
			if (paused)
				str = "PAUSED";
			if (gameOver)
				str = "GAME OVER";
			g2D.setPaint(new Color(255, 0, 128));
			g2D.drawString(str, 20, 385);
		} else
			drawOneBlock(g2D, blockBorder);
		drawOneBlock(g2D, blockNext);
		drawOneBlock(g2D, blockScore);
		drawOneBlock(g2D, blockCount);
		drawOneBlock(g2D, blockExtra);
		drawNextBlock(g2D);
		drawScore(g2D);
		drawCount(g2D);
		drawExtra(g2D);
	}

	private void drawFallingBlock(Graphics2D g2D) {
		g2D.setPaint(trix.getBlock()[0].getColor());
		for (int i = 0; i < 4; i++) {
			int x = trix.getBlock()[i].x;
			int y = trix.getBlock()[i].y;
			int width = trix.getBlock()[i].width;
			int height = trix.getBlock()[i].height;
			g2D.fillRoundRect(x, y, width, height, width, height);
		}
	}

	private void drawNextBlock(Graphics2D g2D) {
		g2D.setPaint(nextTrix.getBlock()[0].getColor());
		for (int i = 0; i < 4; i++) {
			int x = nextTrix.getBlock()[i].x + 180;
			int y = nextTrix.getBlock()[i].y + 75;
			int width = nextTrix.getBlock()[i].width;
			int height = nextTrix.getBlock()[i].height;
			g2D.fillRoundRect(x, y, width, height, width, height);
		}
		g2D.drawString("NEXT", 273, 32);
	}

	private void drawScore(Graphics2D g2D) {
		g2D.setPaint(new Color(255, 0, 128));
		g2D.drawString("SCORE    " + (scoreAddition == 0 ? "" : "+" + scoreAddition), 363, 32);
		g2D.drawString(String.format("%12d", score), 398, 100);
	}

	private void drawCount(Graphics2D g2D) {
		Trix[] t = Trix.getAllTrix();
		int[] c = Trix.getAllCount();

		for (int i = 0; i < t.length; i++) {
			g2D.setPaint(t[i].getBlock()[0].getColor());
			for (Block b : t[i].getBlock()) {
				int height = 10, width = 10;
				int x = (b.x + 460) * 2 / 3;
				int y = (b.y + i * 50 + 250) * 2 / 3;
				g2D.fillRoundRect(x, y, width, height, width, height);
			}
			g2D.drawString(String.format("%12d", c[i]), 398, i * 33 + 185);
		}
	}

	private void drawExtra(Graphics2D g2D) {
		String[] s = { "P", "R", "I", "X" };
		for (int i = 0; i < 4; i++) {
			g2D.setPaint(c1[i]);
			g2D.fill(e[i]);
			g2D.setPaint(c2[i]);
			g2D.drawString(s[i], 292, 185 + 65 * i);
		}
	}

	private void drawOneBlock(Graphics2D g2D, Block[][] block) {
		for (int i = 0; i < block.length; i++) {
			for (int j = 0; j < block[0].length; j++) {
				Color color = block[i][j].getColor();
				if (!color.equals(Color.WHITE)) {
					g2D.setPaint(block[i][j].getColor());
					int x = block[i][j].x;
					int y = block[i][j].y;
					int width = block[i][j].width;
					int height = block[i][j].height;
					g2D.fillRoundRect(x, y, width, height, width, height);
				}
			}
		}
	}

	private void initial() {
		initOneBlock(blockBorder, 0, 0);
		timer1.start();
		timer2.start();
	}

	private void initialInfo() {
		initOneBlock(blockNext, 17, 0);
		initOneBlock(blockScore, 23, 0);
		initOneBlock(blockCount, 23, 8);
		initOneBlock(blockExtra, 17, 8);
	}

	private void initOneBlock(Block[][] block, int x, int y) {
		for (int i = x; i < x + block.length; i++) {
			for (int j = y; j < y + block[i - x].length; j++) {
				block[i - x][j - y] = new Block(i, j, Color.WHITE);
			}
		}
		for (int i = 0; i < block.length; i++) {
			block[i][0].setColor(Color.BLACK);
			block[i][block[0].length - 1].setColor(Color.BLACK);
		}
		for (int i = 0; i < block[0].length; i++) {
			block[0][i].setColor(Color.BLACK);
			block[block.length - 1][i].setColor(Color.BLACK);
		}
	}

	private boolean canMove(Trix trix, int q) {
		for (int i = 0; i < trix.getBlock().length; i++) {
			int m = trix.getBlock()[i].getM();
			int n = trix.getBlock()[i].getN();
			if (q != 2 && (m == 0 - q || m == 16 - q))
				return false;
			for (int j = 0; j < 17; j++) {
				for (int k = 1; k < 27; k++) {
					if (!(blockBorder[j][k].getColor().equals(Color.WHITE))) {
						if (q == 2 && m == j && n == k - 1)
							return false;
						if (q != 2 && m == j - q && n == k)
							return false;
					}
				}
			}
		}
		return true;
	}

	private void fall() {
		for (Block b : trix.getBlock()) {
			int x = b.getM();
			int y = b.getN();
			if (y > 0)
				blockBorder[x][y].setColor(b.getColor());
			else {
				System.out.println("Game Over");
				gameOver = true;
				timer1.stop();
				return;
			}
		}
	}

	private int clear() {
		int count = 0, flag = 1;
		for (int i = 1; i < 26; i++) {
			flag = 1;
			for (int j = 0; j < 17; j++) {
				if (blockBorder[j][i].getColor().equals(Color.WHITE)) {
					flag = 0;
					break;
				}
			}
			if (flag == 1) {
				count++;
				for (int j = 0; j < 17; j++) {
					for (int k = i; k > 1; k--) {
						blockBorder[j][k].setColor(blockBorder[j][k - 1].getColor());
					}
				}
			}
		}
		return count;
	}

	public void pause() {
		if (gameOver)
			return;
		if (timer1.isRunning()) {
			timer1.stop();
			paused = true;
		} else {
			timer1.start();
			paused = false;
		}
	}

	public void restart() {
		Trix.countToZero();
		gameOver = paused = false;
		score = combo = scoreAddition = 0;
		h = -2;
		initial();
		trix = Trix.ProduceOneTrix();
		nextTrix = Trix.ProduceOneTrix();
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub
		if (gameOver || paused)
			return;
		switch (arg0.getKeyCode()) {
		case KeyEvent.VK_SPACE:
		case KeyEvent.VK_ENTER:
			while (canMove(trix, 2)) {
				for (Block b : trix.getBlock()) {
					b.setN(b.getN() + 1);
				}
			}
			fall();
			int count = clear();
			if (count != 0) {
				combo += count;
				scoreAddition = (int) (Math.pow(2, count - 1) + (combo == count ? 0 : combo));
				score += scoreAddition;
			} else {
				combo = 0;
				scoreAddition = 0;
			}
			if (gameOver)
				break;
			trix = nextTrix.clone();
			nextTrix = Trix.ProduceOneTrix();
			break;

		case KeyEvent.VK_UP:
		case KeyEvent.VK_W:
			h = 0;
			break;

		case KeyEvent.VK_DOWN:
		case KeyEvent.VK_S:
			timer1.setDelay(20);
			timer1.setInitialDelay(20);
			if (timer1.isRunning())
				timer1.stop();
			timer1.start();
			break;

		case KeyEvent.VK_LEFT:
		case KeyEvent.VK_A:
			h = -1;
			break;

		case KeyEvent.VK_RIGHT:
		case KeyEvent.VK_D:
			h = 1;
			break;
		}
		repaint();
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		h = -2;
		switch (arg0.getKeyCode()) {
		case KeyEvent.VK_DOWN:
		case KeyEvent.VK_S:
			if (!paused) {
				timer1.setDelay(1000);
				timer1.setInitialDelay(1000);
				if (timer1.isRunning())
					timer1.stop();
				timer1.start();
			}
			break;

		case KeyEvent.VK_P:
			pause();
			return;

		case KeyEvent.VK_R:
			restart();
			return;
		}
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		for (int i = 0; i < 4; i++) {
			System.out.println(arg0.getX() + " " + arg0.getY());
			if (e[i].contains(arg0.getX(), arg0.getY())) {
				Color temp = c1[i];
				c1[i] = c2[i];
				c2[i] = temp;
				break;
			}
		}
		if (e[0].contains(arg0.getX(), arg0.getY()))
			pause();
		if (e[1].contains(arg0.getX(), arg0.getY()))
			restart();
		if (e[2].contains(arg0.getX(), arg0.getY()))
			;
		if (e[3].contains(arg0.getX(), arg0.getY()))
			System.exit(0);
		repaint();
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
	}
}