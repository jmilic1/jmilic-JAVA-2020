package hr.fer.zemris.lsystems.impl.commands;

import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.Command;
import hr.fer.zemris.lsystems.impl.Context;
import hr.fer.zemris.lsystems.impl.TurtleState;
import hr.fer.zemris.math.Vector2D;

/**
 * Command implementation which draws a line using the top TurtleState of given
 * Context. The TurtleState position is also changed.
 * 
 * @author Jura Milić
 *
 */
public class DrawCommand implements Command {
	/**
	 * Value used for scaling the shift of TurtleState
	 */
	private double step;

	/**
	 * Constructs new DrawCommand with given step
	 * 
	 * @param step given step
	 */
	public DrawCommand(double step) {
		this.step = step;
	}

	@Override
	public void execute(Context ctx, Painter painter) {
		TurtleState state = ctx.getCurrentState();
		Vector2D startingPos = state.getPosition().copy();

		SkipCommand comm = new SkipCommand(step);
		comm.execute(ctx, painter);

		Vector2D newPos = state.getPosition();

		painter.drawLine(startingPos.getX(), startingPos.getY(), newPos.getX(), newPos.getY(), state.getColor(), 1);
	}

}
