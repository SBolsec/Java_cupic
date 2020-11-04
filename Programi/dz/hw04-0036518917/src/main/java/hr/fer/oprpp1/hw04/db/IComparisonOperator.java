package hr.fer.oprpp1.hw04.db;

/**
 * This interface allows two string to be compared.
 * @author sbolsec
 *
 */
@FunctionalInterface
public interface IComparisonOperator {

	/**
	 * Tests whether the comparison is satisfied.
	 * @param value1 first value to be tested
	 * @param value2 second value to be tested
	 * @return true if comparison was satisfied, false otherwise
	 */
	boolean satisfied(String value1, String value2);
}
