package automation.util;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Utility class for manipulating {@link Collection}<code>s</code>
 * 
 * @author alexgabor
 *
 */
public final class CollectionUtil {

	/**
	 * Get a random element from the given list
	 * 
	 * @param list
	 *        the list of elements from which to pick the random
	 * @return
	 */
	public static <T> T getRandomElement(List<T> list) {

		if (isEmpty(list)) {
			return null;
		}

		int size = list.size();

		int index = (int) (size * Math.random());

		return list.get(index);
	}

	/**
	 * Checks whether an element is found within an enumeration of elements
	 * 
	 * @param elt
	 *        the element to check for
	 * @param en
	 *        the enumeration of elements against which to verify
	 * @return <code>true</code> if the value of first parameter is found among the subsequent enumeration
	 */
	@SafeVarargs
	public static <E> boolean isIn(E elt, E... en) {

		boolean equality = false;

		for (E element : en) {

			if (elt == null || element == null) {
				equality = element == elt;
			} else {
				equality = element.equals(elt);
			}

			if (equality) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Check if a list contains duplicates in a null-safe manner. <br>
	 * If the list is empty or <code>null</code>, the result will be <code>false</code>
	 * 
	 * @param collection
	 *        the {@link Collection} to evaluate for duplicates
	 * @return
	 */
	public static boolean containsDuplicates(Collection<?> collection) {

		if (isEmpty(collection)) {
			return false;
		}

		Set<?> set = new HashSet<>(collection);

		return collection.size() != set.size();
	}

	/**
	 * Perform a null-safe check and identify if a Collection is empty or not
	 * 
	 * @param the
	 *        collection to verify
	 * @return
	 * 
	 */
	public static boolean isEmpty(Collection<?> collection) {

		if (collection == null || collection.isEmpty()) {
			return true;
		}

		return false;
	}

	/**
	 * Perform a null-safe check and identify if a {@link Map} is empty or not
	 * 
	 * @param the
	 *        map to verify
	 * @return
	 * 
	 */
	public static boolean isEmpty(Map<?, ?> map) {

		if (map == null || map.isEmpty()) {
			return true;
		}

		return false;
	}

	/**
	 * Check if a collections has a given size. Contains a null-safe check. A <code>null</code> collection is considered to have 0 elements
	 * 
	 * @param collection
	 *        the {@link Collection} for which to check the size
	 * @param size
	 *        the size to verify against
	 */
	public static boolean sizeIs(Collection<?> collection, int size) {

		if (isEmpty(collection)) {

			if (size == 0) {
				return true;
			}

			return false;
		}

		return collection.size() == size;
	}

}
