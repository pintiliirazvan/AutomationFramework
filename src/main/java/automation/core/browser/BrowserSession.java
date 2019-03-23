package automation.core.browser;

import static automation.util.LoggerUtil.CHECK_MARK;
import static automation.util.LoggerUtil.logThrowableCause;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.UnreachableBrowserException;

import automation.core.logging.MessageLogger;
import automation.core.properties.Settings;

/**
 * Class in charge with the setup of a test session
 *
 * @author alexgabor
 *
 */
public class BrowserSession {

	private static final MessageLogger LOG = new MessageLogger(BrowserSession.class);

	private Browser browser;

	private static BrowserSession instance = null;

	private BrowserSession() {

	}

	public static synchronized BrowserSession getInstance() {

		if (instance == null) {
			instance = new BrowserSession();
		}

		return instance;
	}

	/**
	 * Tells if there is a browser instance active
	 *
	 * @return <code>true</code> if there is at least one browser window open; <code>false</code> otherwise
	 */
	public static boolean isBrowserActive() {
		
		try {
	
			WebDriver driver = BrowserSession.getInstance().getWebDriver();
	
			return driver != null && driver.getWindowHandles().size() > 0;
	
		} catch (UnreachableBrowserException e) {
			return false;
		}
	}
	
	/**
	 * Get the {@link Browser} that is used in this session
	 * 
	 * @return
	 */
	public Browser getBrowser() {
		return browser;
	}

	/**
	 * Get the {@link WebDriver} instances
	 * 
	 * @return
	 */
	public WebDriver getWebDriver() {

		if (browser == null) {
			return null;
		}

		return browser.getWebDriver();
	}

	/**
	 * Quits the Selenium driver, closing every associated window
	 */
	public void quitDriver() {

		if (browser == null) {
			return;
		}

		WebDriver driver = browser.getWebDriver();

		try {

			if (driver != null) {
				driver.quit();
			}

		} catch (Exception e) {
			LOG.error("Exception thrown when quitting the WebDriver: \n" + e.getMessage());

			logThrowableCause(e);
		}
	}

	/**
	 * Initialize the test execution and open the browser
	 */
	public void openBrowser() {

		Settings settings = Settings.getInstance();

		openBrowser(settings.getURL());
	}

	/**
	 * Initialize the test execution and open the browser
	 *
	 * @param url
	 *        the application URL
	 */
	private void openBrowser(String url) {

		if (isBrowserActive()) {
			return;
		}

		LOG.info("Initializing...");

		Settings settings = Settings.getInstance();

		Browser browser = new Browser(settings.getBrowserName(), url);

		this.browser = browser.open();

		LOG.info(" " + CHECK_MARK + " Open browser window");
	}

}