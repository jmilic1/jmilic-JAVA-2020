package hr.fer.zemris.lsystems.impl.commands;

import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.Command;
import hr.fer.zemris.lsystems.impl.Context;
import hr.fer.zemris.lsystems.impl.TurtleState;

/**
 * Command implementation which scales the shift of top TurtleState of given
 * Context by a set factor.
 * 
 * @author Jura Milić
 *
 */
public class ScaleCommand implements Command {
	/**
	 * Value of scale factor.
	 */
	private double factor;

	/**
	 * Constructs a new ScaleCommand with given factor.
	 * 
	 * @param factor given factor
	 */
	public ScaleCommand(double factor) {
		this.factor = factor;
	}

	@Override
	public void execute(Context ctx, Painter painter) {
		TurtleState state = ctx.getCurrentState();

		state.setShift(state.getShift() * factor);
	}

}
