package hr.fer.zemris.java.fractals;

import hr.fer.zemris.math.Complex;

/**
 * Util class
 * @author sbolsec
 *
 */
public class ComplexUtil {
	/**
     * Parses a string into a complex number.
     * @param s input string
     * @return parsed complex number
     * @throws IllegalArgumentException passed string could not be parsed as complex number
     */
    public static Complex parse(String s) {
        if (s == null) 
        	throw new IllegalArgumentException("Argument can not be null!");
    	
    	s = s.trim();
        
        if (s.length() == 0) 
        	throw new IllegalArgumentException("Argument can not be blank!");
    	
    	int indexI = s.indexOf('i');

        // If there is no 'i', try to parse everything as real part of complex number
        if (indexI == -1) { 
        	try {
        		return new Complex(Double.parseDouble(s), 0);	
        	} catch (NumberFormatException ex) {
        		throw new IllegalArgumentException("Input could not be parsed as double, it was: " + s + ".");
        	}
        }
        
        // If input is: i, +i
        if (s.length() == 1 || (s.length() == 2 && s.charAt(0) == '+')) return new Complex(0,1);
        // If input is: -i
        if (s.length() == 2 && s.charAt(0) == '-') return new Complex(0, -1);

        int indexPlus = s.lastIndexOf('+');
        int indexMinus = s.lastIndexOf('-');
        int separator = indexPlus < indexMinus ? indexMinus : indexPlus;

        // Something like: +i3, i4, -i2, ...
        if (separator <= 0) {
        	if (indexI == 0) {
        		try {
        			return new Complex(0, Double.parseDouble(s.substring(indexI+1)));
        		} catch (NumberFormatException e) {
        			throw new IllegalArgumentException("Input could not be parsed as double, it was: " + s.substring(indexI+1) + ".");
        		}
        	} else if (indexI == 1) {
        		try {
        			boolean positive = s.charAt(0) == '-' ? false : true;
        			double im = Double.parseDouble(s.substring(indexI+1));
        			return new Complex(0, positive ? im : -im);
        		} catch (NumberFormatException e) {
        			throw new IllegalArgumentException("Input could not be parsed as double, it was: " + s.substring(indexI+1) + ".");
        		}
        	} else if (indexI == s.length()-1) {
        		boolean positive = s.charAt(0) == '-' ? false : true;
        		return new Complex(0, positive ? 1 : -1);
        	} else {
        		try {
        			boolean positive = s.charAt(0) == '-' ? false : true;
        			for (int i = 1; i < indexI; i++) {
        				if (s.charAt(i) != ' ') {
        					throw new IllegalArgumentException("There was something between separator and i, input was: " + s + ".");
        				}
        			}
        			
        			double im = Double.parseDouble(s.substring(indexI+1));
        			return new Complex(0, positive ? im : -im);
        		} catch (NumberFormatException e) {
        			throw new IllegalArgumentException("Input could not be parsed as double, it was: " + s.substring(indexI+1) + ".");
        		}
        	}
        }

        // Something like: 1+i3, -5-i1, ...
        double r = Double.parseDouble(s.substring(0, separator));
        double i = 0;
        boolean imPositive = s.charAt(separator) == '-' ? false : true;
        for (int j = separator+1; j < indexI; j++) {
			if (s.charAt(j) != ' ') {
				throw new IllegalArgumentException("There was something between separator and i, input was: " + s + ".");
			}
		}
        if (indexI == s.length()-1) {
        	i = 1;
        } else {
            i = Double.parseDouble(s.substring(indexI+1));
        }
        return new Complex(r, imPositive ? i : -i);
    }
}
