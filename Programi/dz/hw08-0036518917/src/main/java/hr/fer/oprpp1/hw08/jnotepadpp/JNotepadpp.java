package hr.fer.oprpp1.hw08.jnotepadpp;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

/**
 * Notepad like application.
 * @author sbolsec
 *
 */
public class JNotepadpp extends JFrame {

	/** Generated serial version UID **/
	private static final long serialVersionUID = 2115546573199433581L;
	
	
	
	/**
	 * Constructor.
	 */
	public JNotepadpp() {
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		setLocation(0,0);
		setSize(600,600);
		setTitle("JNotepad++");
		
		initGUI();
	}
	
	/**
	 * Initializes the GUI.
	 */
	private void initGUI() {
		
	}
	
	/**
	 * Starting point of program
	 * @param args command line arguments // not used
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new JNotepadpp().setVisible(true));
	}
}
