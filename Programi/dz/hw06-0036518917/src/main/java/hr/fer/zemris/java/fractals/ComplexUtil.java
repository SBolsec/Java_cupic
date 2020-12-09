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

        // Something like: +3i, 4i, -2i, ...
        if (separator <= 0) {
            if (indexI != s.length() - 1) throw new IllegalArgumentException("There are elements after 'i': " + s.substring(indexI+1, s.length()));
            try {
            	return new Complex(0, Double.parseDouble(s.substring(0, s.length()-1)));	
            } catch (NumberFormatException ex) {
            	throw new IllegalArgumentException("Input could not be parsed as double, it was: " + s.substring(0, s.length()-1) + ".");
            }
        }

        // If real part is before imaginary part, examples: 2+3i, -1-i, ...
        if (separator < indexI) {
            if (indexI != s.length() - 1) throw new IllegalArgumentException("There are elements after 'i': " + s.substring(indexI+1, s.length()));
            double r = Double.parseDouble(s.substring(0, separator));
            double i = 0;
            if (separator + 1 == indexI) {
                if (s.charAt(separator) == '-') i = -1;
                else i = 1;
            } else {
                i = Double.parseDouble(s.substring(separator, s.length()-1));
            }
            return new Complex(r, i);
        }
        
        // Else imaginary part is first and real part is second
        // If there is something behind 'i' other than '+' or '-', throw an exception. Examples: -2i1+5, 5is-2, ...
        if (indexI + 1 != separator) throw new IllegalArgumentException("There are elements after 'i': " + s.substring(indexI+1, separator));
        
        double i = 0;
        if (indexI == 0) {
            i = 1;
        } else if (indexI == 1) {
            if (s.charAt(0) == '-') i = -1;
            else if (s.charAt(0) == '+') i = 1;
            else {
            	try {
            		i = Double.parseDouble(s.substring(0, separator-1));
            	} catch (NumberFormatException ex) {
            		throw new IllegalArgumentException("Input could not be parsed as double, it was: " + s.substring(0, separator-1) + ".");
            	}
            }
        } else {
            try {
            	i = Double.parseDouble(s.substring(0, separator-1));
            } catch (NumberFormatException ex) {
            	throw new IllegalArgumentException("Input could not be parsed as double, it was: " + s.substring(0, separator-1) + ".");
            }
        }
        double r = 0;
        try {
        	r = Double.parseDouble(s.substring(separator, s.length()));
        } catch (NumberFormatException ex) {
        	throw new IllegalArgumentException("Input could not be parsed as double, it was: " + s.substring(separator, s.length()) + ".");
        }
        return new Complex(r, i);
    }
}
