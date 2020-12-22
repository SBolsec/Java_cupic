package hr.fer.oprpp1.hw08.jnotepadpp.local;

import java.util.ArrayList;
import java.util.List;

/**
 * Implements <code>ILocalizationProvider</code> and adds it the
 * ability to register, de-register and inform listeners. It does
 * not implement getString() method.
 * @author sbolsec
 *
 */
public abstract class AbstractLocalizationProvider implements ILocalizationProvider {
	/** List of registered listeners **/
	private List<ILocalizationListener> listeners;
	
	/**
	 * Constructor.
	 */
	public AbstractLocalizationProvider() {
		listeners = new ArrayList<>();
	}
	
	@Override
	public void addLocalizationListener(ILocalizationListener listener) {
		listeners.add(listener);
	}

	@Override
	public void removeLocalizationListener(ILocalizationListener listener) {
		listeners.remove(listener);
	}
	
	/**
	 * Informs all the listeners that the language has changed.
	 */
	public void fire() {
		for (ILocalizationListener listener : listeners) {
			listener.localizationChanged();
		}
	}

}
