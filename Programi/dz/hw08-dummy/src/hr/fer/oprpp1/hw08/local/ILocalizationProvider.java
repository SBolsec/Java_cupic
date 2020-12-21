package hr.fer.oprpp1.hw08.local;

/**
 * Interface which allow localization by returning localized strings
 * based on given keys. This interface acts as a subject in the 
 * Observer pattern which informs all of its listeners.
 * @author sbolsec
 *
 */
public interface ILocalizationProvider {
	/**
	 * Adds given <code>ILocalizationListener</code> to list of listeners.
	 * @param listener listener to be added to list of listeners
	 */
	void addLocalizationListener(ILocalizationListener listener);
	
	/**
	 * Removes given <code>ILocalizationListener</code> from list of listeners.
	 * @param listener listener to be removed from list of listeners
	 */
	void removeLocalizationListener(ILocalizationListener listener);
	
	/**
	 * Returns localized string based on given key.
	 * @param key key for which to find localized string
	 * @return localized string for given key
	 */
	String getString(String key);
}
