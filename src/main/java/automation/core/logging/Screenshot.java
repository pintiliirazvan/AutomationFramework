package automation.core.logging;

import static automation.util.LoggerUtil.CHECK_MARK;
import static automation.util.LoggerUtil.HTML_APPENDER_NAME;
import static automation.util.LoggerUtil.LOG_FILENAME_PREFIX;
import static automation.util.LoggerUtil.LOG_FOLDER;
import static automation.util.StringUtil.generateFileNameWithTimestamp;
import static automation.util.StringUtil.isNullOrEmpty;
import static java.lang.Thread.currentThread;
import static org.openqa.selenium.OutputType.FILE;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.FileAppender;
import org.apache.logging.log4j.core.config.Configuration;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import automation.core.browser.BrowserSession;

/**
 * Class in charge with taking screenshots
 * 
 * @author alexgabor
 *
 */
public final class Screenshot {

	private static final MessageLogger LOG = new MessageLogger(Screenshot.class);

	/**
	 * Takes a screenshot and saves it into a {@link File}, then places it into test Logs folder
	 */
	public static void takeScreenshot() {

		WebDriver driver = BrowserSession.getInstance().getWebDriver();

		if (driver == null) {
			return;
		}

		File screenshot = ((TakesScreenshot) driver).getScreenshotAs(FILE);

		String fileName = moveFileToLogsFolder(screenshot);

		LOG.error(" " + CHECK_MARK + " Logged screenshot '" + fileName + "'");
	}

	/**
	 * Move the screenshot to the Logs folder
	 * 
	 * @param screenshot
	 *        the {@link File} representing the screenshot
	 * @return
	 */
	private static String moveFileToLogsFolder(File screenshot) {

		String testName = getTestName();

		testName = isNullOrEmpty(testName) ? "threadId" + currentThread().getId() : testName;

		String baseName = LOG_FILENAME_PREFIX + "_" + testName;
		String fileName = generateFileNameWithTimestamp(baseName, "png");
		String currentWorkingDir = System.getProperty("user.dir");
		String logDir = System.getProperty(LOG_FOLDER);

		Path destPath = Paths.get(currentWorkingDir, logDir, fileName);

		try {

			Files.move(screenshot.toPath(), destPath);

		} catch (IOException e) {
			LOG.error(e.getMessage());
		}

		return fileName;
	}

	/**
	 * Get the test name from Log4j {@link FileAppender}
	 *
	 * @return the current test name
	 */
	private static String getTestName() {
	
		long threadID = currentThread().getId();
	
		final LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
		final Configuration config = ctx.getConfiguration();
		Map<String, Appender> appenders = config.getAppenders();
	
		FileAppender fileApender = (FileAppender) appenders.get(HTML_APPENDER_NAME + threadID);
	
		if (fileApender == null) {
			return "Unknown (possibly test setup)";
		}
	
		String fileName = fileApender.getFileName();

		int beginIndex = fileName.indexOf("_") + 1;
		int endIndex = fileName.lastIndexOf("_");
	
		String testName = fileName.substring(beginIndex, endIndex);
	
		return testName;
	}

}
