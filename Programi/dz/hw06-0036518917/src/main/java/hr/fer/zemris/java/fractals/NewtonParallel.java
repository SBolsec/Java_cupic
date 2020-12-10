package hr.fer.zemris.java.fractals;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import hr.fer.zemris.java.fractals.viewer.FractalViewer;
import hr.fer.zemris.java.fractals.viewer.IFractalProducer;
import hr.fer.zemris.java.fractals.viewer.IFractalResultObserver;
import hr.fer.zemris.math.Complex;
import hr.fer.zemris.math.ComplexPolynomial;
import hr.fer.zemris.math.ComplexRootedPolynomial;

/**
 * Does the work parallel, using more threads and
 * spliting the work between them
 * @author sbolsec
 *
 */
public class NewtonParallel {
	/** Complex rooted polynomial created from the user input **/
	private static ComplexRootedPolynomial polynom;
	/** Complex polynomial created from complex rooted polynomial **/
	private static ComplexPolynomial polynomial;
	/** Complex polynomial that was derived from the complex polynomial **/
	private static ComplexPolynomial derived;
	
	/** Number of workers (threads) **/
	private static int workers;
	/** Number of tracks **/
	private static int tracks;
	
	/** Convergence treshold **/
	private static final double CONVERGENCE_TRESHOLD = 0.001;
	/** Root treshold **/
	private static final double ROOT_TRESHOLD = 0.002;
	/** Maximum number of iterations **/
	private static final short MAX_ITER = 16*16*16;
	
	/**
	 * Starting point of program
	 * @param args command line arguments
	 */
	public static void main(String[] args) {
		configureFromArguments(args);
		
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
		
		polynom = new ComplexRootedPolynomial(Complex.ONE, roots);
		polynomial = polynom.toComplexPolynom();
		derived = polynomial.derive();
		
		System.out.println("Image of fractal will appear shortly. Thank you.");
		
		FractalViewer.show(new MojProducer());	
	}

	/**
	 * Models the actual work, the calculations a thread will perform
	 * @author sbolsec
	 *
	 */
	public static class PosaoIzracuna implements Runnable {
		double reMin;
		double reMax;
		double imMin;
		double imMax;
		int width;
		int height;
		int yMin;
		int yMax;
		int m;
		short[] data;
		AtomicBoolean cancel;
		public static final PosaoIzracuna NO_JOB = new PosaoIzracuna();
		
		private PosaoIzracuna() {
		}
		
		public PosaoIzracuna(double reMin, double reMax, double imMin,
				double imMax, int width, int height, int yMin, int yMax, 
				int m, short[] data, AtomicBoolean cancel) {
			super();
			this.reMin = reMin;
			this.reMax = reMax;
			this.imMin = imMin;
			this.imMax = imMax;
			this.width = width;
			this.height = height;
			this.yMin = yMin;
			this.yMax = yMax;
			this.m = m;
			this.data = data;
			this.cancel = cancel;
		}
		
		/**
		 * Calculates the data for one track
		 */
		@Override
		public void run() {
			System.out.println("Dretva " + Thread.currentThread().getName() + " zapocinje izracun...");
			int offset = yMin*width;
			for(int y = yMin; y <= yMax; y++) {
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
		}
	}
	
	/**
	 * Produces the data needed to draw the fractal
	 * @author sbolsec
	 *
	 */
	public static class MojProducer implements IFractalProducer {
		
		/**
		 * Produces the data that is needed to draw the fractal
		 */
		@Override
		public void produce(double reMin, double reMax, double imMin, double imMax,
				int width, int height, long requestNo, IFractalResultObserver observer, AtomicBoolean cancel) {
			System.out.println("Zapocinjem izracun...");
			int m = MAX_ITER;
			short[] data = new short[width * height];
			final int brojTraka = tracks > height ? height : tracks;
			int brojYPoTraci = height / brojTraka;
			
			System.out.println("Efektivan broj dretvi: " + workers);
			System.out.println("Broj poslova: " + brojTraka);
			
			final BlockingQueue<PosaoIzracuna> queue = new LinkedBlockingQueue<>();

			Thread[] radnici = new Thread[workers];
			for(int i = 0; i < radnici.length; i++) {
				radnici[i] = new Thread(new Runnable() {
					@Override
					public void run() {
						while(true) {
							PosaoIzracuna p = null;
							try {
								p = queue.take();
								if(p==PosaoIzracuna.NO_JOB) break;
							} catch (InterruptedException e) {
								continue;
							}
							p.run();
						}
					}
				});
			}
			for(int i = 0; i < radnici.length; i++) {
				radnici[i].start();
			}
			
			for(int i = 0; i < brojTraka; i++) {
				int yMin = i*brojYPoTraci;
				int yMax = (i+1)*brojYPoTraci-1;
				if(i==brojTraka-1) {
					yMax = height-1;
				}
				PosaoIzracuna posao = new PosaoIzracuna(reMin, reMax, imMin, imMax, width, height, yMin, yMax, m, data, cancel);
				while(true) {
					try {
						queue.put(posao);
						break;
					} catch (InterruptedException e) {
					}
				}
			}
			for(int i = 0; i < radnici.length; i++) {
				while(true) {
					try {
						queue.put(PosaoIzracuna.NO_JOB);
						break;
					} catch (InterruptedException e) {
					}
				}
			}
			
			for(int i = 0; i < radnici.length; i++) {
				while(true) {
					try {
						radnici[i].join();
						break;
					} catch (InterruptedException e) {
					}
				}
			}
			
			System.out.println("Racunanje gotovo. Idem obavijestiti promatraca tj. GUI!");
			observer.acceptResult(data, (short)(polynomial.order() + 1), requestNo);
		}
	}
	
	/**
	 * Configures the number of workers and tracks from command line arguments
	 * @param args command line arguments
	 */
	private static void configureFromArguments(String[] args) {
		if (args.length == 0) {
			workers = Runtime.getRuntime().availableProcessors();
			tracks = 4;
			return;
		}
		
		boolean w = false; // checks whether workers were already changed
		boolean t = false; // checks whether tracks were already changed
		boolean shortForm = false; // checks whether the short form was used first
		
		if (args[0].startsWith("--workers=")) {
			try {
				workers = Integer.parseInt(args[0].substring(args[0].indexOf('=')+1));
				w = true;
			} catch (NumberFormatException e) {
				System.out.println("Number of workers could not be parsed as integer, it was: " + args[0] + ".");
				System.exit(1);
			}
		} else if (args[0].startsWith("--tracks=")) {
			try {
				tracks = Integer.parseInt(args[0].substring(args[0].indexOf('=')+1));
				t = true;
			} catch (NumberFormatException e) {
				System.out.println("Number of tracks could not be parsed as integer, it was: " + args[0] + ".");
				System.exit(1);
			}
		} else if (args[0].equals("-w")) {
			if (args.length < 2) {
				System.out.println("There was no number of workers!");
				System.exit(1);
			}
			try {
				workers = Integer.parseInt(args[1]);
				w = true;
				shortForm = true;
			} catch (NumberFormatException e) {
				System.out.println("Number of workers could not be parsed as integer, it was: " + args[1] + ".");
				System.exit(1);
			}
		} else if (args[0].equals("-t")) {
			if (args.length < 2) {
				System.out.println("There was no number of tracks!");
				System.exit(1);
			}
			try {
				tracks = Integer.parseInt(args[1]);
				t = true;
				shortForm = true;
			} catch (NumberFormatException e) {
				System.out.println("Number of tracks could not be parsed as integer, it was: " + args[1] + ".");
				System.exit(1);
			}
		} else {
			System.out.println("Invalid argument, it was: " + args[0] + ".");
			System.exit(1);
		}
		
		int i = 1;
		if (shortForm) {
			i = 2;
		}
		
		if (args.length <= i) {
			if (!w) workers = Runtime.getRuntime().availableProcessors();
			else if (!t) tracks = 4;
			return;
		}
		
		if (args[i].startsWith("--workers=")) {
			if (w) {
				System.out.println("Number of workers was already set!");
				System.exit(1);
			}
			try {
				workers = Integer.parseInt(args[i].substring(args[i].indexOf('=')+1));
				w = true;
			} catch (NumberFormatException e) {
				System.out.println("Number of workers could not be parsed as integer, it was: " + args[i] + ".");
				System.exit(1);
			}
		} else if (args[i].startsWith("--tracks=")) {
			if (t) {
				System.out.println("Number of tracks was already set!");
				System.exit(1);
			}
			try {
				tracks = Integer.parseInt(args[i].substring(args[i].indexOf('=')+1));
				t = true;
			} catch (NumberFormatException e) {
				System.out.println("Number of tracks could not be parsed as integer, it was: " + args[i] + ".");
				System.exit(1);
			}
		} else if (args[i].equals("-w")) {
			if (w) {
				System.out.println("Number of workers was already set!");
				System.exit(1);
			}
			if (args.length <= i+1) {
				System.out.println("There was no number of workers!");
				System.exit(1);
			}
			try {
				workers = Integer.parseInt(args[i+1]);
				w = true;
				shortForm = true;
			} catch (NumberFormatException e) {
				System.out.println("Number of workers could not be parsed as integer, it was: " + args[i+1] + ".");
				System.exit(1);
			}
		} else if (args[i].equals("-t")) {
			if (t) {
				System.out.println("Number of tracks was already set!");
				System.exit(1);
			}
			if (args.length <= i+1) {
				System.out.println("There was no number of tracks!");
				System.exit(1);
			}
				try {
				tracks = Integer.parseInt(args[i+1]);
				t = true;
				shortForm = true;
			} catch (NumberFormatException e) {
				System.out.println("Number of tracks could not be parsed as integer, it was: " + args[i+1] + ".");
				System.exit(1);
			}
		} else {
			System.out.println("Invalid argument, it was: " + args[i] + ".");
			System.exit(1);
		}
	}
}
