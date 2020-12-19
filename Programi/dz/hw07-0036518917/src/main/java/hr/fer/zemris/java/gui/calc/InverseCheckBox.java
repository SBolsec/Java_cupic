package hr.fer.zemris.java.gui.calc;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JCheckBox;

/**
 * Check box which inverts the state of all the components
 * that are in its list of listeners.
 * @author sbolsec
 *
 */
public class InverseCheckBox extends JCheckBox {

	/** Generated serial version UID **/
	private static final long serialVersionUID = 7952893386476862646L;
	
	/** Listeners that listen to this check box **/
	private List<ActionListener> listeners = new ArrayList<>();
	
	@Override
	public void addActionListener(ActionListener l) {
		listeners.add(l);
	}

	@Override
	public void removeActionListener(ActionListener l) {
		listeners.remove(l);
	}
	
	/**
	 * Informs all the listeners that there was a change
	 */
	public void informListeners() {
		if (listeners != null) {
			for (ActionListener l : listeners) {
				l.actionPerformed(null);
			}
		}
	}
}
