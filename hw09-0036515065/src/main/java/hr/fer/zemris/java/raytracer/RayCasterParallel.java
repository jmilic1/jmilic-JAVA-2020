package hr.fer.zemris.java.raytracer;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.atomic.AtomicBoolean;

import hr.fer.zemris.java.raytracer.model.GraphicalObject;
import hr.fer.zemris.java.raytracer.model.IRayTracerProducer;
import hr.fer.zemris.java.raytracer.model.IRayTracerResultObserver;
import hr.fer.zemris.java.raytracer.model.LightSource;
import hr.fer.zemris.java.raytracer.model.Point3D;
import hr.fer.zemris.java.raytracer.model.Ray;
import hr.fer.zemris.java.raytracer.model.RayIntersection;
import hr.fer.zemris.java.raytracer.model.Scene;
import hr.fer.zemris.java.raytracer.viewer.RayTracerViewer;

/**
 * Program used for drawing a predefined scene using parallelization.
 * 
 * @author Jura Milić
 *
 */
public class RayCasterParallel {
	/**
	 * Ambient value of red-component in observed scene.
	 */
	private static final int R_AMBIENT = 15;
	/**
	 * Ambient value of green-component in observed scene.
	 */
	private static final int G_AMBIENT = 15;
	/**
	 * Ambient value of blue-component in observed scene.
	 */
	private static final int B_AMBIENT = 15;
	/**
	 * Pool which creates ForkJoinTasks for threads to execute.
	 */
	private static ForkJoinPool pool;

	/**
	 * Class which defines a job for tracing the scene.
	 * 
	 * @author Jura Milić
	 *
	 */
	private static class TraceJob extends RecursiveAction {
		/**
		 * Serial version of job
		 */
		private static final long serialVersionUID = 1L;
		/**
		 * Threshold for determining when the job should be done traced directly.
		 */
		private static final int THRESHOLD = 16;
		/**
		 * Origin of the view-point
		 */
		private Point3D eye;
		/**
		 * Horizontal point of vector on screen
		 */
		private double horizontal;
		/**
		 * Vertical point of vector on screen
		 */
		private double vertical;
		/**
		 * Width of the screen
		 */
		private int width;
		/**
		 * Height of the screen
		 */
		private int height;
		/**
		 * Checks whether the Job should be canceled.
		 */
		private AtomicBoolean cancel;
		/**
		 * Red color components of points.
		 */
		short[] red;
		/**
		 * Green color components of points.
		 */
		short[] green;
		/**
		 * Blue color components of points.
		 */
		short[] blue;
		/**
		 * Vector pointing to the screen corner.
		 */
		Point3D screenCorner;
		/**
		 * Vector representing y-axis.
		 */
		Point3D yAxis;
		/**
		 * Vector representing x-axis.
		 */
		Point3D xAxis;
		/**
		 * Scene of the image
		 */
		Scene scene;
		/**
		 * Minimum value of y.
		 */
		int yMin;
		/**
		 * Maximum value of y.
		 */
		int yMax;

		/**
		 * Constructs a TraceJob with values set accordingly.
		 * 
		 * @param eye          given eye
		 * @param horizontal   given horizontal
		 * @param vertical     given vertical
		 * @param width        given width of screen
		 * @param height       given height of screen
		 * @param cancel       given AtomicBoolean
		 * @param red          given array for red color components
		 * @param green        given array for green color components
		 * @param blue         given array for blue color components
		 * @param screenCorner given screen corner
		 * @param yAxis        given vector for y-Axis
		 * @param xAxis        given vector for x-Axis
		 * @param scene        given scene
		 * @param yMin         given minimum y-value
		 * @param yMax         given maximum y-value
		 */
		protected TraceJob(Point3D eye, double horizontal, double vertical, int width, int height, AtomicBoolean cancel,
				short[] red, short[] green, short[] blue, Point3D screenCorner, Point3D yAxis, Point3D xAxis,
				Scene scene, int yMin, int yMax) {
			this.eye = eye;
			this.horizontal = horizontal;
			this.vertical = vertical;
			this.width = width;
			this.height = height;
			this.cancel = cancel;
			this.red = red;
			this.green = green;
			this.blue = blue;
			this.screenCorner = screenCorner;
			this.scene = scene;
			this.yAxis = yAxis;
			this.xAxis = xAxis;
			this.yMax = yMax;
			this.yMin = yMin;
		}

		/**
		 * Determines whether job should be divided or computed directly.
		 */
		@Override
		protected void compute() {
			if (yMax - yMin + 1 <= THRESHOLD) {
				computeDirect();
				return;
			}

			invokeAll(
					new TraceJob(eye, horizontal, vertical, width, height, cancel, red, green, blue, screenCorner,
							yAxis, xAxis, scene, yMin, yMin + (yMax - yMin) / 2),
					new TraceJob(eye, horizontal, vertical, width, height, cancel, red, green, blue, screenCorner,
							yAxis, xAxis, scene, yMin + (yMax - yMin) / 2 + 1, yMax));
		}

		/**
		 * Directly computes color components of points in the scene
		 */
		private void computeDirect() {
			short[] rgb = new short[3];
			for (int y = yMin; y <= yMax; y++) {
				for (int x = 0; x < width; x++) {
					Point3D screenPoint = screenCorner.add(xAxis.scalarMultiply(horizontal * x / (width - 1)))
							.sub(yAxis.scalarMultiply(vertical * y / (height - 1)));
					Ray ray = Ray.fromPoints(eye, screenPoint);

					tracer(scene, ray, rgb);
					int offset = x + y * width;

					red[offset] = rgb[0] > 255 ? 255 : rgb[0];
					green[offset] = rgb[1] > 255 ? 255 : rgb[1];
					blue[offset] = rgb[2] > 255 ? 255 : rgb[2];
				}
			}
		}

	}

	/**
	 * The start of program
	 * 
	 * @param args not used
	 */
	public static void main(String[] args) {
		pool = new ForkJoinPool();
		RayTracerViewer.show(getIRayTracerProducer(), new Point3D(10, 0, 0), new Point3D(0, 0, 0),
				new Point3D(0, 0, 10), 20, 20);
	}

	/**
	 * Returns an IRayTracerProducer which produces an image of the scene
	 * 
	 * @return IRayTracerProducer implementation
	 */
	private static IRayTracerProducer getIRayTracerProducer() {
		return new IRayTracerProducer() {

			/**
			 * Produces the image
			 */
			@Override
			public void produce(Point3D eye, Point3D view, Point3D viewUp, double horizontal, double vertical,
					int width, int height, long requestNo, IRayTracerResultObserver observer, AtomicBoolean cancel) {
				System.out.println("Započinjem izračune...");
				short[] red = new short[width * height];
				short[] green = new short[width * height];
				short[] blue = new short[width * height];

				Point3D og = view.sub(eye);
				Point3D vuv = viewUp.normalize();

				Point3D zAxis = og.normalize();
				Point3D yAxis = vuv.sub(zAxis.scalarMultiply(zAxis.scalarProduct(vuv))).normalize();
				Point3D xAxis = zAxis.vectorProduct(yAxis).normalize();

				Point3D screenCorner = view.sub(xAxis.scalarMultiply(horizontal / 2))
						.add(yAxis.scalarMultiply(vertical / 2));

				Scene scene = RayTracerViewer.createPredefinedScene();

				pool.invoke(new TraceJob(eye, horizontal, vertical, width, height, cancel, red, green, blue,
						screenCorner, yAxis, xAxis, scene, 0, height - 1));

				pool.shutdown();

				System.out.println("Izračuni gotovi...");
				observer.acceptResult(red, green, blue, requestNo);
				System.out.println("Dojava gotova...");
			}
		};
	}

	/**
	 * Traces the scene using given ray and stores the color components in given
	 * array.
	 * 
	 * @param scene given scene
	 * @param ray   given ray
	 * @param rgb   given array
	 */
	private static void tracer(Scene scene, Ray ray, short[] rgb) {
		rgb[0] = 0;
		rgb[1] = 0;
		rgb[2] = 0;

		RayIntersection closest = findClosestIntersection(scene, ray);

		if (closest == null) {
			return;
		}

		determineColorFor(closest, scene, ray, rgb);
	}

	/**
	 * Finds the closest point to the given ray which intersects both the ray and
	 * any object in given scene
	 * 
	 * @param scene given scene
	 * @param ray   given ray
	 * @return found intersection
	 */
	private static RayIntersection findClosestIntersection(Scene scene, Ray ray) {
		RayIntersection intersection = null;

		for (GraphicalObject obj : scene.getObjects()) {
			RayIntersection tmp = obj.findClosestRayIntersection(ray);

			if (tmp == null) {
				continue;
			}
			if (intersection == null) {
				intersection = tmp;
			} else {
				if (Double.compare(tmp.getDistance(), intersection.getDistance()) < 0) {
					intersection = tmp;
				}
			}
		}
		return intersection;
	}

	/**
	 * Determines the color components of a point that is being looked at by the
	 * given ray. Color depends on the light sources in given scene and the closest
	 * intersection to the given ray. Color components are stored in given array.
	 * 
	 * @param closest given closest intersection
	 * @param scene   given scene
	 * @param ray     given ray
	 * @param rgb     given array
	 */
	private static void determineColorFor(RayIntersection closest, Scene scene, Ray ray, short[] rgb) {
		rgb[0] = R_AMBIENT;
		rgb[1] = G_AMBIENT;
		rgb[2] = B_AMBIENT;

		for (LightSource ls : scene.getLights()) {
			Point3D lightPosition = ls.getPoint();
			Ray lightRay = Ray.fromPoints(lightPosition, closest.getPoint());

			RayIntersection inter = findClosestIntersection(scene, lightRay);

			if (inter != null && lightPosition.sub(closest.getPoint()).norm() - inter.getDistance() > 1E-5) {
				continue;
			}

			Point3D l = lightRay.direction.negate().normalize();
			Point3D n = closest.getNormal().normalize();
			double ln = l.scalarProduct(n);

			rgb[0] += closest.getKdr() * ls.getR() * Math.max(ln, 0);
			rgb[1] += closest.getKdg() * ls.getG() * Math.max(ln, 0);
			rgb[2] += closest.getKdb() * ls.getB() * Math.max(ln, 0);

			Point3D r = n.scalarMultiply(2 * l.scalarProduct(n)).sub(l).normalize();
			Point3D v = ray.start.sub(closest.getPoint()).normalize();

			double coef = Math.pow(r.scalarProduct(v), closest.getKrn());

			if (coef <= 0) {
				continue;
			}

			rgb[0] += ls.getR() * closest.getKrr() * coef;
			rgb[1] += ls.getG() * closest.getKrg() * coef;
			rgb[2] += ls.getB() * closest.getKrb() * coef;
		}
	}
}
