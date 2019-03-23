package automation.pom;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

/**
 * Base class for all page objects which initializes the {@link WebDriver}
 * 
 * @author alexgabor
 *
 */
public abstract class AbstractPageObject {

	protected WebDriver driver;

	public AbstractPageObject(WebDriver driver) {

		this.driver = driver;

		PageFactory.initElements(driver, this);
    }
}