package hr.fer.zemris.java.fractals.cmplxcache;

import hr.fer.zemris.java.fractals.Complex;

public interface IComplexCache {
	Complex get();
	Complex get(double real, double imaginary);
	Complex get(Complex templete);
	void release(Complex c);
}
