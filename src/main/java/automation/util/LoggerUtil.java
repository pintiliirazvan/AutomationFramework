package automation.util;

import static automation.core.logging.HtmlLayout.createLayout;
import static automation.util.StringUtil.generateFileNameWithTimestamp;
import static java.lang.Thread.currentThread;
import static org.apache.commons.io.FileUtils.moveFile;
import static org.apache.commons.lang.exception.ExceptionUtils.getStackTrace;
import static org.apache.logging.log4j.core.Filter.Result.ACCEPT;
import static org.apache.logging.log4j.core.Filter.Result.DENY;
import static org.apache.logging.log4j.core.layout.HtmlLayout.FontSize.XSMALL;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.FileAppender;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.apache.logging.log4j.core.filter.MarkerFilter;
import org.junit.runners.model.MultipleFailureException;

import automation.core.logging.HtmlLayout;
import automation.core.logging.MessageLogger;

/**
 * Utility class that contains methods which facilitate the logging of actions
 * 
 * @author alexgabor
 *
 */
public final class LoggerUtil {

	private static final MessageLogger LOG = new MessageLogger(LoggerUtil.class);
	private static final String LINE_SEPARATOR = System.getProperty("line.separator");
	public static final String LOG_FILENAME_PREFIX = "TestResults";
	public static final String LOG_FOLDER = "LOG_FOLDER";
	public static final String HTML_APPENDER_NAME = LOG_FILENAME_PREFIX;
	public static final char CHECK_MARK = '\u2713'; // ✓
	public static final char X_MARK = '\u2716'; // ✖

	private static ThreadLocal<Appender> appenderThreaded = new ThreadLocal<>();

	/**
	 * Creates the Log4j original log file
	 *
	 * @param testName
	 *        the name of the test that will be executed
	 */
	public static void createLogFile(String testName) {

		long threadID = currentThread().getId();

		final LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
		final Configuration config = ctx.getConfiguration();

		String contentType = "text/html";
		Charset charset = Charset.forName("UTF-8");
		String fontSize = XSMALL.getFontSize();
		String font = "arial,sans-serif";
		String pageTitle = "Test Results";

		boolean useLocationInfo = true;

		HtmlLayout layout = createLayout(useLocationInfo, pageTitle, contentType, charset, fontSize, font);

		String logDir = System.getProperty(LOG_FOLDER);
		String fileExtension = "html";
		String baseName = LOG_FILENAME_PREFIX + "_" + testName;
		String newName = generateFileNameWithTimestamp(baseName, fileExtension);

		Filter filter = MarkerFilter.createFilter(String.valueOf(threadID), ACCEPT, DENY);

		Appender appender = FileAppender.newBuilder()
				.withName(HTML_APPENDER_NAME + threadID)
				.withFileName(logDir + "/" + newName)
				.withAppend(false)
				.withIgnoreExceptions(false)
				.withLayout(layout)
				.withFilter(filter)
				.setConfiguration(config)
				.build();

		appender.start();

		config.addAppender(appender);

		LoggerConfig rootLogger = config.getRootLogger();

		rootLogger.addAppender(appender, Level.DEBUG, null);

		ctx.updateLoggers(config);

		appenderThreaded.set(appender);
	}

	/**
	 * Renames the test framework's log file by appending the timestamp suffix (usually called when a test ends)
	 *
	 * @param isSuccessfulTest
	 *        <code>true</code> if the name of the log file should reflect a successful test; <code>false</code> if the name should reflect a failed test
	 */
	public static void renameLogFile(boolean isSuccessfulTest) {
	
		Appender appender = appenderThreaded.get();

		String fileExtension = "html";
		String logDir = System.getProperty(LOG_FOLDER);
		String fileName = ((FileAppender) appender).getFileName();
		String baseName = LOG_FILENAME_PREFIX + fileName.substring(fileName.indexOf("_"), fileName.lastIndexOf("_"));
		String newName = generateFileNameWithTimestamp(baseName, fileExtension);
		String fileNamePrefix = isSuccessfulTest ? "OK_" : "FAILED_";
		String newFileName = fileNamePrefix.concat(newName);
	
		final File fileOriginal = new File(fileName);
		final File fileNewName = new File(logDir, newFileName);
		final boolean isSuccess = fileOriginal.renameTo(fileNewName);
	
		if (!isSuccess) {
			LOG.warn(fileOriginal + " log file was NOT renamed to " + fileNewName);
		}
	}

	/**
	 * Create the log file corresponding to the @BeforeAll method output
	 */
	public static void createBeforeAllLogFile() {
	
		long threadID = currentThread().getId();
	
		final LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
		final Configuration config = ctx.getConfiguration();
	
		String contentType = "text/html";
		Charset charset = Charset.forName("UTF-8");
		String fontSize = XSMALL.getFontSize();
		String font = "arial,sans-serif";
		String pageTitle = "Test Setup";
	
		boolean useLocationInfo = true;
	
		HtmlLayout layout = createLayout(useLocationInfo, pageTitle, contentType, charset, fontSize, font);
	
		String logDir = System.getProperty(LOG_FOLDER);
		String fileExtension = "html";
		String baseName = "Before_All_Setup";
		String newName = generateFileNameWithTimestamp(baseName, fileExtension);
	
		Filter filter = MarkerFilter.createFilter(String.valueOf(threadID), ACCEPT, DENY);
	
		Appender appender = FileAppender.newBuilder()
				.withName(baseName + threadID)
				.withFileName(logDir + "/" + newName)
				.withAppend(false)
				.withIgnoreExceptions(false)
				.withLayout(layout)
				.withFilter(filter)
				.setConfiguration(config)
				.build();
	
		appender.start();
	
		config.addAppender(appender);
	
		LoggerConfig rootLogger = config.getRootLogger();
	
		rootLogger.addAppender(appender, Level.DEBUG, null);
	
		ctx.updateLoggers(config);
	
		appenderThreaded.set(appender);
	}

	/**
	 * Rename the log file corresponding to the @BeforeAll method output
	 */
	public static void renameBeforeAllLogFile() {
	
		String fileExtension = "html";
		String fileName = ((FileAppender) appenderThreaded.get()).getFileName();
		String baseName = fileName.substring(0, fileName.lastIndexOf("_"));
		String newName = generateFileNameWithTimestamp(baseName, fileExtension);
		String fileNameSuffix = "_FINISHED" + "." + fileExtension;
	
		String newFileName = newName.replace("." + fileExtension, fileNameSuffix);
	
		final File fileOriginal = new File(fileName);
		final File fileNewName = new File(newFileName);
	
		try {

			moveFile(fileOriginal, fileNewName);

			LOG.info("Successfully renamed original file.");

		} catch (IOException e) {

			LOG.warn(fileOriginal + " log file was NOT renamed to " + fileNewName);

			logThrowableCause(e);
		}
	
		removeAndStopTestLogger();
	}

	/**
	 * Removes the {@link Logger} appender for tests to detach the data stream from the log file
	 */
	public static void removeAndStopTestLogger() {

		final LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
		final Configuration config = ctx.getConfiguration();

		LoggerConfig rootLogger = config.getRootLogger();
		Map<String, Appender> appendersMap = config.getAppenders();
		Appender appender = appenderThreaded.get();

		if (appender == null) {
			return;
		}

		if (appender.isStarted()) {
			appender.stop();
		}

		appendersMap.values().remove(appender);

		rootLogger.removeAppender(appender.getName());

		ctx.updateLoggers();

		/*
		 * To avoid org.apache.logging.log4j.core.appender.AppenderLoggingException: Error writing to stream <logfile_name>.
		 * Caused by java.io.IOException: Stream Closed
		 */
		appenderThreaded.set(null);
	}

	/**
	 * Logs the error message of the given {@link Throwable} in the test log file
	 *
	 * @param t
	 *        the {@link Throwable} to log
	 */
	public static void logThrowableCause(Throwable t) {
	
		if (!(t instanceof MultipleFailureException)) {
	
			logStackTrace(t);
	
			return;
		}
	
		MultipleFailureException multipleEx = (MultipleFailureException) t;
		List<Throwable> failures = multipleEx.getFailures();
		int failuresCount = failures.size();
	
		for (int i = 0; i < failuresCount; i++) {
	
			LOG.error("FAILURE #" + (i + 1) + ": \n");
	
			logStackTrace(failures.get(i));
		}
	}

	/**
	 * Logs the given {@link Throwable} in the framework log file
	 *
	 * @param t
	 *        the {@link Throwable} to log
	 */
	private static void logStackTrace(Throwable t) {

		Throwable cause = t.getCause();

		if (cause != null) {
			LOG.error(cause.toString());
		}
		
		LOG.error("Stack trace: " + LINE_SEPARATOR + getStackTrace(t));
	}

}
