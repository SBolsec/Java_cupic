package hr.fer.zemris.tecaj.swing.p04;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

public class Prozor1 extends JFrame {

	private static final long serialVersionUID = 1L;

	public Prozor1() {
		super();
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setTitle("Prozor1");
		setLocation(20, 20);
		setSize(500, 200);
		initGUI();
		
	}
	
	private void initGUI() {
		// isključi automatsko pozicioniranje komponenata po površini...
		getContentPane().setLayout(null);
		
		JComponent komponenta1 = new JComponent() {
			private static final long serialVersionUID = 1L;
		
			@Override
			protected void paintComponent(Graphics g) {
				int w = getWidth();
				int h = getHeight();
				if (isOpaque()) {
					g.setColor(getBackground());
					g.fillRect(0, 0, w, h);
				}
				
				Insets ins = getInsets();
				w -= ins.left + ins.right;
				h -= ins.top + ins.bottom;
				g.setColor(getForeground());
				g.fillRect(ins.left + w/4, ins.top + h/4, w/2, h/2);
			}
		};
		komponenta1.setLocation(10, 10);
		komponenta1.setSize(100, 40);
		komponenta1.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
		komponenta1.setOpaque(true);
		komponenta1.setBackground(Color.GREEN);
		komponenta1.setForeground(Color.BLUE);
		
		getContentPane().add(komponenta1);
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				Prozor1 prozor = new Prozor1();
				prozor.setVisible(true);
			}
		});
	}
}
