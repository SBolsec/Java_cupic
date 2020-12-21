package hr.fer.oprpp1.hw08.local.swing;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;

import hr.fer.oprpp1.hw08.local.ILocalizationProvider;
import hr.fer.oprpp1.hw08.local.LocalizationProviderBridge;

/**
 * Class which provides localization for <code>JFrame</code> elements.
 * @author sbolsec
 *
 */
public class FormLocalizationProvider extends LocalizationProviderBridge {

	/**
	 * Constructor which sets provider to be used as decorated object from which
	 * localized string will be returned and <code>JFrame</code> for which it
	 * registers as <code>WindowListener<code>
	 * @param provider provider to be used as decorated object
	 * @param frame frame for which it registers as <code>WindowListener</code>
	 */
	public FormLocalizationProvider(ILocalizationProvider provider, JFrame frame) {
		super(provider);
		
		WindowListener wl = new WindowAdapter() {

			@Override
			public void windowOpened(WindowEvent e) {
				connect();
			}

			@Override
			public void windowClosed(WindowEvent e) {
				disconnect();
			}
		};
		
		frame.addWindowListener(wl);
	}
}
