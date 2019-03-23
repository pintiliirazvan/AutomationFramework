package automation.core;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import automation.core.browser.BrowserSession;

/**
 * Class used for waiting on various events to occur
 *
 * @author alexgabor
 *
 */
public class WaitElement {

	/**
	 * Wait for a given {@link ExpectedCondition} to become true
	 *
	 * @param isTrue
	 *        the condition to evaluate until <code>true</code> is returned
	 * @param seconds
	 *        the number or seconds to wait until timeout
	 */
	public void waitUntil(ExpectedCondition<?> isTrue, int seconds) {

		waitUntil(null, isTrue, seconds);
	}

	/**
	 * Wait for a given {@link ExpectedCondition} to become true
	 *
	 * @param message
	 *        the timeout message representing the failure cause or the verification description
	 * @param isTrue
	 *        the condition to evaluate until <code>true</code> is returned
	 * @param seconds
	 *        the number or seconds to wait until timeout
	 */
	public void waitUntil(String message, ExpectedCondition<?> isTrue, int seconds) {

		WebDriver driver = BrowserSession.getInstance().getWebDriver();

		FluentWait<WebDriver> wait = new WebDriverWait(driver, seconds) //
				.withMessage(message)
				.ignoring(NoSuchElementException.class, StaleElementReferenceException.class);

		wait.until(isTrue);
	}

}