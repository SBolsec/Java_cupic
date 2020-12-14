package hr.fer.zemris.tecaj.swing.p09;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

public class Prozor2 extends JFrame {

	private static final long serialVersionUID = 1L;

	public Prozor2() {
		super();
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setTitle("Prozor2");
		setLocation(20, 20);
		setSize(500, 200);
		JPanel p = new JPanel();
		p.setBorder(BorderFactory.createLineBorder(Color.BLUE, 50));
		setContentPane(p);
		initGUI();
	}
	
	private void initGUI() {
		Container cp = getContentPane();
		cp.setLayout(new OdozgoPremaDoljeLayoutManager());

		JLabel labela = new JLabel("Ovo je tekst!");
		JButton button = new JButton("Stisni me još jednom!");

		cp.add(labela);
		cp.add(button);
		cp.add(new JButton("Jos jedan"));
		cp.add(new JButton("drugi"));
		
		Dimension dim = button.getPreferredSize();
		
		labela.setBounds(10, 10, 100, 30);
		button.setBounds(10, 50, dim.width, dim.height);		
	}
	
	private static class OdozgoPremaDoljeLayoutManager implements LayoutManager {

		private int gap = 2;
		
		@Override
		public void addLayoutComponent(String name, Component comp) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void removeLayoutComponent(Component comp) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public Dimension preferredLayoutSize(Container parent) {
			return new Dimension(10,10);
		}

		@Override
		public Dimension minimumLayoutSize(Container parent) {
			return new Dimension(10,10);
		}

		@Override
		public void layoutContainer(Container parent) {
			Insets ins = parent.getInsets();
			System.out.println("Pozivam layoutContainer");
			int w = parent.getWidth() - ins.left - ins.right;
			int y = ins.top;
			for (Component c : parent.getComponents()) {
				Dimension dim = c.getPreferredSize();
				c.setBounds(ins.left, y, w, dim.height);
				
				y += dim.height + gap;
			}
		}
		
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				Prozor2 prozor = new Prozor2();
				prozor.setVisible(true);
			}
		});
	}
}
