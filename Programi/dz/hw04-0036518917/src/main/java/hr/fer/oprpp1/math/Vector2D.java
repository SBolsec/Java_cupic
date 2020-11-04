package hr.fer.oprpp1.math;

/**
 * This class models a 2D vector whose components are real numbers.
 * @author sbolsec
 *
 */
public class Vector2D {
	/** value on x-axis **/
	private double x;
	/** value on y-axis **/
	private double y;
	
	/**
	 * Creates 2D vector with given values.
	 * @param x
	 * @param y
	 */
	public Vector2D(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Returns value on x-axis.
	 * @return value on x-axis
	 */
	public double getX() {
		return this.x;
	}
	
	/**
	 * Returns value on y-axis.
	 * @return value on y-axis
	 */
	public double getY() {
		return this.y;
	}
	
	/**
	 * Adds the given vector to this vector.
	 * This changes the current vector.
	 * @param offset vector to be added to this vector
	 * @throws NullPointerException if the given offset is null
	 */
	public void add(Vector2D offset) {
		this.x = this.x + offset.x;
		this.y = this.y + offset.y;
	}
	
	/**
	 * Adds the given vector to this vector. This creates a new vector
	 * and does not change the current vector nor the given vector.
	 * @param offset vector to be added to this vector
	 * @return new vector which is equal to sum of these vectors
	 * @throws NullPointerException if the given offset is null
	 */
	public Vector2D added(Vector2D offset) {
		double newX = this.x + offset.x;
		double newY = this.y + offset.y;
		return new Vector2D(newX, newY);
	}
	
	/**
	 * Rotates this vector for the given angle in radians.
	 * This changes the vector.
	 * @param angle angle in radians for which the vector will be rotated
	 */
	public void rotate(double angle) {
		double newX = Math.cos(angle) * this.x - Math.sin(angle) * this.y;
		double newY = Math.sin(angle) * this.x + Math.cos(angle) * this.y;
		this.x = newX;
		this.y = newY;
	}
	
	/**
	 * Rotates this vector for the given angle in radians.
	 * This creates a new vector and does not change the current vector.
	 * @param angle angle in radians for which the vector will be rotated
	 * @return new vector which is rotated for the given angle
	 */
	public Vector2D rotated(double angle) {
		double newX = Math.cos(angle) * this.x - Math.sin(angle) * this.y;
		double newY = Math.sin(angle) * this.x + Math.cos(angle) * this.y;
		return new Vector2D(newX, newY);
	}
	
	/**
	 * Scales this vector by the given scaler.
	 * This changes the vector.
	 * @param scaler scalar by which the vector will be scaled
	 */
	public void scale(double scaler) {
		this.x *= scaler;
		this.y *= scaler;
	}
	
	/**
	 * Scales this vector by the given scaler.
	 * This creates a new vector and does not change the current vector.
	 * @param scaler scaler by which the vector will be scaled
	 * @return new vector which is scaled by the given scaler
	 */
	public Vector2D scaled(double scaler) {
		return new Vector2D(this.x * scaler, this.y * scaler);
	}
	
	/**
	 * Returns new vector which is a copy of this vector
	 * @return new vector which is a copy of this vector 
	 */
	public Vector2D copy() {
		return new Vector2D(this.x, this.y);
	}
}
