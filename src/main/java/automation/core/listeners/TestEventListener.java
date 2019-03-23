package automation.core.listeners;

import static automation.util.StringUtil.isNullOrEmpty;

import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.WebDriverEventListener;

import automation.core.logging.MessageLogger;

/**
 * Listener for various {@link WebDriver} events during the test run
 *
 * @author alexgabor
 *
 */
public class TestEventListener implements WebDriverEventListener {

	private static final MessageLogger LOG = new MessageLogger(TestEventListener.class);

	@Override
	public void onException(Throwable e, WebDriver driver) {

		/**
		 * Already handled by OutcomeTestWather class
		 */
	}

	@Override
	public void beforeClickOn(WebElement element, WebDriver driver) {

		String elementIdentifier = getMeaningfulIdentifier(element);

		if ("checkbox".equals(element.getAttribute("type"))) {
			LOG.info("### Click on checkbox '" + elementIdentifier + "'");
		} else {
			LOG.info("### Click on element '" + elementIdentifier + "'");
		}
	}

	@Override
	public void afterClickOn(WebElement element, WebDriver driver) {

	}

	@Override
	public void beforeChangeValueOf(WebElement element, WebDriver driver, CharSequence[] keysToSend) {
	
		String elementIdentifier = getMeaningfulIdentifier(element);
	
		LOG.info("### Changing value of field '" + elementIdentifier + "'");
	}

	@Override
	public void afterChangeValueOf(WebElement element, WebDriver driver, CharSequence[] keysToSend) {
	
	}

	@Override
	public void beforeGetText(WebElement element, WebDriver driver) {
	
	}

	@Override
	public void afterGetText(WebElement element, WebDriver driver, String text) {
	
	}

	@Override
	public void beforeFindBy(By locator, WebElement element, WebDriver driver) {

	}

	@Override
	public void afterFindBy(By locator, WebElement element, WebDriver driver) {

	}

	@Override
	public void beforeNavigateBack(WebDriver driver) {

	}

	@Override
	public void afterNavigateBack(WebDriver driver) {

	}

	@Override
	public void beforeNavigateForward(WebDriver driver) {

	}

	@Override
	public void afterNavigateForward(WebDriver driver) {

	}

	@Override
	public void beforeNavigateTo(String url, WebDriver driver) {

	}

	@Override
	public void afterNavigateTo(String url, WebDriver driver) {

	}

	@Override
	public void beforeNavigateRefresh(WebDriver driver) {

	}

	@Override
	public void afterNavigateRefresh(WebDriver driver) {

	}

	@Override
	public void beforeScript(String script, WebDriver driver) {

	}

	@Override
	public void afterScript(String script, WebDriver driver) {

	}

	@Override
	public void beforeAlertAccept(WebDriver driver) {

	}

	@Override
	public void afterAlertAccept(WebDriver driver) {

	}

	@Override
	public void afterAlertDismiss(WebDriver driver) {

	}

	@Override
	public void beforeAlertDismiss(WebDriver driver) {

	}

	@Override
	public void beforeSwitchToWindow(String windowName, WebDriver driver) {

	}

	@Override
	public void afterSwitchToWindow(String windowName, WebDriver driver) {

	}

	@Override
	public <X> void beforeGetScreenshotAs(OutputType<X> target) {

	}

	@Override
	public <X> void afterGetScreenshotAs(OutputType<X> target, X screenshot) {

	}

	/**
	 * Returns a meaningful identifier of the given {@link WebElement} for logging purpose
	 *
	 * @param element
	 *        the {@link WebElement} for which to get the identifier
	 * @return
	 */
	private static final String getMeaningfulIdentifier(WebElement element) {

		String meaningfulName = element.getAttribute("id");

		meaningfulName = isNullOrEmpty(meaningfulName) ? element.getText() : meaningfulName;
		meaningfulName = isNullOrEmpty(meaningfulName) ? element.getTagName() : meaningfulName;

		return meaningfulName;
	}

}
