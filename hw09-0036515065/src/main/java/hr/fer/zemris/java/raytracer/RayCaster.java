package hr.fer.zemris.java.raytracer;

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
 * Program used for drawing a predefined scene.
 * 
 * @author Jura Milić
 *
 */
public class RayCaster {
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
	 * The start of program
	 * 
	 * @param args not used
	 */
	public static void main(String[] args) {

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

				short[] rgb = new short[3];
				int offset = 0;
				for (int y = 0; y < height; y++) {
					for (int x = 0; x < width; x++) {
						Point3D screenPoint = screenCorner.add(xAxis.scalarMultiply(horizontal * x / (width - 1)))
								.sub(yAxis.scalarMultiply(vertical * y / (height - 1)));
						Ray ray = Ray.fromPoints(eye, screenPoint);

						tracer(scene, ray, rgb);

						red[offset] = rgb[0] > 255 ? 255 : rgb[0];
						green[offset] = rgb[1] > 255 ? 255 : rgb[1];
						blue[offset] = rgb[2] > 255 ? 255 : rgb[2];

						offset++;
					}
				}
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
