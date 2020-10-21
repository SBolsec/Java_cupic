package hr.fer.oprpp1.custom.collections;

/**
 * This class tests whether passed objects are acceptable by some criteria.
 * 
 * @author sbolsec
 *
 */
@FunctionalInterface
public interface Tester {
	
	/**
	 * Tests whether the given object is acceptable or not.
	 * 
	 * @param obj
	 * @return true if the object is acceptable, false otherwise
	 */
	boolean test(Object obj);
}
