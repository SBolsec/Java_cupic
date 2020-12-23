package hr.fer.oprpp1.hw08.jnotepadpp.local.swing;

import javax.swing.JLabel;

import hr.fer.oprpp1.hw08.jnotepadpp.local.ILocalizationListener;
import hr.fer.oprpp1.hw08.jnotepadpp.local.ILocalizationProvider;

/**
 * Displays length of document and supports localization.
 * @author sbolsec
 *
 */
public class LJStatusLength extends JLabel {
	/** Serial version UID **/
	private static final long serialVersionUID = 1L;
	
	/** Key to use for localization of length **/
	private String key;
	/** Number of characters in document **/
	private int value;
	/** Used to localize strings **/
	private ILocalizationProvider lp;
	
	/**
	 * Listener that listens for language changes.
	 */
	private ILocalizationListener listener = new ILocalizationListener() {
		@Override
		public void localizationChanged() {
			updateLabel();
		}
	};
	
	/**
	 * Constructor.
	 * @param lineKey key to use for localization of length
	 * @param colKey key to use for localization of column
	 * @param selKey key to use for localization of selection
	 * @param lp used to localize strings
	 */
	public LJStatusLength(String key,ILocalizationProvider lp) {
		super();
		this.key = key;
		this.value = 0;
		this.lp = lp;
		
		lp.addLocalizationListener(listener);
		
		updateLabel();
	}
	
	/**
	 * Updates the displayed text.
	 */
	private void updateLabel() {
		String l = lp.getString(key);
		
		String text = String.format("%s : %d", l, value);
		setText(text);
	}
	
	/**
	 * Sets the number of characters in document and updates the displayed text.
	 * @param line line number of the carret
	 * @param col column the carret is in
	 * @param sel size of selecion if one exsists
	 */
	public void setLength(int value) {
		this.value = value;
		
		updateLabel();
	}
}
