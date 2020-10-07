package hr.fer.zemris.java.hw11.jnotepadpp.local;

import javax.swing.JMenu;

/**
 * A Swing Component which inherits from JMenu and also allows localization
 * compatibility.
 * 
 * @author Jura Milić
 *
 */
public class LJMenu extends JMenu {
	private static final long serialVersionUID = 1L;
	/**
	 * Associated key for this LJMenu.
	 */
	private String key;
	/**
	 * Listener which changes the text of this LJMenu.
	 */
	private ILocalizationListener listener;
	/**
	 * The localization provider
	 */
	private ILocalizationProvider provider;

	/**
	 * Constructs a LJMenu with given key and provider. A new listener is added to
	 * the provider so this LJMenu can change it's text whenever localization
	 * changes.
	 * 
	 * @param key given key
	 * @param lp  given provider
	 */
	public LJMenu(String key, ILocalizationProvider lp) {
		this.key = key;
		String value = lp.getString(this.key);
		setText(value);

		provider = lp;
		listener = () -> {
			String translation = provider.getString(this.key);
			setText(translation);
		};
		provider.addLocalizationListener(listener);
	}
}
