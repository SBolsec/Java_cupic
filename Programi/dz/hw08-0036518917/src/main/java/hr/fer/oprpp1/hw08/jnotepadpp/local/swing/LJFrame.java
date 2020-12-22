package hr.fer.oprpp1.hw08.jnotepadpp.local.swing;

import javax.swing.JFrame;

import hr.fer.oprpp1.hw08.jnotepadpp.local.LocalizationProvider;


/**
 * Extends <code>JFrame</code> and adds a <code>FormLocalizationProvider</code>.
 * When the frame is created, <code>FormLocalizationProvider</code> will register
 * itself to decorated localization provider automatically; when frame closes,
 * <code>FormLocalizationProvider</code> will de-register itself from the decorated
 * localization provider automatically so that it won't hold any reference to it
 * and the garbage collector will be able to free frame and all of its resources.
 * @author sbolsec
 *
 */
public class LJFrame extends JFrame {
	/** Serial version UID **/
	private static final long serialVersionUID = 1L;
	/** Provides localization for this <code>JFrame</code> **/
	private FormLocalizationProvider flp;
	
	/**
	 * Constructor which initializes <code>FormLocalizationProvider</code>.
	 */
	public LJFrame() {
		super();
		flp = new FormLocalizationProvider(LocalizationProvider.getInstance(), this);
	}
	
	public FormLocalizationProvider getFormLocalizationProvider() {
		return flp;
	}
}
