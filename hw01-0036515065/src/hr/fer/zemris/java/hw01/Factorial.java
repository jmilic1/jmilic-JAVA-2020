package hr.fer.zemris.java.hw01;

import java.util.Scanner;

/**
 * Program reads number typed through keyboard and calculates its factorial.
 * Number must be in range [3, 20].
 * 
 * @author Jura Milić
 */

public class Factorial {
	/**
	 * Method that is executed when the program starts. If the string typed through
	 * console can be read as an integer it calculates the number's factorial.
	 * 
	 * @param args not used
	 */
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);

		while (true) {
			System.out.format("Unesite broj > ");
			if (sc.hasNextInt()) {
				hasInt(sc);
			} else {
				String elem = sc.next();
				if (elem.equals("kraj")) {
					System.out.println("Doviđenja.");
					break;
				} else {
					System.out.format("'%s' nije cijeli broj.%n", elem);
				}
			}
		}

		sc.close();
	}

	/**
	 * Method checks if given number is in range [3, 20] and calls method for
	 * calculating factorial.
	 * 
	 * @param sc scanner that reads number typed into console
	 */
	private static void hasInt(Scanner sc) {
		int elem = sc.nextInt();

		if (elem >= 3 && elem <= 20) {
			long result = calculateFactorial(elem);
			
			System.out.format("%d! = %d%n", elem, result);
		} else {
			System.out.format("'%d' nije broj u dozvoljenom rasponu.%n", elem);
		}
	}

	/**
	 * Method calculates factorial of number x.
	 * 
	 * @param elem argument x
	 * @return value of expression (x!).
	 * @throws IllegalArgumentException if number is not within range [0, 20].
	 */
	public static long calculateFactorial(int elem) {
		if (elem < 0 || elem > 20) {
			throw new IllegalArgumentException("Factorial of given number cannot be displayed.");
		}

		long result = 1;

		while (elem != 1) {
			result = result * elem;
			elem--;
		}

		return result;
	}
}
