package hr.fer.zemris.java.gui.calc.buttons;

import java.awt.Color;
import java.awt.event.ActionListener;

import javax.swing.JButton;

/**
 * Class used for representing a basic Calculator Button.
 * 
 * @author Jura Milić
 *
 */
public class CalcButton extends JButton {
	private static final long serialVersionUID = 1L;

	/**
	 * Constructs a CalcButton with given String set as it's text and given
	 * ActionListener set as one of it's listeners.
	 * 
	 * @param text     given String
	 * @param listener given ActionListener
	 */
	public CalcButton(String text, ActionListener listener) {
		super(text);
		addActionListener(listener);

		setBackground(Color.CYAN);
		setFont(getFont().deriveFont(15f));
	}
}
