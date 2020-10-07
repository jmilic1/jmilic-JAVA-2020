package hr.fer.zemris.lsystems.impl;

import java.awt.Color;
import hr.fer.zemris.math.Vector2D;

/**
 * Class used for representing a turtle on a paintable area. TurtleState defines
 * turtle's attributes: its position, the direction it is looking at, what color
 * its lines are and the amount by which it shifts.
 * 
 * @author Jura Milić
 *
 */
public class TurtleState {
	/**
	 * Position of turtle
	 */
	private Vector2D position;
	/**
	 * Direction of next shift
	 */
	private Vector2D direction;
	/**
	 * Color of turtle's lines
	 */
	private Color color;
	/**
	 * Shift amount
	 */
	private double shift;

	/**
	 * Constructs new TurtleState with variables defined accordingly.
	 * 
	 * @param position  given position
	 * @param direction given direction
	 * @param color     given color
	 * @param shift     given shift
	 */
	public TurtleState(Vector2D position, Vector2D direction, Color color, double shift) {
		this.position = position;
		this.direction = direction;
		this.color = color;
		this.shift = shift;
	}

	/**
	 * Generates a copy of this TurtleState.
	 * 
	 * @return Generated TurtleState copy
	 */
	public TurtleState copy() {
		return new TurtleState(position.copy(), direction.copy(), color, shift);
	}

	/**
	 * @return position
	 */
	public Vector2D getPosition() {
		return position;
	}

	/**
	 * @param position
	 */
	public void setPosition(Vector2D position) {
		this.position = position;
	}

	/**
	 * @return direction
	 */
	public Vector2D getDirection() {
		return direction;
	}

	/**
	 * @param direction
	 */
	public void setDirection(Vector2D direction) {
		this.direction = direction;
	}

	/**
	 * @return color
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * @param color
	 */
	public void setColor(Color color) {
		this.color = color;
	}

	/**
	 * @return shift
	 */
	public double getShift() {
		return shift;
	}

	/**
	 * @param shift
	 */
	public void setShift(double shift) {
		this.shift = shift;
	}

}
