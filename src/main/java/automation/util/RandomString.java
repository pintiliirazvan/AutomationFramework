package automation.util;

import java.util.Random;

/**
 * Class used for generating a random alpha-numeric {@link String}
 * 
 * @author alexgabor
 * 
 */
public final class RandomString {

	private static final char[] symbols = new char[36];
	private static final char[] integers = new char[10];
	private static final char[] alphabetic = new char[26];
	
	private final Random random = new Random();

	private final char[] buf;

	static {
		// initialize available chars

		for (int idx = 0; idx < 10; ++idx) {
			symbols[idx] = (char) ('0' + idx);
		}

		for (int idx = 10; idx < 36; ++idx) {
			symbols[idx] = (char) ('a' + idx - 10);
		}

		// -------

		for (int idx = 0; idx < 10; ++idx) {
			integers[idx] = (char) ('0' + idx);
		}

		for (int idx = 0; idx < 26; ++idx) {
			alphabetic[idx] = (char) ('a' + idx);
		}

	}

	public RandomString(int length) {

		if (length < 1) {
			throw new IllegalArgumentException("length < 1: " + length);
		}

		buf = new char[length];
	}

	/**
	 * Return a pseudo-random {@link String} consisted of a sequence of symbols
	 * 
	 * @return
	 */
	public String nextSymbol() {

		for (int idx = 0; idx < buf.length; ++idx) {
			buf[idx] = symbols[random.nextInt(symbols.length)];
		}

		return new String(buf);
	}

	/**
	 * Return a pseudo-random alphabetic {@link String} consisted of a sequence of letters
	 * 
	 * @return
	 */
	public String nextLetter() {

		for (int idx = 0; idx < buf.length; ++idx) {
			buf[idx] = alphabetic[random.nextInt(alphabetic.length)];
		}

		return new String(buf);
	}

	/**
	 * Return a pseudo-random {@link String} consisted of a sequence of integers
	 * 
	 * @return
	 */
	public String nextInt() {

		for (int idx = 0; idx < buf.length; ++idx) {
			buf[idx] = integers[random.nextInt(integers.length)];
		}

		return new String(buf);
	}

}