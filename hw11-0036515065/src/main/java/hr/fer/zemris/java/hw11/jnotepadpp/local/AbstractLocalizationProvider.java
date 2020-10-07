package hr.fer.zemris.java.hw11.jnotepadpp.local;

import java.util.ArrayList;
import java.util.List;

/**
 * An abstract localization provider which keeps a collection of it's registered
 * listeners.
 * 
 * @author Jura Milić
 *
 */
public abstract class AbstractLocalizationProvider implements ILocalizationProvider {
	/**
	 * List of registered listeners
	 */
	private List<ILocalizationListener> listeners;

	/**
	 * Constructs an AbstractLocalizationProvider
	 */
	public AbstractLocalizationProvider() {
		listeners = new ArrayList<ILocalizationListener>();
	}

	@Override
	public void addLocalizationListener(ILocalizationListener l) {
		listeners.add(l);
	}

	@Override
	public void removeLocalizationListener(ILocalizationListener l) {
		listeners.remove(l);
	}

	/**
	 * Notifies all registered listeners that the localization changed.
	 */
	public void fire() {
		for (ILocalizationListener listener : listeners) {
			listener.localizationChanged();
		}
	}
}
