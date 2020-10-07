package hr.fer.zemris.lsystems.impl;

import hr.fer.zemris.lsystems.Painter;

/**
 * Basic interface for commands used for drawing a graphic representation of a
 * Lindenmayer system.
 * 
 * @author Jura Milić
 *
 */
public interface Command {
	/**
	 * Execute Command specified by the Class.
	 * 
	 * @param ctx     Context containing a Stack of TurtleStates which the Command
	 *                will use as operands.
	 * @param painter Painter used only if Command needs to draw.
	 */
	void execute(Context ctx, Painter painter);
}
