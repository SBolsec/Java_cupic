package hr.fer.oprpp1.hw08.jnotepadpp.local.swing;

import javax.swing.AbstractAction;
import javax.swing.Action;

import hr.fer.oprpp1.hw08.jnotepadpp.local.ILocalizationListener;
import hr.fer.oprpp1.hw08.jnotepadpp.local.ILocalizationProvider;


public abstract class LocalizableAction extends AbstractAction {
	
	private static final long serialVersionUID = 1L;
	
	private String key;
	
	private ILocalizationListener listener = new ILocalizationListener() {
		@Override
		public void localizationChanged() {
			String translated = lp.getString(key);
			putValue(Action.NAME, translated);
		}
	};
	
	private ILocalizationProvider lp;
	
	public LocalizableAction(String key, ILocalizationProvider lp) {
		this.key = key;
		this.lp = lp;
		
		String translated = lp.getString(key);
		putValue(Action.NAME, translated);
		
		lp.addLocalizationListener(listener);
	}
}
