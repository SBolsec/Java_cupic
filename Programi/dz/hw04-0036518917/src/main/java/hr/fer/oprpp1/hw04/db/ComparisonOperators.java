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
		int j = 0;
		int indexOfDot = b.indexOf('*');
		
		if (indexOfDot == -1) return a.equals(b);
		if (b.length() == 1) return true;	// b is '*' -> accept all
		
		try {
			for (int i = 0; i < a.length(); i++) {
				if (a.charAt(i) == b.charAt(j)) {
					j++;
					continue;
				}
				if (b.charAt(j) != '*')
					return false;
				
				if (b.length() - 1 == indexOfDot)
					return true;
				
				if (a.charAt(i) == b.charAt(j+1)) {
					j += 2;
					continue;
				}
			}
		} catch (Exception ex) {
			return false;
		}
		
		if (j != b.length()) return false;
		return true;
	};
}
