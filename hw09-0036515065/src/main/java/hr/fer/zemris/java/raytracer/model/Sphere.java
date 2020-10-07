package hr.fer.zemris.java.raytracer.model;

/**
 * A class representing a sphere which is defined by the location of it's center
 * and radius. The sphere has color diffusion, color reflection and light
 * reflection components attributed to it.
 * 
 * @author Jura Milić
 *
 */
public class Sphere extends GraphicalObject {
	/**
	 * Vector pointing to center
	 */
	private Point3D center;
	/**
	 * Length of radius
	 */
	private double radius;
	/**
	 * Red diffusion component
	 */
	private double kdr;
	/**
	 * Green diffusion component
	 */
	private double kdg;
	/**
	 * Blue diffusion component
	 */
	private double kdb;
	/**
	 * Red reflective component
	 */
	private double krr;
	/**
	 * Green reflective component
	 */
	private double krg;
	/**
	 * Blue reflective component
	 */
	private double krb;
	/**
	 * Light reflection component
	 */
	private double krn;

	/**
	 * Constructs a Sphere with values set accordingly.
	 * 
	 * @param center given vector pointing to center
	 * @param radius given length of radius
	 * @param kdr    given red diffusion component
	 * @param kdg    given green diffusion component
	 * @param kdb    given blue diffusion component
	 * @param krr    given red reflective component
	 * @param krg    given green reflective component
	 * @param krb    given blue reflective component
	 * @param krn    given reflection component
	 */
	public Sphere(Point3D center, double radius, double kdr, double kdg, double kdb, double krr, double krg, double krb,
			double krn) {
		this.center = center;
		this.radius = radius;
		this.kdr = kdr;
		this.kdg = kdg;
		this.kdb = kdb;
		this.krr = krr;
		this.krg = krg;
		this.krb = krb;
		this.krn = krn;
	}

	/**
	 * Finds the closest point to the origin of given ray which intersects given ray
	 * and this Sphere.
	 * 
	 * @param ray given ray
	 * @return found point or null if point was not found
	 */
	@Override
	public RayIntersection findClosestRayIntersection(Ray ray) {
		Point3D direction = ray.direction;
		Point3D origin = ray.start;

		double b = (direction.scalarProduct(origin.sub(center)));
		double c = Math.pow(origin.sub(center).norm(), 2) - Math.pow(radius, 2);

		double discriminant = Math.pow(b, 2) - c;

		if (discriminant < 0) {
			return null;
		}

		double distance = (-b + Math.sqrt(discriminant));
		double dTmp = (-b - Math.sqrt(discriminant));

		if (Math.abs(distance) > Math.abs(dTmp)) {
			distance = dTmp;
		}

		Point3D point = origin.add(direction.scalarMultiply(distance));

		return new RayIntersection(point, distance, true) {

			@Override
			public Point3D getNormal() {
				return point.sub(center).normalize();
			}

			@Override
			public double getKrr() {
				return krr;
			}

			@Override
			public double getKrn() {
				return krn;
			}

			@Override
			public double getKrg() {
				return krg;
			}

			@Override
			public double getKrb() {
				return krb;
			}

			@Override
			public double getKdr() {
				return kdr;
			}

			@Override
			public double getKdg() {
				return kdg;
			}

			@Override
			public double getKdb() {
				return kdb;
			}
		};

	}

}
