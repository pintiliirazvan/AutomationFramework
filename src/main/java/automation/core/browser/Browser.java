package automation.core.browser;

import static automation.util.LoggerUtil.CHECK_MARK;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.openqa.selenium.UnexpectedAlertBehaviour.ACCEPT;
import static org.openqa.selenium.remote.CapabilityType.LOGGING_PREFS;
import static org.openqa.selenium.remote.CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR;

import java.io.File;
import java.util.logging.Level;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriver.Timeouts;
import org.openqa.selenium.WebDriver.Window;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;

import automation.core.listeners.TestEventListener;
import automation.core.logging.MessageLogger;
import automation.core.properties.Settings;

/**
 * Class that acts as a browser handler
 *
 * @author alexgabor
 *
 */
public class Browser {

    private static final MessageLogger LOG = new MessageLogger(Browser.class);

    private WebDriver driver;
    private BrowserName browserName;
    private String url;

    public Browser(BrowserName browserName, String url) {
        this.browserName = browserName;
        this.url = url;
    }

    /**
     * Sets up and then opens the {@link BrowserName} based on the configuration file (see {@link Settings})
     *
     * @return the current {@link Browser} instance
     */
    public Browser open() {

        switch (browserName) {
        case FIREFOX:

            setupFirefoxDriver();

            break;

        case CHROME:

            setupChromeDriver();

            break;

        case SAFARI:

            driver = new SafariDriver();

            break;

        default:

            setupFirefoxDriver();

            break;
        }

        configureTimeouts();

        maximizeBrowserWindow();

        driver.get(url); // load the provided URL address

        registerEventListener();

        return this;
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
     * Set the {@link WebDriver} instance
     *
     * @param webDriver
     *        the {@link WebDriver} to set
     */
    public void setWebDriver(WebDriver webDriver) {
        this.driver = webDriver;
    }

    /**
     * Get the {@link BrowserName}
     *
     * @return
     */
    public BrowserName getBrowserName() {
        return browserName;
    }

    /**
     * Close the current window, quitting the browser if it's the last window currently open
     */
    public void closeBrowser() {
        driver.close();
    }

    /**
     * Initializes the Firefox Driver
     */
    private void setupFirefoxDriver() {
   
        File gekoDriver = new File("./lib/geckodriver");
   
        System.setProperty("webdriver.gecko.driver", gekoDriver.getAbsolutePath());
   
        driver = new FirefoxDriver(getFirefoxOptions());
    }

    /**
     * Initializes the Chrome Driver
     */
    private void setupChromeDriver() {
   
        File chromeDriver = new File("./lib/chromedriver");
   
        System.setProperty("webdriver.chrome.driver", chromeDriver.getAbsolutePath());
   
        driver = new ChromeDriver(getChromeOptions());
    }

    /**
     * Register a {@link TestEventListener} for the current {@link WebDriver} instance
     */
    private void registerEventListener() {
   
        TestEventListener eventListener = new TestEventListener();
   
        EventFiringWebDriver eventWebDriver = new EventFiringWebDriver(driver);
   
        EventFiringWebDriver eventFiringDriver = eventWebDriver.register(eventListener);
   
        eventFiringDriver.manage().deleteAllCookies();
   
        driver = eventFiringDriver;
   
        LOG.info(" " + CHECK_MARK + " Register event listener");
    }

    /**
     * Maximize the browser window
     */
    private void maximizeBrowserWindow() {
   
        Window browserWindow = driver.manage().window();
   
        browserWindow.maximize();
    }

    /**
     * Configure the {@link WebDriver} timeouts
     */
    private void configureTimeouts() {
   
        Timeouts timeouts = driver.manage().timeouts();
   
        timeouts.pageLoadTimeout(80, SECONDS);
        timeouts.setScriptTimeout(30, SECONDS);
    }

    /**
     * Creates the {@link FirefoxOptions} need for creating a {@link FirefoxDriver}
     */
    private static FirefoxOptions getFirefoxOptions() {
   
        FirefoxProfile profile = new FirefoxProfile();
        FirefoxOptions options = new FirefoxOptions();

        // File firebugPath = new File("./lib/firebug-2.0.19.xpi");
        // File firepathPath = new File("./lib/firepath-0.9.7.1-fx.xpi");
        //
        // profile.addExtension(firebugPath);
        // profile.addExtension(firepathPath);

        profile.setAssumeUntrustedCertificateIssuer(false);

        // profile.setPreference("extensions.firebug.currentVersion", "2.0.19");
        // profile.setPreference("extensions.firepath.currentVersion", "0.9.7.1");
        profile.setPreference("browser.download.folderList", 2);
        profile.setPreference("browser.download.manager.showWhenStarting", false);
        profile.setPreference("browser.download.useDownloadDir", true);
        // profile.setPreference("browser.download.dir", "/downloads");
        profile.setPreference("browser.helperApps.neverAsk.saveToDisk", "text/csv, application/vnd.ms-excel, application/excel, application/vnd.openxmlformats-officedocument.spreadsheetml.sheet, application/zip, text/plain");
        profile.setPreference("browser.helperApps.alwaysAsk.force", false);
        profile.setPreference("browser.tabs.loadInBackground", true);
        profile.setPreference("focusmanager.testmode", true); // handle focus & blur events when the browser window doesn't have focus

        options.setCapability(FirefoxDriver.PROFILE, profile);
        options.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, ACCEPT);
        // options.setCapability("marionette", false);

        return options;
    }

    /**
     * Creates the {@link ChromeOptions} need for creating a {@link ChromeDriver}
     *
     * @return
     */
    private static ChromeOptions getChromeOptions() {

        LoggingPreferences logPreferences = new LoggingPreferences();
        ChromeOptions options = new ChromeOptions();

        logPreferences.enable(LogType.BROWSER, Level.ALL);

        options.addArguments("disable-infobars");
        options.addArguments("--start-maximized");

        options.setCapability(LOGGING_PREFS, logPreferences);
        options.setCapability(UNEXPECTED_ALERT_BEHAVIOUR, ACCEPT);
   
        return options;
    }

}

}