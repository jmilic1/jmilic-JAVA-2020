package hr.fer.zemris.lsystems.impl.commands;

import hr.fer.zemris.lsystems.Painter;

import java.awt.Color;
import hr.fer.zemris.lsystems.impl.Command;
import hr.fer.zemris.lsystems.impl.Context;

/**
 * Command implementation which sets a color to the top TurtleState of given
 * Context.
 * 
 * @author Jura Milić
 *
 */
public class ColorCommand implements Command {
	/**
	 * Color which should be set
	 */
	private Color color;

	/**
	 * Constructs new ColorCommand with given color
	 * 
	 * @param color given color
	 */
	public ColorCommand(Color color) {
		this.color = color;
	}

	@Override
	public void execute(Context ctx, Painter painter) {
		ctx.getCurrentState().setColor(color);
	}

}
