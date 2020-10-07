package hr.fer.zemris.java.hw11.jnotepadpp.local;

/**
 * Functional interface for a localization listener used in JNotepad++.
 * 
 * @author Jura Milić
 *
 */
public interface ILocalizationListener {
	/**
	 * This method should be executed whenever localization changes.
	 */
	void localizationChanged();
}
