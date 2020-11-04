package hr.fer.zemris.lsystems.impl;

import java.awt.Color;

import hr.fer.oprpp1.math.Vector2D;

/**
 * This class models the state of a turtle which draws to the screen.
 * @author sbolsec
 *
 */
public class TurtleState {
	
	/** Position of the turtle **/
	private Vector2D position;
	/** Angle in which the turtle is currently facing **/
	private Vector2D angle;
	/** Color of the line the turtle draws **/
	private Color color;
	/** Length by which the turtle moves in the direction it is facing **/
	private double unitLength;
	
	/**
	 * Constructor which initializes all the values of the turtle state.
	 * @param position position of the turtle state
	 * @param angle angle of the turtle state
	 * @param color color of the turtle state
	 * @param unitLength unit length of the turtle state
	 */
	public TurtleState(Vector2D position, Vector2D angle, Color color, double unitLength) {
		super();
		this.position = position;
		this.angle = angle;
		this.color = color;
		this.unitLength = unitLength;
	}

	/**
	 * Copies this turtle state with all the values being copied.
	 * @return copy of this turtle state
	 */
	public TurtleState copy() {
		return new TurtleState(position.copy(), angle.copy(), color, unitLength);
	}

	/**
	 * Returns the position of the turtle.
	 * @return position of the turtle
	 */
	public Vector2D getPosition() {
		return position;
	}

	/**
	 * Sets the position of the turtle.
	 * @param position new position of the turtle
	 */
	public void setPosition(Vector2D position) {
		this.position = position;
	}

	/**
	 * Returns the angle vector of the direction the turtle is facing.
	 * @return angle vector of the direction the turtle is facing
	 */
	public Vector2D getAngle() {
		return angle;
	}

	/**
	 * Sets the angle vector of the direction the turtle is facing.
	 * @param angle angle vector of the direction the turtle is facing
	 */
	public void setAngle(Vector2D angle) {
		this.angle = angle;
	}

	/**
	 * Returns the color which is used for drawing lines.
	 * @return color of the line
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * Sets the color which is user for drawing lines.
	 * @param color color of the line
	 */
	public void setColor(Color color) {
		this.color = color;
	}

	/**
	 * Returns the unit length of the turtle.
	 * @return unit length
	 */
	public double getUnitLength() {
		return unitLength;
	}

	/**
	 * Sets the unit length of the turtle.
	 * @param unitLength unit length to be set
	 */
	public void setUnitLength(double unitLength) {
		this.unitLength = unitLength;
	}
}
