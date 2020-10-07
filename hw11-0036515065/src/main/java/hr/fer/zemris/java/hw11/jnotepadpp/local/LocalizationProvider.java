package hr.fer.zemris.java.hw11.jnotepadpp.local;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * A simpleton class for providing localization to it's listeners.
 * 
 * @author Jura Milić
 *
 */
public class LocalizationProvider extends AbstractLocalizationProvider {
	/**
	 * Default language of provider
	 */
	private static final String DEFAULT_LANGUAGE = "en";
	/**
	 * List of currently implemented languages.
	 */
	private List<String> legalLanguages;
	/**
	 * Current language
	 */
	private String language;
	/**
	 * Current bundle
	 */
	private ResourceBundle bundle;
	/**
	 * The static reference to the one and only instance of LocalizationProvider.
	 */
	private static LocalizationProvider provider;

	/**
	 * Constructs LocalizationProvider.
	 */
	private LocalizationProvider() {
		legalLanguages = new ArrayList<String>();
		legalLanguages.add("en");
		legalLanguages.add("hr");
		legalLanguages.add("de");

		language = DEFAULT_LANGUAGE;
		Locale locale = Locale.forLanguageTag(language);
		bundle = ResourceBundle.getBundle("hr.fer.zemris.java.hw11.jnotepadpp.local.translations", locale);
	}

	/**
	 * @return the instance of LocalizationProvider
	 */
	public static LocalizationProvider getInstance() {
		if (provider == null) {
			provider = new LocalizationProvider();
		}

		return provider;
	}

	/**
	 * Sets the languages of the provider and changes the resource bundle.
	 * 
	 * @param lang given language
	 */
	public void setLanguage(String lang) {
		for (String language : legalLanguages) {
			if (language.equals(lang)) {
				this.language = lang;
				Locale locale = Locale.forLanguageTag(language);
				bundle = ResourceBundle.getBundle("hr.fer.zemris.java.hw11.jnotepadpp.local.translations", locale);
				fire();
				return;
			}
		}

		throw new IllegalArgumentException("Invalid language was given");
	}

	@Override
	public String getString(String key) {
		return bundle.getString(key);
	}

	@Override
	public String getLanguage() {
		return language;
	}
}
