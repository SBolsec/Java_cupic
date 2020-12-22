package hr.fer.oprpp1.hw08.jnotepadpp.local;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Singleton class which extends <code>AbstractLocalizationProvider</code> and
 * adds ability to get localized strings. The language can be changed, but only
 * one instance of this class will exsist.
 * @author sbolsec
 *
 */
public class LocalizationProvider extends AbstractLocalizationProvider {
	/** Language of current localization **/
	private String language;
	/** Resource bundle of current localization **/
	private ResourceBundle bundle;
	/** Instance of this class **/
	private static final LocalizationProvider instance = new LocalizationProvider();
	
	/**
	 * Constructor which sets initial setting.
	 */
	private LocalizationProvider() {
		this.language = "en";
		Locale locale = Locale.forLanguageTag(language);
		this.bundle = ResourceBundle.getBundle("prijevodi", locale);
	}
	
	/**
	 * Returns instance of this class.
	 * @return instance of this class
	 */
	public static LocalizationProvider getInstance() {
		return instance;
	}
	
	/**
	 * Sets the language to the provided one
	 * @param language sets new language
	 */
	public void setLanguage(String language) {
		this.language = language;
		Locale locale = Locale.forLanguageTag(language);
		this.bundle = ResourceBundle.getBundle("prijevodi", locale);
		fire();
	}
	
	@Override
	public String getString(String key) {
		return bundle.getString(key);
	}

	@Override
	public String getCurrentLanguage() {
		return language;
	}

}
