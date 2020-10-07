package hr.fer.zemris.java.gui.calc.buttons;

import java.awt.event.ActionListener;

/**
 * Class represents an Invertible Calculator Button. This button will invert
 * it's listener and text when an outside action occurs.
 * 
 * @author Jura Milić
 *
 */
public class InvertibleCalcButton extends CalcButton {
	private static final long serialVersionUID = 1L;
	/**
	 * Primary text for button.
	 */
	private String primaryText;
	/**
	 * Secondary text for button.
	 */
	private String secondaryText;
	/**
	 * Primary listener for button.
	 */
	private ActionListener primaryListener;
	/**
	 * Secondary listener for button.
	 */
	private ActionListener secondaryListener;

	/**
	 * Constructs InvertibleCalcButton with values set appropriately.
	 * 
	 * @param primaryText       given primary text
	 * @param primaryListener   given primary listener
	 * @param secondaryText     given secondary text
	 * @param secondaryListener given seconday listener
	 */
	public InvertibleCalcButton(String primaryText, ActionListener primaryListener, String secondaryText,
			ActionListener secondaryListener) {
		super(primaryText, primaryListener);

		this.primaryText = primaryText;
		this.primaryListener = primaryListener;
		this.secondaryText = secondaryText;
		this.secondaryListener = secondaryListener;
	}

	/**
	 * Inverts the listener and text of the button.
	 */
	public void switchOperation() {
		if (secondaryText != null) {
			if (getText().equals(primaryText)) {
				setText(secondaryText);
				removeActionListener(primaryListener);
				addActionListener(secondaryListener);
			} else {
				setText(primaryText);
				removeActionListener(secondaryListener);
				addActionListener(primaryListener);
			}
		}
	}
}
