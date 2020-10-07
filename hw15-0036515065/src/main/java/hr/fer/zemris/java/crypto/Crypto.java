package hr.fer.zemris.java.crypto;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Class for generating a SHA digest of a given string.
 * 
 * @author Jura Milić
 */
public class Crypto {
	/**
	 * Computes the SHA digest of given string, and returns the generated digest.
	 * 
	 * @param str given string
	 * @return digest of string
	 * @throws RuntimeException if digest could not be processed
	 */
	public static String getDigest(String str) {
		try {
			MessageDigest sha = MessageDigest.getInstance("SHA-256");

			sha.update(str.getBytes());
			return Util.bytetohex(sha.digest());
		} catch (NoSuchAlgorithmException ex) {
			throw new RuntimeException("Unable to get a MessageDigest instance");
		}
	}
}