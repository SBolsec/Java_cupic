package hr.fer.oprpp1.hw08.jnotepadpp.local.swing;

import javax.swing.AbstractAction;
import javax.swing.Action;

import hr.fer.oprpp1.hw08.jnotepadpp.local.ILocalizationListener;
import hr.fer.oprpp1.hw08.jnotepadpp.local.ILocalizationProvider;

/**
 * Action which changes name and description based on currently
 * selected language.
 * @author sbolsec
 *
 */
public abstract class LocalizableAction extends AbstractAction {
	/** Serial version UID **/
	private static final long serialVersionUID = 1L;
	
	/** Key for the name of the action **/
	private String name;
	/** Key for the description of the action **/
	private String desc;
	/** Localization provider which is used to get localized strings **/
	private ILocalizationProvider lp;
	
	/**
	 * Listener that changes the name and description of the action
	 * when the selected language changes.
	 */
	private ILocalizationListener listener = new ILocalizationListener() {
		@Override
		public void localizationChanged() {
			putValue(Action.NAME, lp.getString(name));
			putValue(Action.SHORT_DESCRIPTION, lp.getString(desc));
		}
	};
	
	/**
	 * Constructor which sets name and description of action.
	 * @param name key to be used for name by localization provider
	 * @param desc ket to be used for descrition by localization provider
	 * @param lp localization provider used to get localized strings
	 */
	public LocalizableAction(String name, String desc, ILocalizationProvider lp) {
		this.name = name;
		this.desc = desc;
		this.lp = lp;
		
		putValue(Action.NAME, lp.getString(name));
		putValue(Action.SHORT_DESCRIPTION, lp.getString(desc));
		
		lp.addLocalizationListener(listener);
	}
}
