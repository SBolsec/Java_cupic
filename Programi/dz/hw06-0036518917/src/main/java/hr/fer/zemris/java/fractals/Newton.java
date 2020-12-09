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

public class Newton {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("Welcome to Newton-Raphson iteration-based fractal viewer.");
		System.out.println("Please enter at least two roots, one root per line. Enter 'done' when done.");
		
		Scanner sc = new Scanner(System.in);
		List<String> inputs = new ArrayList<>();
		int count = 0;
		while (true) {
			System.out.format("Root %d> ", ++count);
			String line = sc.nextLine();
			if (line.equals("done"))
				break;
			inputs.add(line);
		}
		sc.close();
		
		Complex[] roots = new Complex[inputs.size()];
		try {
			for (int i = 0; i < inputs.size(); i++) {
				roots[i] = ComplexUtil.parse(inputs.get(i));
			}
		} catch (IllegalArgumentException e) {
			System.out.println("Input was not a complex number!");
			System.exit(1);
		}
		ComplexRootedPolynomial polynom = new ComplexRootedPolynomial(Complex.ONE, roots);
		
		System.out.println("Image of fractal will appear shortly. Thank you.");
		
		
		FractalViewer.show(new MojProducer(polynom));	
	}
	
	public static class MojProducer implements IFractalProducer {
		
		private ComplexRootedPolynomial polynom;
		private ComplexPolynomial polynomial;
		private ComplexPolynomial derived;
		
		private final double CONVERGENCE_TRESHOLD = 0.001;
		private final double ROOT_TRESHOLD = 0.002;
		private final short MAX_ITER = 16*16*16;
		
		public MojProducer(ComplexRootedPolynomial polynom) {
			this.polynom = polynom;
			this.polynomial = polynom.toComplexPolynom();
			this.derived = polynomial.derive();
		}
		
		
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
