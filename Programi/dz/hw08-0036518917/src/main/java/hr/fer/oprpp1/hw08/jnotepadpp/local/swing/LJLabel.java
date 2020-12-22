package hr.fer.oprpp1.hw08.jnotepadpp.local.swing;

import javax.swing.JLabel;

import hr.fer.oprpp1.hw08.jnotepadpp.local.ILocalizationListener;
import hr.fer.oprpp1.hw08.jnotepadpp.local.ILocalizationProvider;

public class LJLabel extends JLabel {

	private static final long serialVersionUID = 1L;
	
	String key;
	
	private ILocalizationListener listener = new ILocalizationListener() {
		@Override
		public void localizationChanged() {
			updateLabel();
		}
	};
	
	private ILocalizationProvider lp;
	
	public LJLabel(String key, ILocalizationProvider provider) {
		this.key = key;
		this.lp = provider;
		
		lp.addLocalizationListener(listener);
		
		updateLabel();
	}
	
	private void updateLabel() {
		String translated = lp.getString(key);
		setText(translated);
	}
}
