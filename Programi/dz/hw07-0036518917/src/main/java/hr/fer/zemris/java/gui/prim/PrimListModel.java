package hr.fer.zemris.java.gui.prim;

import java.util.ArrayList;
import java.util.List;

import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

/**
 * List of prim numbers.
 * @author sbolsec
 *
 */
public class PrimListModel implements ListModel<Integer> {
	/** Calculated data **/
	private List<Integer> data = new ArrayList<>();
	/** Listeners that are attached to this model **/
	private List<ListDataListener> listeners = new ArrayList<>();
	
	/**
	 * Constructor.
	 * Adds first prim number.
	 */
	public PrimListModel() {
		data.add(1);
	}
	
	/**
	 * Calculates next prim number and informs listeners.
	 */
	public void next() {
		int n = data.get(data.size()-1) + 1;
		
		while (true) {
			boolean prim = true;
			for (int i = 2; i <= Math.sqrt(n); i++) {
				if (n % i == 0) {
					prim = false;
					break;
				}
			}
			if (prim)
				break;
			else
				n++;
		}
		
		data.add(n);
		
		ListDataEvent e = new ListDataEvent(this, ListDataEvent.INTERVAL_ADDED, data.size()-1, data.size());
		for (ListDataListener l : listeners) {
			l.intervalAdded(e);
		}
	}
	
	@Override
	public int getSize() {
		return data.size();
	}

	@Override
	public Integer getElementAt(int index) {
		return data.get(index);
	}

	@Override
	public void addListDataListener(ListDataListener l) {
		listeners.add(l);
	}

	@Override
	public void removeListDataListener(ListDataListener l) {
		listeners.remove(l);
	}

}
