package hr.fer.zemris.math;

import static java.lang.Math.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Models a polynomial of complex numbers in the form:
 * Z0*(Z-Z1)*(Z-Z2)*...*(Z-Zn)
 * @author sbolsec
 *
 */
public class ComplexRootedPolynomial {
	/** Constant complex number (first one) **/
	private Complex constant;
	/** Roots of the polynomial **/
	private List<Complex> roots;
	
	/**
	 * Constructor which initializes the constant and the roots of the polynomial
	 * @param constant constant of the polynomial
	 * @param roots roots of the polynomial
	 */
	public ComplexRootedPolynomial(Complex constant, Complex ... roots) {
		this.constant = constant;
		this.roots = new ArrayList<>();
		this.roots.addAll(Arrays.asList(roots));
	}
	
	/**
	 * Computes polynomial value at given point z
	 * @param z point at which to compute polynomial
	 * @return computed polynomial value at given point z
	 */
	public Complex apply(Complex z) {
		Complex result = constant;
		
		for (Complex c : roots) {
			result = result.multiply(z.add(c.negate()));
		}
		
		return result;
	}
	
	/**
	 * Converts this representation to ComplexPolynomial type
	 * @return this representation converted to ComplexPolynomial type
	 */
	public ComplexPolynomial toComplexPolynom() {
		ComplexPolynomial result = new ComplexPolynomial(constant);
		
		for (Complex root : roots) {
			result = result.multiply(new ComplexPolynomial(root, Complex.ONE));
		}
		
		return result;
	}
	
	/**
	 * Returns a string representation of the complex rooted polynomial
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append(constant.toString());
		for (Complex c : roots) {
			sb.append("*(z-" + c.toString() + ")");
		}
		
		return sb.toString();
	}
	
	/**
	 * Find index of closest root for given complex number z that is within
	 * treshold; if there is no such root, return -1
	 * First root has index 0, second index 1, etc
	 * @param z complex number for which to search
	 * @param treshold threshold to use while searching for index
	 * @return index of closest root for given complex number that is within treshold
	 */
	public int indexOfClosestRootFor(Complex z, double treshold) {
		int index = -1;
		double min = Double.MAX_VALUE;
		double treshold2 = pow(treshold, 2);
		
		for (int i = 0; i < roots.size(); i++) {
			Complex root = roots.get(i);
			double value = pow(root.getRe() - z.getRe(), 2) + pow(root.getIm() - z.getIm(), 2);
			if (value < min && value <= treshold2) {
				min = value;
				index = i;
			}
		}
		
		return index;
	}
}
