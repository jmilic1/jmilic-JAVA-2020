package hr.fer.zemris.java.hw11.jnotepadpp.local;

/**
 * A bridge for LocalizationProvider.
 * 
 * @author Jura Milić
 *
 */
public class LocalizationProviderBridge extends AbstractLocalizationProvider {
	/**
	 * Keeps track if decorator is connected to a listener.
	 */
	private boolean connected;
	/**
	 * Connected listener
	 */
	private ILocalizationListener listener;
	/**
	 * Decorated parent
	 */
	private ILocalizationProvider parent;

	/**
	 * Constructs a LocalizationProviderBridge with given provider
	 * 
	 * @param lp given provider
	 */
	public LocalizationProviderBridge(ILocalizationProvider lp) {
		parent = lp;
		listener = () -> fire();
	}

	/**
	 * Disconnects the listener from the parent.
	 */
	public void disconnect() {
		if (connected) {
			parent.removeLocalizationListener(listener);
			connected = false;
		}
	}

	/**
	 * Connects the listener to the parent.
	 */
	public void connect() {
		if (!connected) {
			parent.addLocalizationListener(listener);
			connected = true;
		}
	}

	@Override
	public String getString(String key) {
		return parent.getString(key);
	}

	@Override
	public String getLanguage() {
		return parent.getLanguage();
	}
}
