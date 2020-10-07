package hr.fer.zemris.lsystems.impl.commands;

import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.Command;
import hr.fer.zemris.lsystems.impl.Context;
import hr.fer.zemris.lsystems.impl.TurtleState;
import hr.fer.zemris.math.Vector2D;

/**
 * Command implementation which moves the top TurtleState of given Context by a
 * set step.
 * 
 * @author Jura Milić
 *
 */
public class SkipCommand implements Command {
	/**
	 * Length of Turtle's step.
	 */
	private double step;

	/**
	 * Constructs a new SkipCommand with given step.
	 * 
	 * @param step given step
	 */
	public SkipCommand(double step) {
		this.step = step;
	}

	@Override
	public void execute(Context ctx, Painter painter) {
		TurtleState state = ctx.getCurrentState();

		Vector2D orient = state.getDirection().scaled(step * state.getShift());

		state.getPosition().translate(orient);
	}

}
