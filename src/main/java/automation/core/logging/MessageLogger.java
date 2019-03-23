package automation.core.logging;

import static java.lang.Thread.currentThread;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.MarkerManager.Log4jMarker;

/**
 * Wrapper class for the logging messages marked with the current thread id
 *
 * @author alexgabor
 * 
 */
public class MessageLogger {

	private Logger logger;

	public MessageLogger(Class<?> clazz) {
		this.logger = LogManager.getLogger(clazz);
	}

	public synchronized void info(String message) {

		logger.info(getCurrentThreadIdMarker(), message);
	}

	public synchronized void warn(String message) {

		logger.warn(getCurrentThreadIdMarker(), message);
	}

	public synchronized void error(String message) {

		logger.error(getCurrentThreadIdMarker(), message);
	}

	public synchronized void debug(String message) {

		logger.debug(getCurrentThreadIdMarker(), message);
	}

	/**
	 * Creates a {@link Log4jMarker} based on the current thread
	 *
	 * @return the {@link Log4jMarker} built on current thread
	 */
	private synchronized static Log4jMarker getCurrentThreadIdMarker() {

		Log4jMarker marker = new Log4jMarker(String.valueOf(currentThread().getId()));

		return marker;
	}

}
