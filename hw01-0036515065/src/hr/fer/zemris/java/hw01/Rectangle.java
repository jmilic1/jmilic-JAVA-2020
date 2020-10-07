package hr.fer.zemris.java.hw01;

import java.util.Scanner;

/**
 * Program calculates area and circumference of given rectangle. Arguments of
 * the rectangle can be read through command line when executing program or
 * through console while the program is running.
 * 
 * @author Jura Milić
 */

public class Rectangle {
	/**
	 * Method that is executed when the program starts. If nothing was given in
	 * command line program will wait for string in console.
	 * 
	 * @param args command line. Can be used instead of typing in console
	 */
	public static void main(String[] args) {
		if (args.length == 0) {
			Scanner sc = new Scanner(System.in);

			double width;

			width = inputVariable(sc, "širinu");

			double height;

			height = inputVariable(sc, "visinu");

			areaCircumference(width, height);

			sc.close();

		} else {
			if (args.length == 2) {
				double sirina = readPositiveDouble(args[0]);
				double visina = readPositiveDouble(args[1]);

				areaCircumference(sirina, visina);
			} else {
				handleError(args);
			}
		}
	}

	/**
	 * Method writes which variable is being read and reads the value given through
	 * console.
	 * 
	 * @param sc scanner that reads number typed into console
	 * @param variableName used to write what variable the program is reading.
	 * @return the number read
	 */
	public static double inputVariable(Scanner sc, String variableName) {
		double variable;

		while (true) {
			System.out.format("Unesite %s > ", variableName);
			String parameter = sc.nextLine().trim();
			
			variable = readPositiveDouble(parameter);
			
			if (variable > 0) {
				break;
			}
		}
		return variable;
	}

	/**
	 * Method handles errors in case the given String array has more than 2
	 * arguments or only 1.
	 * 
	 * @param args the String array we want to check
	 */
	private static void handleError(String[] args) {
		if (args.length == 1) {
			System.out.println("Only one argument was given into command line.");
		}
		if (args.length > 2) {
			System.out.println("More than two arguments were given into command line.");
		}
	}

	/**
	 * Method reads number of double type from text. It also checks if the number is
	 * positive.
	 * 
	 * @param parametar text the method will read
	 * @return value of read number
	 */
	public static double readPositiveDouble(String parametar) {
		double result = 0;
		
		try {
			result = Double.parseDouble(parametar);
			if (result < 0) {
				System.out.println("Unijeli ste negativnu vrijednost.");
			}
			if (result == 0) {
				System.out.println("Unijeli ste nulu.");
			}
		} catch (NumberFormatException ex) {
			System.out.format("'%s' se ne može protumačiti kao broj.%n", parametar);
		}

		return result;
	}

	/**
	 * Method calculates and outputs area and circumference of rectangle assigned by
	 * its width and height
	 * 
	 * @param width width of rectangle
	 * @param height height of rectangle
	 */
	public static void areaCircumference(double width, double height) {
		double area = width * height;
		double circumference = 2 * (width + height);

		System.out.format("Pravokutnik širine %.1f i visine %.1f ima površinu %.1f te opseg %.1f.", width, height,
				area, circumference);
	}

}
