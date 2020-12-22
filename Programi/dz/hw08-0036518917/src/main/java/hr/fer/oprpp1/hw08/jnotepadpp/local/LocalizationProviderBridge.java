package hr.fer.oprpp1.hw08.jnotepadpp.local;

/**
 * Decorator for some <code>ILocalizationProvider</code>. 
 * This class manages a connection to the decorated object and uses
 * it to return localized strings.
 * @author sbolsec
 *
 */
public class LocalizationProviderBridge extends AbstractLocalizationProvider {
	/** Status whether anonymous listener is registered to decorated object (parent) **/
	private boolean connected;
	/** Decorated object whihc is used to return localized strings **/
	private ILocalizationProvider parent;
	/** Listener used to register or de-register from decorated object **/
	private ILocalizationListener listener = new ILocalizationListener() {
		@Override
		public void localizationChanged() {
			fire();
		}
	};
	
	/**
	 * Constructos which sets decorated object.
	 * @param parent decorated object which will be used to localize strings
	 */
	public LocalizationProviderBridge(ILocalizationProvider parent) {
		this.parent = parent;
	}
	
	/**
	 * Registers listener to decorated object.
	 */
	public void connect() {
		if (connected) return;
		
		parent.addLocalizationListener(listener);
		this.connected = true;
	}
	
	/**
	 * De-registers listener from decorated object.
	 */
	public void disconnect() {
		if (!connected) return;
		
		parent.removeLocalizationListener(listener);
		this.connected = false;
	}
	
	@Override
	public String getString(String key) {
		return parent.getString(key);
	}

	@Override
	public String getCurrentLanguage() {
		return parent.getCurrentLanguage();
	}

}
