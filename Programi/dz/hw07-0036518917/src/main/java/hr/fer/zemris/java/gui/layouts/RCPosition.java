package hr.fer.zemris.java.gui.layouts;

/**
 * Models a position in a grid using row and column.
 * @author sbolsec
 *
 */
public class RCPosition {
	/** Row of the element **/
	private int row;
	/** Column of the element **/
	private int column;
	
	/**
	 * Constructor which initializes the row and column.
	 * @param row row of the element
	 * @param column column of the element
	 */
	public RCPosition(int row, int column) {
		this.row = row;
		this.column = column;
	}

	/**
	 * Returns row.
	 * @return row
	 */
	public int getRow() {
		return row;
	}

	/**
	 * Returns column.
	 * @return column
	 */
	public int getColumn() {
		return column;
	}
	
	/**
	 * Parses the given text into a RCPosition.
	 * @param text input text
	 * @return RCPosition generated from given text
	 * @throws NullPointerException if input text is null
	 * @throws IllegalArgumentException if input text can not be parsed as RCPosition
	 */
	public static RCPosition parse(String text) {
		if (text == null)
			throw new NullPointerException("Input text can not be null!");
		if (text.trim().length() == 0)
			throw new IllegalArgumentException("Input can not be empty!");
		if (text.indexOf(',') != text.lastIndexOf(','))
			throw new IllegalArgumentException("There were more than one ',' signs!");
		
		String[] elements = text.trim().split(",");
		
		if (elements.length != 2)
			throw new IllegalArgumentException("Input must be in format: 'x,y', it was: " + text + ".");
		
		try {
			int row = Integer.parseInt(elements[0].trim());
			int column = Integer.parseInt(elements[1].trim());
			return new RCPosition(row, column);
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("Input could not be parsed into RCPosition, it was: " + text + ".");
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + column;
		result = prime * result + row;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RCPosition other = (RCPosition) obj;
		if (column != other.column)
			return false;
		if (row != other.row)
			return false;
		return true;
	}
}
