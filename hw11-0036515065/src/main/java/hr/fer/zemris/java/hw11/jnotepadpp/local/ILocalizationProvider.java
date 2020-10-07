package hr.fer.zemris.java.hw11.jnotepadpp.local;

/**
 * Interface for a Localization provider used in JNotepad++.
 * 
 * @author Jura Milić
 *
 */
public interface ILocalizationProvider {
	/**
	 * Returns the translation of given key in current language
	 * 
	 * @param key given key
	 * @return translation
	 */
	String getString(String key);

	/**
	 * Register given listener to this localization provider.
	 * 
	 * @param l given listener
	 */
	void addLocalizationListener(ILocalizationListener l);

	/**
	 * Unregister a given listener from this localization provider
	 * 
	 * @param l given listener
	 */
	void removeLocalizationListener(ILocalizationListener l);

	/**
	 * @return the current language
	 */
	public String getLanguage();
}
