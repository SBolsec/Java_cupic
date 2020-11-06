package hr.fer.oprpp1.hw01;

/**
 * This class adds support for complex numbers.
 *
 * @author sbolsec
 *
 */
public class ComplexNumber {

    /**
     * Real part of the complex number
     */
    private double real;
    /**
     * Imaginary part of the complex number
     */
    private double imaginary;

    /**
     * Creates a new complex number with the passed arguments.
     *
     * @param real real part of the complex number
     * @param imaginary imaginary part of the complex number
     */
    public ComplexNumber(double real, double imaginary) {
        this.real = real;
        this.imaginary = imaginary;
    }

    /**
     * Creates a new complex number which has the imaginary part equal to 0,
     * and the real part equal to the passed argument.
     *
     * @param real real part of the complex number
     * @return complex number with the imaginary part equal to 0, and the real part equal to the passed argument
     */
    public static ComplexNumber fromReal(double real) {
        return new ComplexNumber(real, 0);
    }

    /**
     * Creates a new complex number which has the real part equal to 0,
     * and the real imaginary equal to the passed argument.
     *
     * @param imaginary real imaginary of the complex number
     * @return complex number with the real part equal to 0, and the imaginary part equal to the passed argument
     */
    public static ComplexNumber fromImaginary(double imaginary) {
        return new ComplexNumber(0, imaginary);
    }

    /**
     * Creates a new complex number from the polar form.
     *
     * @param magnitude the distance to the origin (0,0)
     * @param angle angle of the complex number
     * @return complex number in the standard form (a+bi)
     */
    public static ComplexNumber fromMagnitudeAndAngle(double magnitude, double angle) {
        double x = magnitude * Math.cos(angle);
        double y = magnitude * Math.sin(angle);

        return new ComplexNumber(x, y);
    }

    /**
     * Parses a string into a complex number.
     * @param s input string
     * @return parsed complex number
     * @throws IllegalArgumentException passed string could not be parsed as complex number
     */
    public static ComplexNumber parse(String s) {
        int indexI = s.indexOf('i');

        // If there is no 'i', try to parse everything as real part of complex number
        if (indexI == -1) { 
        	try {
        		return fromReal(Double.parseDouble(s));	
        	} catch (NumberFormatException ex) {
        		throw new IllegalArgumentException("Input could not be parsed as double, it was: " + s + ".");
        	}
        }
        
        // If input is: i, +i
        if (s.length() == 1 || (s.length() == 2 && s.charAt(0) == '+')) return fromImaginary(1);
        // If input is: -i
        if (s.length() == 2 && s.charAt(0) == '-') return fromImaginary(-1);

        int indexPlus = s.lastIndexOf('+');
        int indexMinus = s.lastIndexOf('-');
        int separator = indexPlus < indexMinus ? indexMinus : indexPlus;

        // Something like: +3i, 4i, -2i, ...
        if (separator <= 0) {
            if (indexI != s.length() - 1) throw new IllegalArgumentException("There are elements after 'i': " + s.substring(indexI+1, s.length()));
            try {
            	return fromImaginary(Double.parseDouble(s.substring(0, s.length()-1)));	
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
            return new ComplexNumber(r, i);
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
        return new ComplexNumber(r, i);
    }

    /**
     * Returns the real part of the complex number.
     *
     * @return real part of complex number
     */
    public double getReal() {
        return real;
    }

    /**
     * Returns the imaginary part of the complex number.
     *
     * @return imaginary part of complex number
     */
    public double getImaginary() {
        return imaginary;
    }


    /**
     * Returns the magnitude of the complex number.
     *
     * @return magnitude of the complex number
     */
    public double getMagnitude() {
        return Math.hypot(this.real, this.imaginary);
    }

    /**
     * Returns the angle of the complex number.
     *
     * @return angle of the complex number
     */
    public double getAngle() {
    	return Math.atan2(imaginary, real);
    }

    /**
     * Returns the sum of the two complex numbers.
     *
     * @param c complex number to be added
     * @return sum of the complex numbers
     */
    public ComplexNumber add(ComplexNumber c) {
        return new ComplexNumber(this.real + c.real, this.imaginary + c.imaginary);
    }

    /**
     * Subtracts the passed complex numbers and returns the result.
     *
     * @param c complex number to be subtracted
     * @return original complex number subtracted by passed complex number
     */
    public ComplexNumber sub(ComplexNumber c) {
        return new ComplexNumber(this.real - c.real, this.imaginary - c.imaginary);
    }

    /**
     * Multiplies the two complex numbers.
     *
     * @param c complex number to be multiplied
     * @return result of multiplication of the two complex numbers
     */
    public ComplexNumber mul(ComplexNumber c) {
        return new ComplexNumber(this.real*c.real - this.imaginary*c.imaginary, this.real*c.imaginary + this.imaginary*c.real);
    }

    /**
     * Divides the original complex number by the passed complex number.
     *
     * @param c complex number which will be used to divide the original complex number
     * @return result of the division
     */
    public ComplexNumber div(ComplexNumber c) {
        double r = (this.real*c.real + this.imaginary*c.imaginary)/(c.real*c.real + c.imaginary*c.imaginary);
        double i = -1 * (this.real*c.imaginary - this.imaginary*c.real)/(c.real*c.real + c.imaginary*c.imaginary);
        return new ComplexNumber(r, i);
    }

    /**
     * Raises the original complex number to the specified power.
     *
     * @param n power to which the complex number will be raised
     * @return the original complex number raised to the power of the passed argument
     * @throws IllegalArgumentException n must be >= 0
     */
    public ComplexNumber power(int n) {
        if (n < 0) throw new IllegalArgumentException("n must be >= 0, it was: " + n + ".");

        // Moivre's formula will be used to calculate this
        double r = getMagnitude();
        double a = getAngle();

        return fromMagnitudeAndAngle(Math.pow(r, n), n*a);
    }

    /**
     * Returns the n-th roots of the original complex number.
     *
     * @param n determines which root will be the result
     * @return the n-th roots of the original complex number
     * @throws IllegalArgumentException n must be greater than 0
     */
    public ComplexNumber[] root(int n) {
        if (n <= 0) throw new IllegalArgumentException("n must be greater than 0, it was: " + n + ".");

        double r = getMagnitude();
        double a = getAngle();

        ComplexNumber[] array = new ComplexNumber[n];

        for (int i = 0; i < n; i++) {
            double m = Math.pow(r, 1./n);
            double fi = (a + (2 * Math.PI * i)) / n;

            array[i] = fromMagnitudeAndAngle(m, fi);
        }

        return array;
    }

    @Override
    public String toString() {
        char sign = imaginary < 0 ? '-' : '+';

        return String.format("(%f %c %fi)", real, sign, Math.abs(imaginary));
    }
}
