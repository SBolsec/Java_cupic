package hr.fer.oprpp1.custom.collections;

/**
 * The Processor is a model of an object capable of performing some operation based on the passed object.
 * 
 * @author sbolsec
 *
 */
@FunctionalInterface
public interface Processor {
	
	/**
	 * Processes the passed object
	 * 
	 * @param value object to be processed
	 */
	void process(Object value);
}
