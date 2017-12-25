package main;

import javax.swing.JFrame;

/**
 * 
 * @author Benzolamps
 *
 */
public class MainFrame extends JFrame {

	private static final long serialVersionUID = -3249573337883474642L;

	DrawPanel draw = new DrawPanel();

	public MainFrame() {
		super("¶íÂÞË¹·½¿é");
		setResizable(false);
		setSize(600, 442);
		addKeyListener(draw);
		getContentPane().add(draw);
		setVisible(true);
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		MainFrame.setDefaultLookAndFeelDecorated(true);
		new MainFrame().setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
