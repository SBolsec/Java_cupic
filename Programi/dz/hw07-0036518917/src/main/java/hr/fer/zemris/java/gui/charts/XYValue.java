package hr.fer.zemris.java.gui.charts;

/**
 * Has two integer read-only properties.
 * @author sbolsec
 *
 */
public class XYValue {
	/** Value of x **/
	private int x;
	/** Value of y **/
	private int y;
	
	/**
	 * Default constructor which initializes the properties.
	 * @param x value to be set for x
	 * @param y value to be set for y
	 */
	public XYValue(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Returns value of x.
	 * @return value of x
	 */
	public int getX() {
		return x;
	}

	/**
	 * Returns value of y.
	 * @return value of y
	 */
	public int getY() {
		return y;
	}
	
	
}
