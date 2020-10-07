package hr.fer.zemris.java.hw11.jnotepadpp.local;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

/**
 * Class for allowing communication between JFrame and the provider.
 * 
 * @author Jura Milić
 *
 */
public class FormLocalizationProvider extends LocalizationProviderBridge {
	/**
	 * Constructs new FormLocalizationProvider with given provider and frame. A
	 * window listener is added to the frame so the bridge can control provider when
	 * deemed necessary.
	 * 
	 * @param lp    given provider
	 * @param frame given frame
	 */
	public FormLocalizationProvider(ILocalizationProvider lp, JFrame frame) {
		super(lp);

		frame.addWindowListener(new WindowAdapter() {

			@Override
			public void windowOpened(WindowEvent e) {
				connect();
			}

			@Override
			public void windowClosed(WindowEvent e) {
				disconnect();
			}

		});
	}
}
