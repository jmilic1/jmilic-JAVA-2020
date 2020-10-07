package hr.fer.zemris.java.hw11.jnotepadpp.local;

import javax.swing.AbstractAction;

/**
 * Class which inherits from AbstractAction and allows localization
 * compatibility.
 * 
 * @author Jura Milić
 *
 */
public abstract class LocalizableAction extends AbstractAction {
	private static final long serialVersionUID = 1L;
	/**
	 * Associated key
	 */
	private String key;
	/**
	 * Associated listener
	 */
	private ILocalizationListener listener;
	/**
	 * Provider of localization
	 */
	private ILocalizationProvider provider;

	/**
	 * Constructs a LocalizableAction with given key and localization provider. A
	 * listener is added to the provider so the action can change it's name whenever
	 * localization changes.
	 * 
	 * @param key given key
	 * @param lp  given provider
	 */
	public LocalizableAction(String key, ILocalizationProvider lp) {
		this.key = key;
		String value = lp.getString(this.key);
		putValue(NAME, value);

		provider = lp;
		listener = () -> {
			String translation = provider.getString(this.key);
			putValue(NAME, translation);
		};
		provider.addLocalizationListener(listener);
	}
}
