package hr.fer.oprpp1.hw08.jnotepadpp.tutorials;

import java.awt.GridLayout;
import java.awt.event.KeyEvent;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

public class TabbedPane extends JFrame {

	public TabbedPane() {
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setSize(600,600);
		setTitle("Tabbed pane demo");
		
		initGUI();
	}
	
	private void initGUI() {
//		getContentPane().setLayout(new GridLayout(1,1));
		
		JTabbedPane tabbedPane = new JTabbedPane();
		
		JComponent panel1 = makeTextPanel("Panel #1");
		tabbedPane.addTab("Tab 1", null, panel1, "Haja");
		tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);
	
		JComponent panel2 = makeTextPanel("Panel #2");
		tabbedPane.addTab("Tab 2", panel2);
		
		JComponent panel3 = makeTextPanel("Panel #3");
		tabbedPane.addTab("Tab 3", panel3);
		
		JComponent panel4 = makeTextPanel("Panel #4");
		tabbedPane.addTab("Tab 4", panel4);
		
		add(tabbedPane);
		tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
	}
	
	protected JComponent makeTextPanel(String text) {
		JPanel panel = new JPanel(false);
		JLabel filler = new JLabel(text);
		filler.setHorizontalAlignment(JLabel.CENTER);
		panel.setLayout(new GridLayout(1, 1));
		panel.add(filler);
		return panel;
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			new TabbedPane().setVisible(true);
		});
	}
}
