package hr.fer.oprpp1.hw04.db;

/**
 * This class offers implementations of <code>IComparisonOperator</code> interface
 * @author sbolsec
 *
 */
public class ComparisonOperators {
	
	/**
	 * Tests whether the first string is less than the second string.
	 */
	public static final IComparisonOperator LESS = (a, b) -> {
		if (a.compareTo(b) < 0) 
			return true;
		else
			return false;
	};
	
	/**
	 * Tests whether the first string is less than or equal to the second string.
	 */
	public static final IComparisonOperator LESS_OR_EQUALS = (a, b) -> {
		if (a.compareTo(b) <= 0)
			return true;
		else
			return false;
	};
	
	/**
	 * Tests whether the first string is greater than the second string.
	 */
	public static final IComparisonOperator GREATER = (a, b) -> {
		if (a.compareTo(b) > 0)
			return true;
		else
			return false;
	};
	
	/**
	 * Tests whether the first string is greater than or equal to the second string.
	 */
	public static final IComparisonOperator GREATER_OR_EQUALS = (a, b) -> {
		if (a.compareTo(b) >= 0) 
			return true;
		else 
			return false;
	};
	
	/**
	 * Tests whether the first string is equal to the second string.
	 */
	public static final IComparisonOperator EQUALS = (a, b) -> a.equals(b);
	
	/**
	 * Tests whether the first string is different from the second string.
	 */
	public static final IComparisonOperator NOT_EQUALS = (a, b) -> !a.equals(b);

	/**
	 * Tests whether the first string matches the regex in the second string.
	 */
	public static final IComparisonOperator LIKE = (a, b) -> {
		int indexOfDot = b.indexOf('*');
		
		if (indexOfDot != b.lastIndexOf('*'))
			throw new IllegalArgumentException("There can only be one wildcard character! Input was: " + b + ".");
		
		if (indexOfDot == -1) return a.equals(b);
		if (b.length() == 1) return true;	// b is '*' -> accept all
		
		if (a.length() < b.length() - 1) return false;
		
		if (a.startsWith(b.substring(0, indexOfDot)) &&
			a.endsWith(b.substring(indexOfDot+1, b.length())))
			return true;
		
		return false;
	};
}
