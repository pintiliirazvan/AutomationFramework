package automation.util;

import static java.util.Calendar.HOUR_OF_DAY;
import static java.util.Calendar.MILLISECOND;
import static java.util.Calendar.MINUTE;
import static java.util.Calendar.SECOND;
import static java.util.Calendar.getInstance;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Calendar;
import java.util.Collection;
import java.util.Random;

/**
 * Utility class for manipulating {@link String}<code>s</code>
 * 
 * @author alexgabor
 * 
 */
public final class StringUtil {

	private StringUtil() {
	}

	/**
	 * Checks if a given {@link String} for <code>null</code> or emptiness
	 * 
	 * @param s
	 *        the {@link String} to be checked for emptiness
	 * @return <code>true</code> if the given {@link String} is <code>null</code> or is empty; <code>false</code> otherwise
	 */
	public static boolean isNullOrEmpty(String s) {
		return s == null || s.isEmpty();
	}

	/**
	 * Verifies if a text matches a given pattern. <br>
	 * - #' for numbers <br>
	 * - '@' for letters <br>
	 * - '$' for letter or number <br>
	 * - other chars will be considered as they are (e.g. "x" will be compared to "x") <br>
	 * 
	 * @param candidate
	 *        the {@link String} against which to verify the pattern
	 * @param pattern
	 *        the pattern/format to use for <code>candidate</code> verification
	 * @return <code>true</code> if the given string candidate matches the given pattern; <code>false</code> otherwise
	 */
	public static boolean isMatchPattern(String candidate, String pattern) {

		if (candidate.length() != pattern.length()) {
			return false;
		}

		char[] candidateChars = candidate.toCharArray();
		char[] templateChars = pattern.toCharArray();

		for (int i = 0; i < templateChars.length; i++) {

			char templateChar = templateChars[i];
			char candidateChar = candidateChars[i];

			switch (templateChar) {
			case '@':

				if (!Character.isLetter(candidateChar)) {
					return false;
				}

				break;

			case '#':

				if (!Character.isDigit(candidateChar)) {
					return false;
				}

				break;

			case '$':

				if (!Character.isLetter(candidateChar) && !Character.isDigit(candidateChar)) {
					return false;
				}

				break;

			default:

				if (templateChar != candidateChar) {
					return false;
				}

				break;
			}

		}

		return true;
	}

	/**
	 * Reads the file content into a {@link String}
	 * 
	 * @param filePath
	 *        the file path to read from
	 * @return the {@link String} consisted of the file content
	 * @throws IOException
	 */
	public static String readFromFile(String filePath) throws IOException {

		BufferedReader br = new BufferedReader(new FileReader(filePath));
		String everything;

		try {

			StringBuilder sb = new StringBuilder();
			String line = br.readLine();

			while (line != null) {

				sb.append(line);
				sb.append(System.lineSeparator());

				line = br.readLine();

			}

			everything = sb.toString();

		} finally {
			br.close();
		}

		return everything;
	}

	/**
	 * Return a {@link String} with the elements separated by a given delimiter
	 * 
	 * @param collection
	 *        the {@link Collection} to transform
	 * @param delimiter
	 *        the delimiter to user for between the elements
	 * @return
	 */
	public static String listToString(Collection<? extends Object> collection, String delimiter) {

		StringBuilder sb = new StringBuilder();

		for (Object element : collection) {
			sb.append(element.toString() + delimiter);
		}

		return sb.toString();
	}

	/**
	 * Generates a random alpha-numeric String of a given length
	 * 
	 * @param length
	 *        the length of the String that will be generated
	 * @return a random alpha-numeric String of a given length
	 */
	public static String generateRandomString(int length) {
	
		RandomString rnd = new RandomString(length);
	
		return rnd.nextSymbol();
	}

	/**
	 * Generates a matching {@link String}, according to the given pattern/format. <br>
	 * - #' for numbers <br>
	 * - '@' for letters <br>
	 * - '$' for letter or number <br>
	 * - other chars will be considered as they are (e.g. "x" will output "x")
	 * 
	 * @param pattern
	 *        the pattern/format according to which to generate the {@link String}
	 * @return the randomly generated {@link String}
	 */
	public static String generateRandomString(String pattern) {
	
		String value = "";
		char[] templateChars = pattern.toCharArray();
		Random random = new Random();
	
		for (char c : templateChars) {
	
			String randomLetter = new RandomString(1).nextLetter();
			String randomInt = new RandomString(1).nextInt();
	
			switch (c) {
			case '@':
	
				value += randomLetter;
	
				break;
	
			case '#':
	
				value += randomInt;
	
				break;
	
			case '$':
	
				value += (random.nextBoolean()) ? randomLetter : randomInt;
	
				break;
	
			default:
	
				value += c;
	
				break;
			}
		}
	
		return value;
	}

	/**
	 * Generates a File Name based on the current timestamp and a given file extension
	 *
	 * @param baseName
	 *        The basic (head title) of the file name
	 * @param fileExtension
	 *        The extension of the file name
	 * @return the filename in format <code>baseName_day_of_month_hour24_minute_second.fileExtension</code>
	 */
	public static String generateFileNameWithTimestamp(String baseName, String fileExtension) {
	
		Calendar cal = getInstance();
	
		return baseName + "_h" + cal.get(HOUR_OF_DAY) + "m" + cal.get(MINUTE) + "s" + cal.get(SECOND) + "ms" + cal.get(MILLISECOND) + "." + fileExtension;
	}

}