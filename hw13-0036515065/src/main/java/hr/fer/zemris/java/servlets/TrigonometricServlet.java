package hr.fer.zemris.java.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * WebServlet which creates a list of values for trigonometric.jsp to use to
 * render a table of sine and cosine values of angles.
 * 
 * @author Jura Milić
 *
 */
@WebServlet("/trigonometric")
public class TrigonometricServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String param = req.getParameter("a");
		int a = 0;

		if (param != null) {
			a = Integer.parseInt(param);
		}

		param = req.getParameter("b");
		int b = 360;
		if (param != null) {
			b = Integer.parseInt(param);
		}

		if (a > b) {
			int temp = b;
			b = a;
			a = temp;
		}

		if (b > a + 720) {
			b = a + 720;
		}

		List<SinCosValue> vals = new ArrayList<SinCosValue>();
		for (int i = a; i <= b; i++) {
			vals.add(new SinCosValue(i));
		}

		req.setAttribute("vals", vals);

		req.getRequestDispatcher("WEB-INF/pages/trigonometric.jsp").forward(req, resp);
	}

	/**
	 * Class which models values of sine and cosine of an angle.
	 * 
	 * @author Jura Milić
	 *
	 */
	public class SinCosValue {
		/**
		 * Sine of angle.
		 */
		private double sinVal;
		/**
		 * Cosine of angle.
		 */
		private double cosVal;
		/**
		 * Value of angle in degrees.
		 */
		private int angle;

		/**
		 * Constructs a SinCosValue using given angle
		 * 
		 * @param angle given in degrees
		 */
		private SinCosValue(int angle) {
			this.angle = angle;
			double deg = angle * Math.PI / 180;
			this.sinVal = Math.sin(deg);
			this.cosVal = Math.cos(deg);
		}

		/**
		 * Gets the sine of angle.
		 * 
		 * @return sinVal
		 */
		public double getSin() {
			return sinVal;
		}

		/**
		 * Gets the cosine of angle.
		 * 
		 * @return cosVal
		 */
		public double getCos() {
			return cosVal;
		}

		/**
		 * Gets the angle.
		 * 
		 * @return angle
		 */
		public int getAngle() {
			return angle;
		}
	}
}
