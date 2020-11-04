package hr.fer.oprpp1.hw04.db;

/**
 * This interface allows the creation of filters for <code>StudendRecord</code> class
 * @author sbolsec
 *
 */
@FunctionalInterface
public interface IFilter {
	
	/**
	 * Returns true if the given student record satisfies a filter.
	 * @param record student record to be tested
	 * @return true if the given student record satisfies the filter, false otherwise
	 */
	public boolean accepts(StudentRecord record);
}
