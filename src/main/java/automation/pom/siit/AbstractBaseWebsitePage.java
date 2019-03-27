package automation.pom.siit;

import static org.junit.Assert.assertEquals;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfAllElementsLocatedBy;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import automation.core.WaitElement;
import automation.pom.AbstractPageObject;
import automation.pom.siit.menu.MainMenu;

/**
 * Base Page Object class that contains {@link WebElement}<code>s</code> which are found on all other pages
 * 
 * @author alexgabor
 *
 */
public class AbstractBaseWebsitePage extends AbstractPageObject {
	
	private MainMenu menu = new MainMenu(driver);
	
	@FindBy(id = "s")
	private WebElement searchForm;

	@FindBy(xpath = "//form[@id='searchform']/input[@type='submit']")
	private WebElement btnSearch;

	public AbstractBaseWebsitePage(WebDriver driver) {
		super(driver);
	}
	
	/**
	 * Get the Main Menu instance
	 * 
	 * @return
	 */
	public MainMenu getMenu() {
		return menu;
	}

	/**
	 * Search the entire website using the search box
	 * 
	 * @param keywords
	 *        the {@link String} to search for
	 * @return
	 */
	public AbstractBaseWebsitePage search(String keywords) {

		searchForm.sendKeys(keywords);

		btnSearch.click();

		By resultsLocator = By.xpath("//section[@id='primary']");

		new WaitElement().waitUntil(visibilityOfAllElementsLocatedBy(resultsLocator), 10);

		return this;
	}

	/**
	 * Select the given result among the results list and verify that the course title is correct
	 * 
	 * @param searchResultLinkText
	 *        the link text displayed in the search result
	 */
	public void selectSearchResult(String searchResultLinkText) {

		By coursesResult = By.linkText(searchResultLinkText);

		driver.findElement(coursesResult).click();

		String expectedTitle = searchResultLinkText.toUpperCase();
		String actualTitle = driver.findElement(By.tagName("h1")).getText();

		assertEquals("The course title was incorrect", expectedTitle, actualTitle);
	}

}
