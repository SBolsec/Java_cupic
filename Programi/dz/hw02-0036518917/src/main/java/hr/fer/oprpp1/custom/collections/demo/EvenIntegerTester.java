package hr.fer.oprpp1.custom.collections.demo;

import hr.fer.oprpp1.custom.collections.Tester;

/**
 * Tests whether the given object is a even integer
 * @author sbolsec
 *
 */
class EvenIntegerTester implements Tester {
	public boolean test(Object obj) {
		if(!(obj instanceof Integer)) return false;
		Integer i = (Integer)obj;
		return i % 2 == 0;
	}
}
