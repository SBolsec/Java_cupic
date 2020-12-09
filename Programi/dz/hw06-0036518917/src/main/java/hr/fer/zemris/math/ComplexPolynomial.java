package hr.fer.zemris.math;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Models a polynomial of complex numbers in the form:
 * Zn * Z^n + Z^(n-1) * Zn-1 + ... + Z2 * Z^2 + Z1 * Z + Z0
 * @author sbolsec
 *
 */
public class ComplexPolynomial {
	/** Factors of the polynomial **/
	List<Complex> factors;
	
	/**
	 * Constructor which initializes the factors of the polynomial
	 * @param factors factors of the polynomial
	 */
	public ComplexPolynomial(Complex ... factors) {
		this.factors = new ArrayList<>();
		this.factors.addAll(Arrays.asList(factors));
	}
	
	/**
	 * Returns order of this polynom; eg. For (7+2i)z^3+2z^2+5z+1 returns 3
	 * @return order of this polynom
	 */
	public short order() {
		return (short) (factors.size() - 1);
	}
	
	/**
	 * Computes a new polynomial this*p
	 * @param p polynomial by which to multiply
	 * @return computed polynomial
	 */
	public ComplexPolynomial multiply(ComplexPolynomial p) {
		int size = this.order() + p.order() + 1;
		Complex[] fact = new Complex[size];
		
		for (int i = 0; i < size; i++) {
			fact[i] = Complex.ZERO;
		}
		
		for (int i = 0; i < this.order()+1; i++) {
			for (int j = 0; j < p.order()+1; j++) {
				fact[i+j] = fact[i+j].add(factors.get(i).multiply(p.factors.get(j)));
			}
		}
		
		return new ComplexPolynomial(fact);
	}
	
	/**
	 * Computes first derivative of this polynomial; for example, for
	 * (7+2i)z^3+2z^2+5z+1 returns (21+6i)z^2+4z+5
	 * @return derivative of this polynomial
	 */
	public ComplexPolynomial derive() {
		if (factors.size() == 1)
			return new ComplexPolynomial(Complex.ZERO);
		
		ComplexPolynomial result = null;
		Complex[] fact = new Complex[factors.size()-1];
		
		for (int i = 0; i < factors.size()-1; i++) {
			fact[i] = (factors.get(i+1).multiply(new Complex(i+1, 0)));
		}
		
		result = new ComplexPolynomial(fact);
		
		return result;
	}
	
	/**
	 * Computes polynomial value at given point z
	 * @param z point at which to compute polynomial
	 * @return computed polynomial value at given point z
	 */
	public Complex apply(Complex z) {
		if (factors.size() == 0)
			return null;
		
		Complex result = factors.get(0);
		
		for (int i = 1; i < factors.size(); i++) {
			result = result.add(factors.get(i).multiply(z.power(i)));
		}
		
		return result;
	}
	
	/**
	 * Returns a string representation of the complex polynomial
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		for (int i = factors.size()-1; i > 0; i--) {
			sb.append(factors.get(i)).append("*z^").append(i).append("+");
		}
		sb.append(factors.get(0));
		
		return sb.toString();
	}
	
}
