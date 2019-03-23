package automation.core;

import static automation.util.LoggerUtil.LOG_FOLDER;
import static automation.util.LoggerUtil.createLogFile;
import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.rules.TestName;
import org.junit.rules.TestRule;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import automation.core.browser.BrowserSession;
import automation.core.logging.MessageLogger;
import automation.core.watchers.OutcomeTestWatcher;

/**
 * Class that manages the start-up and tear down of the automated tests
 *
 * @author alexgabor
 *
 */
public class SeleneseTest {

	private static final MessageLogger LOG = new MessageLogger(SeleneseTest.class);

	private static SeleneseTest singleton;

	public static synchronized SeleneseTest getInstance() {

		if (singleton == null) {
			singleton = new SeleneseTest();
		}

		return singleton;
	}

	private WebDriver driver;

	protected SeleneseTest() {
	}

	/**
	 * Before class setup for the test classes
	 */
	@BeforeClass
	public static void setupClass() {

		System.setProperty(LOG_FOLDER, "Logs");
	}

	/**
	 * After class tear down - close browser
	 */
	@AfterClass
	public static void tearDownClass() {

		BrowserSession.getInstance().quitDriver();
	}

	@Rule
	public TestName testNameRule = new TestName();

	@Rule
	public TestRule quitRule = new OutcomeTestWatcher(); // must stay the last @Rule defined in this class!

	/**
	 * Set up and start the execution of a test method
	 */
	@Before
	public void setup() {
		
		setupLogs(testNameRule.getMethodName());

		BrowserSession.getInstance().openBrowser();

		driver = BrowserSession.getInstance().getWebDriver();

		acceptCookies();
	}

	/**
	 * Tear down executed after each test method
	 */
	@After
	public void tearDown() {

		tearDownLogs(testNameRule.getMethodName());
	}
	
	/**
	 * Get the {@link WebDriver} instance
	 * 
	 * @return
	 */
	public WebDriver getWebDriver() {
		return driver;
	}

	/**
	 * Closes the footer bar message by accepting the cookies
	 */
	private void acceptCookies() {

		By btnAcceptCookies = By.id("cn-accept-cookie");

		if (driver.findElements(btnAcceptCookies).isEmpty()) {
			return;
		}

		new WaitElement().waitUntil(elementToBeClickable(btnAcceptCookies), 10);

		driver.findElement(btnAcceptCookies).click();
	}

	/**
	 * Sets up the log file and logs that test has begun
	 * 
	 * @param testName
	 *        the name of the test method that is executed
	 */
	public static void setupLogs(String testName) {

		createLogFile(testName); // re-create the Log file for each test (old log gets renamed)

		LOG.info("############# Starting Test: " + testName + " #############");
	}

	/**
	 * Print test failures and logs that test ended
	 *
	 * @param testName
	 *        the name of the test method that is executed
	 */
	public static void tearDownLogs(String testName) {

		LOG.info("############# Finished Test: " + testName + " #############");
	}

}
