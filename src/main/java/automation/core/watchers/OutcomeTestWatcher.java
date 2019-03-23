package automation.core.watchers;

import static automation.core.logging.Screenshot.takeScreenshot;
import static automation.util.LoggerUtil.CHECK_MARK;
import static automation.util.LoggerUtil.X_MARK;
import static automation.util.LoggerUtil.logThrowableCause;
import static automation.util.LoggerUtil.removeAndStopTestLogger;
import static automation.util.LoggerUtil.renameLogFile;

import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.openqa.selenium.WebDriver;

import automation.core.logging.MessageLogger;

/**
 * Generic {@link TestWatcher} used to handle termination of test execution (e.g. quitting {@link WebDriver}, taking screenshot)
 * 
 * @author alexgabor
 *
 */
public class OutcomeTestWatcher extends TestWatcher {

	private static final MessageLogger LOG = new MessageLogger(OutcomeTestWatcher.class);

	@Override
	protected void failed(Throwable e, Description desc) {

		takeScreenshot();

		LOG.error(" " + X_MARK + " Assert failed: ");
		LOG.error("Test Name: " + desc.getDisplayName());

		logThrowableCause(e);

		renameLogFile(false);
	}

	@Override
	protected void succeeded(Description desc) {

		LOG.info(" " + CHECK_MARK + " Asserts passed: ");
		LOG.info("Test Name: " + desc.getDisplayName());

		renameLogFile(true);
	}

	@Override
	protected void finished(Description desc) {

		LOG.info("Quitting the driver...");

		removeAndStopTestLogger(); // needed after renaming the log file (logs will no longer be written)

		LOG.info("Done.");
	}

}
