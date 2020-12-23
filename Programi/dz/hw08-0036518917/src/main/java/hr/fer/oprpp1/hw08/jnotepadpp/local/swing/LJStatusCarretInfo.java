package hr.fer.oprpp1.hw08.jnotepadpp.local.swing;

import javax.swing.JLabel;

import hr.fer.oprpp1.hw08.jnotepadpp.local.ILocalizationListener;
import hr.fer.oprpp1.hw08.jnotepadpp.local.ILocalizationProvider;

/**
 * Displays current line, column and size of selection
 * in status bar and supports localization.
 * @author sbolsec
 *
 */
public class LJStatusCarretInfo extends JLabel {
	/** Serial version UID **/
	private static final long serialVersionUID = 1L;
	
	/** Key to use for localization of line **/
	private String lineKey;
	/** Key to use for localization of column **/
	private String colKey;
	/** Key to use for localization of selection **/
	private String selKey;
	/** Line number of the carret **/
	private int line;
	/** Column the carret is in **/
	private int col;
	/** Size of selecion if one exsists **/
	private int sel;
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
	 * @param lineKey key to use for localization of line
	 * @param colKey key to use for localization of column
	 * @param selKey key to use for localization of selection
	 * @param lp used to localize strings
	 */
	public LJStatusCarretInfo(String lineKey, String colKey, String selKey, ILocalizationProvider lp) {
		super();
		this.lineKey = lineKey;
		this.colKey = colKey;
		this.selKey = selKey;
		this.line = 0;
		this.col = 0;
		this.sel = 0;
		this.lp = lp;
		
		lp.addLocalizationListener(listener);
		
		updateLabel();
	}
	
	/**
	 * Updates the displayed text.
	 */
	private void updateLabel() {
		String l = lp.getString(lineKey);
		String c = lp.getString(colKey);
		String s = lp.getString(selKey);
		
		String text = String.format("%s : %d   %s : %d   %s : %d", l, line+1, c, col+1, s, sel);
		setText(text);
	}
	
	/**
	 * Sets the values to the provided ones and updates the displayed text.
	 * @param line line number of the carret
	 * @param col column the carret is in
	 * @param sel size of selecion if one exsists
	 */
	public void setValues(int line, int col, int sel) {
		this.line = line;
		this.col = col;
		this.sel = sel;
		
		updateLabel();
	}
}
