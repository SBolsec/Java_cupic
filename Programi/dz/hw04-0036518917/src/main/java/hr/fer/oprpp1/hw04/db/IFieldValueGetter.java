package hr.fer.oprpp1.hw04.db;

/**
 * This interface is responsible for obtaining a requested field value
 * from given StudentRecord.
 * @author sbolsec
 *
 */
@FunctionalInterface
public interface IFieldValueGetter {

	/**
	 * Returns requested field value from given StudentRecord.
	 * @param record student record from which to obtain the requested field value
	 * @return requested field value
	 */
	String get(StudentRecord record);
}
