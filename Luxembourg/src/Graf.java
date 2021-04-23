import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Graf {
	private static void initUI() {
		JFrame f = new JFrame("Harta Luxembourg");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.add(new MyPanel());
		f.setSize(850, 850);
		f.setBackground(Color.GRAY);
		f.setVisible(true);
	
	}

	public static void main(String[] args) {

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				initUI();
			}
		});
	}
}
