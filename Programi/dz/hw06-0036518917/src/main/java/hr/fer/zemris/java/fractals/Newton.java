package hr.fer.zemris.java.fractals;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

import hr.fer.zemris.java.fractals.viewer.FractalViewer;
import hr.fer.zemris.java.fractals.viewer.IFractalProducer;
import hr.fer.zemris.java.fractals.viewer.IFractalResultObserver;
import hr.fer.zemris.math.Complex;
import hr.fer.zemris.math.ComplexPolynomial;
import hr.fer.zemris.math.ComplexRootedPolynomial;

/**
 * Does everything sequentially on one thread
 * @author sbolsec
 *
 */
public class Newton {

	/**
	 * Starting point of program
	 * @param args command line arguments
	 */
	public static void main(String[] args) {
		System.out.println("Welcome to Newton-Raphson iteration-based fractal viewer.");
		System.out.println("Please enter at least two roots, one root per line. Enter 'done' when done.");
		
		Scanner sc = new Scanner(System.in);
		List<Complex> inputs = new ArrayList<>();
		int count = 1;
		while (true) {
			System.out.format("Root %d> ", count);
			String line = sc.nextLine();
			if (line.equals("done"))
				break;
			try {
				Complex c = ComplexUtil.parse(line);
				inputs.add(c);
				count++;
			} catch (IllegalArgumentException e) {
				System.out.println("Input could not be parsed as complex number!");
			}
		}
		sc.close();
		
		Complex[] roots = new Complex[inputs.size()];
		for (int i = 0; i < inputs.size(); i++) {
			roots[i] = inputs.get(i);
		}
		ComplexRootedPolynomial polynom = new ComplexRootedPolynomial(Complex.ONE, roots);
		
		System.out.println("Image of fractal will appear shortly. Thank you.");
		
		
		FractalViewer.show(new MojProducer(polynom));	
	}
	
	/**
	 * Produces the data needed to draw the fractal
	 * @author sbolsec
	 *
	 */
	public static class MojProducer implements IFractalProducer {
		/** Complex rooted polynomial created from the user input **/
		private ComplexRootedPolynomial polynom;
		/** Complex polynomial created from complex rooted polynomial **/
		private ComplexPolynomial polynomial;
		/** Complex polynomial that was derived from the complex polynomial **/
		private ComplexPolynomial derived;
		
		/** Convergence treshold **/
		private final double CONVERGENCE_TRESHOLD = 0.001;
		/** Root treshold **/
		private final double ROOT_TRESHOLD = 0.002;
		/** Maximum number of iterations **/
		private final short MAX_ITER = 16*16*16;
		
		/**
		 * Constructor which sets the polynoms needed for the calculations
		 * @param polynom
		 */
		public MojProducer(ComplexRootedPolynomial polynom) {
			this.polynom = polynom;
			this.polynomial = polynom.toComplexPolynom();
			this.derived = polynomial.derive();
		}
		
		/**
		 * Produces the data that is needed to draw the fractal
		 */
		@Override
		public void produce(double reMin, double reMax, double imMin, double imMax,
				int width, int height, long requestNo, IFractalResultObserver observer, AtomicBoolean cancel) {
			System.out.println("Zapocinjem izracun...");
			int offset = 0;
			short[] data = new short[width * height];
			for(int y = 0; y < height; y++) {
				if(cancel.get()) break;
				for(int x = 0; x < width; x++) {
					double cre = x / (width-1.0) * (reMax - reMin) + reMin;
					double cim = (height-1.0-y) / (height-1) * (imMax - imMin) + imMin;
					Complex zn = new Complex(cre, cim);
					double module = 0;
					int iters = 0;
					do {
						Complex numerator = polynomial.apply(zn);
						Complex denominator = derived.apply(zn);
						Complex znold = zn;
						Complex fraction = numerator.divide(denominator);
						zn = zn.sub(fraction);
						module = znold.sub(zn).module();
						iters++;
					} while(module > CONVERGENCE_TRESHOLD && iters < MAX_ITER);
					int index = polynom.indexOfClosestRootFor(zn, ROOT_TRESHOLD);
					data[offset++] = (short) (index + 1);
				}
			}
			System.out.println("Racunanje gotovo. Idem obavijestiti promatraca tj. GUI!");
			observer.acceptResult(data, (short)(polynomial.order()+1), requestNo);
		}
	}
}
