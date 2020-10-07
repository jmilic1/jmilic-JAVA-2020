package hr.fer.zemris.java.fractals;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicBoolean;

import hr.fer.zemris.java.fractals.viewer.FractalViewer;
import hr.fer.zemris.java.fractals.viewer.IFractalProducer;
import hr.fer.zemris.java.fractals.viewer.IFractalResultObserver;
import hr.fer.zemris.math.Complex;
import hr.fer.zemris.math.ComplexPolynomial;
import hr.fer.zemris.math.ComplexRootedPolynomial;
import hr.fer.zemris.math.complexParser.ComParser;

/**
 * Program used for viewing a Newton-Raphson fractal of a user-given Complex
 * Number.
 * 
 * @author Jura Milić
 *
 */
public class Newton {
	/**
	 * Threshold used in finding the closest root.
	 */
	public static final double ROOT_THRESHOLD = 0.002;
	/**
	 * Threshold used in checking if a module passed convergence.
	 */
	public static final double CONVERGENCE_THRESHOLD = 0.001;

	/**
	 * Start of program. The user is asked to generate a Complex polynomial by
	 * entering it's roots.
	 * 
	 * @param args not used
	 */
	public static void main(String[] args) {
		System.out.println("Welcome to Newton-Raphson iteration-based fractal viewer.");
		System.out.println("Please enter at least two roots, one root per line. Enter 'done' when done.");

		Scanner sc = new Scanner(System.in);
		int i = 1;

		List<Complex> nums = new ArrayList<Complex>();

		while (true) {
			System.out.printf("Root " + i + "> ");
			String input = sc.nextLine();
			if (input.equals("done")) {
				if (i < 3) {
					System.out.println("Please enter 2 roots first.");
				} else {
					break;
				}
			} else {
				ComParser parser = new ComParser(input);
				nums.add(parser.getComplex());
				i++;
			}
		}
		sc.close();

		Complex[] numbers = new Complex[nums.size()];
		nums.toArray(numbers);

		ComplexRootedPolynomial rooted = new ComplexRootedPolynomial(new Complex(1, 0), numbers);

		FractalViewer.show(new NewtonProducer(rooted));
	}

	/**
	 * An implementation of IFractalProducer used for producing a fractal image.
	 * 
	 * @author Jura Milić
	 *
	 */
	public static class NewtonProducer implements IFractalProducer {
		/**
		 * Roots of the fractal's polynomial
		 */
		private ComplexRootedPolynomial rp;
		/**
		 * Assigns tasks to threads which will execute them
		 */
		private ExecutorService pool;

		/**
		 * Constructs a NewtonProducer with RootedPolynomial set as the given
		 * RootedPolynomial and generates a pool of empty Threads.
		 * 
		 * @param rp given RootedPolynomial
		 */
		public NewtonProducer(ComplexRootedPolynomial rp) {
			this.rp = rp;
			pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors(), new ThreadFactory() {
				@Override
				public Thread newThread(Runnable r) {
					Thread thr = new Thread(r);
					thr.setDaemon(true);
					return thr;
				}
			});
		}

		/**
		 * Produces fractal image
		 */
		@Override
		public void produce(double reMin, double reMax, double imMin, double imMax, int width, int height,
				long requestNo, IFractalResultObserver observer, AtomicBoolean cancel) {
			System.out.println("Zapocinjem izracun...");
			int m = 16 * 16 * 16;
			short[] data = new short[width * height];
			final int brojTraka = 8 * Runtime.getRuntime().availableProcessors();
			int brojYPoTraci = height / brojTraka;

			List<Future<Void>> results = new ArrayList<>();

			for (int i = 0; i < brojTraka; i++) {
				int yMin = i * brojYPoTraci;
				int yMax = (i + 1) * brojYPoTraci - 1;
				if (i == brojTraka - 1) {
					yMax = height - 1;
				}

				CalculateJob job = new CalculateJob(reMin, reMax, imMin, imMax, width, height, yMin, yMax, cancel, rp,
						m, data);

				results.add(pool.submit(job));
			}

			for (Future<Void> posao : results) {
				while (true) {
					try {
						posao.get();
						break;
					} catch (InterruptedException | ExecutionException e) {
					}
				}
			}

			System.out.println("Racunanje gotovo. Idem obavijestiti promatraca tj. GUI!");
			observer.acceptResult(data, (short) (rp.toComplexPolynom().order() + 1), requestNo);
		}
	}

	/**
	 * Calculates fractals
	 * 
	 * @author Jura Milić
	 *
	 */
	public static class CalculateJob implements Callable<Void> {
		/**
		 * Minimum real value.
		 */
		double reMin;
		/**
		 * Maximum real value.
		 */
		double reMax;
		/**
		 * Minimum imaginary value.
		 */
		double imMin;
		/**
		 * Maximum imaginary value.
		 */
		double imMax;
		/**
		 * Width of screen.
		 */
		int width;
		/**
		 * Height of screen.
		 */
		int height;
		/**
		 * Minimum y-value on screen.
		 */
		int yMin;
		/**
		 * Maximum y-value on screen.
		 */
		int yMax;
		/**
		 * Tells whether or not job should be cancelled.
		 */
		AtomicBoolean cancel;
		/**
		 * RootedPolynomial of fractal
		 */
		ComplexRootedPolynomial rp;
		/**
		 * Number of iterations.
		 */
		int m;
		/**
		 * Stores calculated data.
		 */
		short[] data;

		/**
		 * Constructs a CalculateJob with values set accordingly.
		 * 
		 * @param reMin  given minimum real value
		 * @param reMax  given maximum real value
		 * @param imMin  given minimum imaginary value
		 * @param imMax  given maximum imaginary value
		 * @param width  given width of screen
		 * @param height given height of screen
		 * @param yMin   given minimum y-value
		 * @param yMax   given maximum y-value
		 * @param m      given number of iterations
		 * @param data   array where data will be stored
		 * @param cancel given AtomicBoolean
		 * @param rp     given RootedPolynomial
		 */
		public CalculateJob(double reMin, double reMax, double imMin, double imMax, int width, int height, int yMin,
				int yMax, AtomicBoolean cancel, ComplexRootedPolynomial rp, int m, short[] data) {
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
			this.rp = rp;
		}

		/**
		 * Calculates fractal using parameters set in constructor
		 */
		@Override
		public Void call() {
			ComplexPolynomial polynomial = rp.toComplexPolynom();
			ComplexPolynomial derivative = polynomial.derive();

			for (int y = yMin; y <= yMax; y++) {
				if (cancel.get()) {
					break;
				}
				for (int x = 0; x < width; x++) {
					if (cancel.get()) {
						break;
					}
					double re = x / (width - 1.0) * (reMax - reMin) + reMin;
					double im = (height - 1.0 - y) / (height - 1.0) * (imMax - imMin) + imMin;

					Complex zn = new Complex(re, im);
					int iteration = 0;

					Complex znold;
					do {
						Complex numerator = polynomial.apply(zn);
						Complex denominator = derivative.apply(zn);
						znold = zn;
						Complex fraction = numerator.divide(denominator);
						zn = zn.sub(fraction);
						iteration++;
					} while (znold.sub(zn).module() > CONVERGENCE_THRESHOLD && iteration < m);

					int index = rp.indexOfClosestRootFor(zn, ROOT_THRESHOLD);
					data[x + width * y] = (short) (index + 1);
				}
			}
			return null;
		}
	}
}
