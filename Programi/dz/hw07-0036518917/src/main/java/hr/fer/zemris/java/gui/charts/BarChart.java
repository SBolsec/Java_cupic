package hr.fer.zemris.java.gui.charts;

import java.util.List;

/**
 * Models a bar chart. Stores all data neded to
 * create bar chart.
 * @author sbolsec
 *
 */
public class BarChart {
	/** List of XYValues **/
	private List<XYValue> values;
	/** Description of x-axis **/
	private String xDesc;
	/** Description of y-axis **/
	private String yDesc;
	/** Minimum y to show on y-axis **/
	private int minY;
	/** Maximum y to show on x-axis **/
	private int maxY;
	/** Gap between two y's that are next to each other **/
	private int gap;
	
	/**
	 * Constructor whihc checks the validity of the arguments
	 * and stores them if they are valid.
	 * @param values list of <code>XYValue</code> items
	 * @param xDesc description of x-axis
	 * @param yDesc description of y-axis
	 * @param minY minimum y to show on y-axis
	 * @param maxY maximum y to show on y-axis
	 * @param gap gap between two y's that are next to each other
	 * @throws NullPointerException if the list of values is null
	 * @throws IllegalArgumentException if there is something wrong with the input arguments
	 */
	public BarChart(List<XYValue> values, String xDesc, String yDesc, int minY, int maxY, int gap) {
		int rest = (maxY - minY) % gap;
		if ((maxY - minY) % 2 != 0) minY += rest;
		if (minY < 0) throw new IllegalArgumentException("Minimum y can not be negative! It was: " + minY + ".");
		if (maxY <= minY) throw new IllegalArgumentException("Maximum y must be greater than minimum y! Max was: " + maxY + ", min was: " + minY + ".");
		if (values == null) throw new NullPointerException("List of values can not be null!");
		
		for (XYValue v : values) {
			if (v.getY() < minY)
				throw new IllegalArgumentException("Y value in one of the values in the list was smaller than the specified minimim value of y. Min y is: " + minY + ", value was: " + v.getX() + ".");
		}
		
		this.values = values;
		this.xDesc = xDesc == null ? "" : xDesc; // to avoid potential NullPointerException
		this.yDesc = yDesc == null ? "" : yDesc;
		this.minY = minY;
		this.maxY = maxY;
		this.gap = gap;
	}

	/**
	 * Returns list of XYValue values.
	 * @return list of XYValue values
	 */
	public List<XYValue> getValues() {
		return values;
	}

	/**
	 * Returns description of x-axis.
	 * @return description of x-axis
	 */
	public String getxDesc() {
		return xDesc;
	}

	/**
	 * Returns description of y-axis.
	 * @return description of y-axis
	 */
	public String getyDesc() {
		return yDesc;
	}

	/**
	 * Returns minimum y value.
	 * @return minimum y
	 */
	public int getMinY() {
		return minY;
	}

	/**
	 * Returns maximum y value.
	 * @return maximum y
	 */
	public int getMaxY() {
		return maxY;
	}

	/**
	 * Returns gap between y's that are next to each other.
	 * @return gap between y's
	 */
	public int getGap() {
		return gap;
	}
}
