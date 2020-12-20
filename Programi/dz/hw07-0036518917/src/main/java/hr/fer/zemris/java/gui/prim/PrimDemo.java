package hr.fer.zemris.java.gui.prim;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
/**
 * Creates a window with two lists of prim numbers.
 * @author sbolsec
 *
 */
public class PrimDemo extends JFrame {

	/** Generated serial version UID **/
	private static final long serialVersionUID = -1305147476023758530L;

	/**
	 * Constructor.
	 */
	public PrimDemo() {
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setTitle("PrimDemo");
		setSize(400, 400);
		initGUI();
	}
	
	/**
	 * Initializes the GUI.
	 */
	private void initGUI() {
		Container cp = getContentPane();
		cp.setLayout(new BorderLayout());

		PrimListModel model = new PrimListModel();
		
		JList<Integer> list1 = new JList<>(model);
		JList<Integer> list2 = new JList<>(model);
		list1.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		list2.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		
		JPanel p = new JPanel(new GridLayout(1, 0));
		p.add(new JScrollPane(list1));
		p.add(new JScrollPane(list2));
		cp.add(p, BorderLayout.CENTER);
		
		JButton btn = new JButton("Sljedeći");
		btn.addActionListener(l -> model.next());
		cp.add(btn, BorderLayout.SOUTH);
	}
	
	/**
	 * Starting point of program.
	 * @param args command line arguments // not used
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new PrimDemo().setVisible(true));
	}
}
