package hr.fer.oprpp1.hw04.db;

/**
 * This class offers implementations of the <code>IFieldGetter</code> interface.
 * @author sbolsec
 *
 */
public class FieldValueGetters {
	
	/**
	 * Returns the first name from the given student record.
	 */
	public static final IFieldValueGetter FIRST_NAME = StudentRecord::getFirstName;
	
	/**
	 * Returns the last name from the given student record.
	 */
	public static final IFieldValueGetter LAST_NAME = StudentRecord::getLastName;
	
	/**
	 * Returns the jmbag from the given student record.
	 */
	public static final IFieldValueGetter JMBAG = StudentRecord::getJmbag;
}
