package primjer1;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.WindowConstants;

public class MojProzor extends JFrame {

	private static final long serialVersionUID = -215553492241421368L;

	public MojProzor() {
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		setTitle("Demonstracija centriranja!");
		setSize(300, 300);

		WindowListener wl = new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.out.println("Neko me zatvara!");
				boolean imaLiNesnimljeno = true; // umjesto poziva metode

				if (!imaLiNesnimljeno) {
					dispose();
					return;
				}
				
				String[] opcije = new String[] {"Hoću", "Neću", "Otkaži zatvaranje"};
				int rezultat = JOptionPane.showOptionDialog(MojProzor.this,
						"Posotoji nesmiljeni sadržaj. Želite li ga snimiti?", "Upozorenje!",
						JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
						null, opcije, opcije[0]);
				switch(rezultat) {
				case JOptionPane.CLOSED_OPTION:
					return;
				case 0:
					// save();
					System.out.println("Snimam");
					dispose();
					return;
				case 1:
					dispose();
					return;
				case 2:
					return;
				}

//				int rezultat = JOptionPane.showConfirmDialog(MojProzor.this,
//						"Posotoji nesmiljeni sadržaj. Želite li ga snimiti?", "Upozorenje!",
//						JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE);
//				switch (rezultat) {
//				case JOptionPane.CANCEL_OPTION:
//					return;
//				case JOptionPane.CANCEL_OPTION:
//					return;
//				case JOptionPane.YES_OPTION:
//					// save();
//					System.out.println("Snimam");
//					dispose();
//					return;
//				case JOptionPane.NO_OPTION:
//					dispose();
//					return;
//				}
			}

		};
		this.addWindowListener(wl);

		initGUI();

		// pack();
		// getContentPane().getLayout().preferredLayoutSize(getContentPane());

		// Ovo centrira prozor ali prije toga mora biti poziv metode setSize()
		setLocationRelativeTo(null);
	}

	private void initGUI() {
		
		Action a = new AbstractAction() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Neko me stisnuo!");
			}
		};
		a.putValue(Action.NAME, "Stisni me s jednog mjesta!");
		a.setEnabled(false);
		
		JButton b = new JButton(a);
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(b, BorderLayout.CENTER);
		
		JMenuBar jmb = new JMenuBar();
		this.setJMenuBar(jmb);
		JMenu gumbi = new JMenu("Gumbi");
		jmb.add(gumbi);
		JMenuItem it = new JMenuItem(a);
		//JMenuItem it = new JMenuItem("Stisni me");
		gumbi.add(it);		
		
		
		
//		ActionListener al = new ActionListener() {
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				System.out.println("Neko me stisnuo!");
//			}
//		};
//		b.addActionListener(al);
//		it.addActionListener(al);
//		
//		b.setEnabled(false);
//		it.setEnabled(false);
		
		Timer timer = new Timer(5000, e-> {
			a.setEnabled(!a.isEnabled());
		});
		timer.setRepeats(true);
		timer.start();
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new MojProzor().setVisible(true));
	}
}
