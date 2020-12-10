package hr.fer.zemris.math;

import static java.lang.Math.*;

import java.util.ArrayList;
import java.util.List;

/**
 * This class models a complex number and basic operations
 * with complex numbers
 * @author sbolsec
 *
 */
public class Complex {
	/** Real part of the complex number **/
	private double re;
	/** Imaginary part of the complex number **/
	private double im;
	
	public static final Complex ZERO = new Complex(0,0);
	public static final Complex ONE = new Complex(1,0);
	public static final Complex ONE_NEG = new Complex(-1,0);
	public static final Complex IM = new Complex(0,1);
	public static final Complex IM_NEG = new Complex(0, -1);

	/**
	 * Default constructor which sets real and imaginary part to 0
	 */
	public Complex() {
		this.re = 0;
		this.im = 0;
	}
	
	/**
	 * Constructor which initializes the real and imaginary part
	 * @param re real part of the complex number
	 * @param im imaginary part of the complex number
	 */
	public Complex(double re, double im) {
		this.re = re;
		this.im = im;
	}
	
	/**
	 * Returns the module of the complex number
	 * @return module of the complex number
	 */
	public double module() {
		return hypot(re, im);
	}
	
	/**
     * Multiplies the two complex numbers.
     * @param c complex number to be multiplied
     * @return result of multiplication of the two complex numbers
     */
	public Complex multiply(Complex c) {
		return new Complex(this.re*c.re - this.im*c.im, this.re*c.im + this.im*c.re);
	}
	
	/**
    * Divides the original complex number by the passed complex number.
    * @param c complex number which will be used to divide the original complex number
    * @return result of the division
    */
   public Complex divide(Complex c) {
       double r = (this.re*c.re + this.im*c.im)/(c.re*c.re + c.im*c.im);
       double i = -1 * (this.re*c.im - this.im*c.re)/(c.re*c.re + c.im*c.im);
       return new Complex(r, i);
   }
   
   /**
    * Returns the sum of the two complex numbers.
    * @param c complex number to be added
    * @return sum of the complex numbers
    */
   public Complex add(Complex c) {
       return new Complex(this.re + c.re, this.im + c.im);
   }

   /**
    * Subtracts the passed complex numbers and returns the result.
    * @param c complex number to be subtracted
    * @return original complex number subtracted by passed complex number
    */
   public Complex sub(Complex c) {
       return new Complex(this.re - c.re, this.im - c.im);
   }
   
   /**
    * Negates the complex number
    * @return returns negated complex number
    */
   public Complex negate() {
	   return new Complex(-this.re, -this.im);
   }
   
   /**
    * Raises the original complex number to the specified power.
    * @param n power to which the complex number will be raised
    * @return the original complex number raised to the power of the passed argument
    * @throws IllegalArgumentException n must be >= 0
    */
   public Complex power(int n) {
       if (n < 0) throw new IllegalArgumentException("n must be >= 0, it was: " + n + ".");

       // Moivre's formula will be used to calculate this
       double r = module();
       double a = getAngle();

       return fromModuleAndAngle(Math.pow(r, n), n*a);
   }

   /**
    * Returns the n-th roots of the original complex number.
    * @param n determines which root will be the result
    * @return the n-th roots of the original complex number
    * @throws IllegalArgumentException n must be greater than 0
    */
   public List<Complex> root(int n) {
       if (n <= 0) throw new IllegalArgumentException("n must be greater than 0, it was: " + n + ".");

       double r = module();
       double a = getAngle();

       List<Complex> list = new ArrayList<>();

       for (int i = 0; i < n; i++) {
           double m = Math.pow(r, 1./n);
           double fi = (a + (2 * Math.PI * i)) / n;

           list.add(fromModuleAndAngle(m, fi));
       }

       return list;
   }
   
   /**
    * Returns a string representation of the complex number
    */
   @Override
   public String toString() {
       char sign = im < 0 ? '-' : '+';

       return String.format("(%.1f%ci%.1f)", re, sign, abs(im));
   }
   
   /**
    * Creates a new complex number from the polar form.
    * @param magnitude the distance to the origin (0,0)
    * @param angle angle of the complex number
    * @return complex number in the standard form (a+bi)
    */
   private static Complex fromModuleAndAngle(double magnitude, double angle) {
       double x = magnitude * Math.cos(angle);
       double y = magnitude * Math.sin(angle);

       return new Complex(x, y);
   }

   /**
    * Returns the angle of the complex number.
    * @return angle of the complex number
    */
   private double getAngle() {
	   double angle = atan2(im, re);
	   if (angle < 0) angle += (2 * Math.PI);
	   return angle;
   }

public double getRe() {
	return re;
}

public double getIm() {
	return im;
}
   
}
