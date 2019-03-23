package automation.pom.siit.menu;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import automation.pom.AbstractPageObject;
import automation.pom.siit.AdmissionsPage;
import automation.pom.siit.ContactPage;
import automation.pom.siit.HomePage;
import automation.pom.siit.NetworkPage;

/**
 * Page object class that describes the main menu
 * 
 * @author alexgabor
 *
 */
public class MainMenu extends AbstractPageObject {

	@FindBy(xpath = "//ul[@id='menu-header-menu']//a[@href and .='Home']")
	private WebElement home;

	@FindBy(xpath = "//ul[@id='menu-header-menu']//a[@href and .='Admissions']")
	private WebElement admissions;

	@FindBy(xpath = "//ul[@id='menu-header-menu']//a[@href and .='Network']")
	private WebElement network;

	@FindBy(xpath = "//ul[@id='menu-header-menu']//a[@href and .='Contact']")
	private WebElement contact;
	
	public MainMenu(WebDriver driver) {
		super(driver);
	}
	
	/**
	 * Navigate to Home page
	 * 
	 * @return
	 */
	public HomePage goToHomePage() {

		home.click();

		return new HomePage(driver);
	}
	
	/**
	 * Navigate to Admissions page
	 * 
	 * @return
	 */
	public AdmissionsPage goToAdmissionsPage() {

		admissions.click();

		return new AdmissionsPage(driver);
	}
	
	/**
	 * Navigate to Network page
	 * 
	 * @return
	 */
	public NetworkPage goToNetworkPage() {

		network.click();

		return new NetworkPage(driver);
	}
	
	/**
	 * Navigate to Contact page
	 * 
	 * @return
	 */
	public ContactPage goToContactPage() {

		contact.click();

		return new ContactPage(driver);
	}

}
