package automation.util;

import static org.openqa.selenium.Keys.CONTROL;
import static org.openqa.selenium.Keys.END;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Point;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import automation.core.browser.BrowserSession;
import automation.core.logging.MessageLogger;

/**
 * Contains methods for simulating various events
 * 
 * @author alexgabor
 * 
 */
public final class Event {

	private static final MessageLogger LOG = new MessageLogger(Event.class);

	private Event() {
	}

	/**
	 * Simulation of mouse scroll down or up
	 * 
	 * @param howMuch
	 *        direction and level of scrolling (e.g. 200 scrolls down, -200 scrolls up)
	 */
	public static void scrollVertically(int howMuch) {

		String direction = howMuch > 0 ? "down" : "up";

		LOG.info("Scrolling " + direction + " by " + howMuch + "px");

		JavascriptExecutor js = (JavascriptExecutor) BrowserSession.getInstance().getWebDriver();

		js.executeScript("window.scrollBy(0," + howMuch + ")");
	}

	/**
	 * Simulation of mouse scroll left or right
	 * 
	 * @param howMuch
	 *        direction and level of scrolling (e.g. 200 scrolls right, -200 scrolls left)
	 */
	public static void scrollHorizontally(int howMuch) {

		String direction = howMuch > 0 ? "right" : "left";

		LOG.info("Scrolling " + direction + " by " + howMuch + "px");

		JavascriptExecutor js = (JavascriptExecutor) BrowserSession.getInstance().getWebDriver();

		js.executeScript("window.scrollBy(" + howMuch + ",0)");
	}

	/**
	 * Scrolls to the given element
	 * 
	 * @param locator
	 *        {@link By} locator (the object to scroll to)
	 */
	public static void scrollTo(By locator) {

		WebDriver driver = BrowserSession.getInstance().getWebDriver();
		WebElement element = driver.findElement(locator);

		try {
			scrollTo(element);
		} catch (StaleElementReferenceException e) {
			scrollTo(driver.findElement(locator));
		}
	}

	/**
	 * Scrolls to the given element, avoiding the Main Menu bar overlay that may overlap the {@link WebElement}
	 * 
	 * @param element
	 *        the {@link WebElement} (the object to scroll to)
	 */
	public static void scrollTo(WebElement element) {

		/*
		 * Need to scroll into view relative to the Main Menu navigation bar, which is an overlay.
		 * This causes situations where the element is under this overlay, thus not being clickable on Chrome.
		 */

		WebDriver driver = BrowserSession.getInstance().getWebDriver();
		List<WebElement> menuBars = driver.findElements(By.id("header"));

		if (menuBars.isEmpty()) {
			return;
		}

		WebElement mainMenuBar = menuBars.get(0);

		if (!mainMenuBar.isDisplayed()) {
			return;
		}

		new Actions(driver).moveToElement(element).build().perform();

		Point menuLocation = mainMenuBar.getLocation();
		Point elementLocation = element.getLocation();

		int viewPortHeight = getViewPortHeight();
		int mainMenuBarHeight = mainMenuBar.getSize().getHeight();
		int elementHeight = element.getSize().getHeight();
		int offset = elementLocation.getY() - menuLocation.getY();
		int elementToMenuBarDistance = viewPortHeight - elementHeight - mainMenuBarHeight;

		if (offset > mainMenuBarHeight && offset < elementToMenuBarDistance) {

			// no need to scroll

			return;
		}

		int safetyMargin = offset > 0 ? 5 : -5;
		int howMuch = 0;

		if (offset < mainMenuBarHeight) {

			howMuch = offset - mainMenuBarHeight + safetyMargin;

		} else if (offset > elementToMenuBarDistance) {

			howMuch = Math.abs(offset - elementToMenuBarDistance) + safetyMargin;
		}

		scrollVertically(howMuch);
	}

	/**
	 * Scroll to the given element
	 * 
	 * @param locator
	 *        {@link WebElement} locator (the object to scroll to)
	 */
	public static void scrollToJS(By locator) {

		WebDriver driver = BrowserSession.getInstance().getWebDriver();

		scrollToJS(driver.findElement(locator));
	}

	/**
	 * Scroll to the given element
	 * 
	 * @param element
	 *        {@link WebElement} to scroll to
	 */
	public static void scrollToJS(WebElement element) {

		Point hoverItem = element.getLocation();

		JavascriptExecutor js = (JavascriptExecutor) BrowserSession.getInstance().getWebDriver();

		js.executeScript("window.scrollBy(0," + hoverItem.getY() + ");");

		// or ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", element);
	}

	/**
	 * Simulation of mouse scroll, down to the bottom of the page
	 */
	public static void scrollToBottom() {

		// 1st manner
		Actions actions = new Actions(BrowserSession.getInstance().getWebDriver());

		actions.keyDown(CONTROL).sendKeys(END).perform();

		// 2nd manner (in case the 1st didn't work)
		JavascriptExecutor jse = (JavascriptExecutor) BrowserSession.getInstance().getWebDriver();

		jse.executeScript("window.scrollTo(0,Math.max(document.documentElement.scrollHeight,document.body.scrollHeight,document.documentElement.clientHeight));");
	}

	/**
	 * Simulation of mouse scroll, up to the top of the page
	 */
	public static void scrollToTop() {

		JavascriptExecutor jse = (JavascriptExecutor) BrowserSession.getInstance().getWebDriver();

		// using "-50" because of the Main Menu, which is an overlay
		jse.executeScript("window.scrollTo(0,Math.min(document.documentElement.scrollHeight,document.body.scrollHeight,document.documentElement.clientHeight) - 50);");
	}

	/**
	 * Simulation of mouse scroll, to the rightmost position
	 */
	public static void scrollToRightEdge() {

		JavascriptExecutor js = (JavascriptExecutor) BrowserSession.getInstance().getWebDriver();

		js.executeScript("window.scrollTo(Math.max(document.documentElement.scrollWidth,document.body.scrollHeight,document.documentElement.clientWidth),-1);");
	}

	/**
	 * Hovers the given element using the mouse cursor
	 * 
	 * @param element
	 *        the {@link WebElement}
	 */
	public static void mouseOver(WebElement element) {

		WebDriver driver = BrowserSession.getInstance().getWebDriver();

		Actions builder = new Actions(driver);

		builder.moveToElement(element).build().perform();
	}

	/**
	 * Hovers the element determined by the given locator, using the mouse cursor
	 * 
	 * @param locator
	 *        the {@link By} locator
	 */
	public static void mouseOver(By locator) {

		WebDriver driver = BrowserSession.getInstance().getWebDriver();

		mouseOver(driver.findElement(locator));
	}

	/**
	 * Blurs the element determined by the given {@link By} locator, using {@link JavascriptExecutor}
	 * 
	 * @param locator
	 *        the {@link By} locator of the element
	 */
	public static void blur(By locator) {

		WebDriver driver = BrowserSession.getInstance().getWebDriver();

		JavascriptExecutor js = (JavascriptExecutor) driver;

		try {

			js.executeScript("return arguments[0].blur();", driver.findElement(locator));

		} catch (StaleElementReferenceException e) {
			blur(locator);
		}
	}

	/**
	 * Blurs the given element using {@link JavascriptExecutor}
	 * 
	 * @param element
	 *        the {@link WebElement} to blur
	 */
	public static void blur(WebElement element) {

		JavascriptExecutor js = (JavascriptExecutor) BrowserSession.getInstance().getWebDriver();

		js.executeScript("return arguments[0].blur();", element); // arguments[0] != undefined
	}

	/**
	 * Focus the given element using {@link Actions}
	 * 
	 * @param locator
	 *        the {@link By} locator of the element
	 */
	public static void focus(By locator) {

		WebDriver driver = BrowserSession.getInstance().getWebDriver();

		focus(driver.findElement(locator));
	}

	/**
	 * Focuses the given element using {@link Actions}
	 * 
	 * @param element
	 *        the element to focus on
	 */
	public static void focus(WebElement element) {

		WebDriver driver = BrowserSession.getInstance().getWebDriver();

		Actions action = new Actions(driver);

		action.moveToElement(element).build().perform();
	}

	/**
	 * Focuses the given element using {@link JavascriptExecutor}
	 * 
	 * @param locator
	 *        the {@link By} locator of the element
	 */
	public static void focusJS(By locator) {

		WebDriver driver = BrowserSession.getInstance().getWebDriver();

		focusJS(driver.findElement(locator));
	}

	/**
	 * Focuses the given element using {@link JavascriptExecutor}
	 * 
	 * @param element
	 *        the {@link WebElement} to focus
	 */
	public static void focusJS(WebElement element) {

		JavascriptExecutor js = (JavascriptExecutor) BrowserSession.getInstance().getWebDriver();

		js.executeScript("return arguments[0] != undefined && arguments[0].focus();", element);
	}

	/**
	 * Hides the DOM element having the given locator from the UI
	 *
	 * @param locator
	 *        the {@link By} locator of the DOM element
	 */
	public static void hideElement(By locator) {

		WebDriver driver = BrowserSession.getInstance().getWebDriver();

		hideElement(driver.findElement(locator));
	}

	/**
	 * Hides the DOM element from the UI
	 *
	 * @param element
	 *        the {@link WebElement} to hide
	 */
	public static void hideElement(WebElement element) {

		WebDriver driver = BrowserSession.getInstance().getWebDriver();

		JavascriptExecutor js = (JavascriptExecutor) driver;

		js.executeScript("arguments[0].setAttribute('style', 'display: none;')", element);
	}

	/**
	 * Removes the DOM element having the given locator from the UI
	 *
	 * @param locator
	 *        the {@link By} to remove
	 */
	public static void removeElement(By locator) {

		WebDriver driver = BrowserSession.getInstance().getWebDriver();

		removeElement(driver.findElement(locator));
	}

	/**
	 * Removes the DOM element from the UI
	 *
	 * @param element
	 *        the {@link WebElement} to remove
	 */
	public static void removeElement(WebElement element) {

		WebDriver driver = BrowserSession.getInstance().getWebDriver();

		JavascriptExecutor js = (JavascriptExecutor) driver;

		js.executeScript("arguments[0].remove()", element);
	}

	/**
	 * Get the view port height attribute on the current HTML page
	 * 
	 * @return
	 */
	private static int getViewPortHeight() {

		WebDriver driver = BrowserSession.getInstance().getWebDriver();

		JavascriptExecutor js = (JavascriptExecutor) driver;

		Object heightAttribute = js.executeScript("return Math.max(document.documentElement.clientHeight, window.innerHeight || 0);");

		int viewPortHeight = Integer.valueOf(heightAttribute.toString());

		return viewPortHeight;
	}

}