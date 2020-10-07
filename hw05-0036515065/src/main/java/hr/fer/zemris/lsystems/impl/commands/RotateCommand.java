package hr.fer.zemris.lsystems.impl.commands;

import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.Command;
import hr.fer.zemris.lsystems.impl.Context;

/**
 * Command implementation which rotates the top TurtleState of given Context by
 * a set angle.
 * 
 * @author Jura Milić
 *
 */
public class RotateCommand implements Command {
	/**
	 * Value used for getting the Radians value from Degree
	 */
	private static final double DEGREE_TO_RAD = Math.PI / 180;
	/**
	 * Angle of rotation in radians
	 */
	private double angle;

	/**
	 * Constructs a new RotateCommand with angle given in degrees.
	 * 
	 * @param angle given angle in degrees
	 */
	public RotateCommand(double angle) {
		this.angle = angle * DEGREE_TO_RAD ;
	}

	@Override
	public void execute(Context ctx, Painter painter) {
		ctx.getCurrentState().getDirection().rotate(angle);
	}

}
