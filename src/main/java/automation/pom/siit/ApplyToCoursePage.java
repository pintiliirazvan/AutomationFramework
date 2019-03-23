package automation.pom.siit;

import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOf;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;

import automation.core.WaitElement;
import automation.pom.siit.constants.EnglishLevel;
import automation.pom.siit.constants.ITKnowledge;
import automation.pom.siit.constants.Occupation;
import automation.util.Event;

/**
 * Page object class that describes the "Apply here" page used for joining a selected Course
 * 
 * @author alexgabor
 *
 */
public class ApplyToCoursePage extends AbstractBaseWebsitePage {

	@FindBy(name = "candidate_first_name")
	private WebElement txtFirstName;

	@FindBy(name = "candidate_last_name")
	private WebElement txtLastName;

	@FindBy(name = "field_key_date")
	private WebElement cldBirthDate;

	@FindBy(name = "candidate_email")
	private WebElement txtEmail;

	@FindBy(name = "candidate_phone")
	private WebElement txtPhone;

	@FindBy(name = "candidate_online_profile")
	private WebElement txtLinkedInProfile;

	@FindBy(name = "candidate_address")
	private WebElement txtAddress;

	@FindBy(id = "candidate_location")
	private WebElement selectCity;

	@FindBy(id = "candidate_social_standing")
	private WebElement selectOccupation;

	@FindBy(xpath = "//input[@name='candidate_technical_skills' and @value='DA']")
	private WebElement radioITKnowledgeYes;

	@FindBy(xpath = "//input[@name='candidate_technical_skills' and @value='NU']")
	private WebElement radioITKnowledgeNo;

	@FindBy(xpath = "//input[@name='candidate_technical_skills' and @value='Am studiat individual']")
	private WebElement radioITKnowledgeSelf;

	@FindBy(id = "available_courses")
	private WebElement selectCourseName;

	@FindBy(id = "candidate_business_sector")
	private WebElement selectBusinessSector;

	@FindBy(id = "candidate_english_level")
	private WebElement selectEnglishLevel;

	@FindBy(name = "marketing_source")
	private WebElement txtHeardOfSIIT;

	@FindBy(name = "rb_newsletter")
	private WebElement chkNewsletter;

	@FindBy(name = "acknowledgement")
	private WebElement chkTermsAndConditions;

	@FindBy(xpath = "//button[.='Submit']")
	private WebElement btnSubmit;

	public ApplyToCoursePage(WebDriver driver) {
		super(driver);
	}

	/**
	 * Enter the first name of the trainee on the application form
	 * 
	 * @param firstName
	 *        the first name to enter
	 */
	public void enterFirstName(String firstName) {

		new WaitElement().waitUntil(visibilityOf(txtFirstName), 10);

		txtFirstName.sendKeys(firstName);
	}

	/**
	 * Enter the last name of the trainee on the application form
	 * 
	 * @param lastName
	 *        the last name to enter
	 */
	public void enterLastName(String lastName) {

		new WaitElement().waitUntil(visibilityOf(txtLastName), 10);

		txtLastName.sendKeys(lastName);
	}

	/**
	 * Enter the birth date of the trainee on the application form
	 * 
	 * @param birthDate
	 *        the birth date to enter
	 */
	public void enterBirthDate(LocalDate birthDate) {

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

		String date = birthDate.format(formatter);

		cldBirthDate.sendKeys(date);
	}

	/**
	 * Enter the email address of the trainee on the application form
	 * 
	 * @param email
	 *        the email to enter
	 */
	public void enterEmailAddress(String email) {

		txtEmail.sendKeys(email);
	}

	/**
	 * Enter the phone number of the trainee on the application form
	 * 
	 * @param phoneNumber
	 *        the phone number to enter
	 */
	public void enterPhoneNumber(String phoneNumber) {

		txtPhone.sendKeys(phoneNumber);
	}

	/**
	 * Enter the URL to the trainee's LinkedIn profile on the application form
	 * 
	 * @param linkedInProfileURL
	 *        the URL to the trainee's LinkedIn profile
	 */
	public void enterLinkedInProfile(String linkedInProfileURL) {

		txtLinkedInProfile.sendKeys(linkedInProfileURL);
	}

	/**
	 * Enter the address of the trainee on the application form
	 * 
	 * @param address
	 *        the address to enter
	 */
	public void enterAddress(String address) {

		txtAddress.sendKeys(address);
	}

	/**
	 * Select the city of the trainee on the application form
	 * 
	 * @param city
	 *        the city to enter
	 */
	public void selectCity(String city) {

		Select dropdown = new Select(selectCity);

		dropdown.selectByValue(city);
	}

	/**
	 * Select the current {@link Occupation} of the trainee on the application form
	 * 
	 * @param occupation
	 *        the occupation to enter
	 */
	public void selectOccupation(Occupation occupation) {

		Select dropdown = new Select(selectOccupation);

		dropdown.selectByValue(occupation.getLabel());
	}

	/**
	 * Enter the IT knowledge level of the trainee on the application form
	 * 
	 * @param itKnowledge
	 *        the IT knowledge level
	 */
	public void checkITKnowledge(ITKnowledge itKnowledge) {

		WaitElement w = new WaitElement();

		switch (itKnowledge) {
		case YES:

			w.waitUntil(elementToBeClickable(radioITKnowledgeYes), 15);

			radioITKnowledgeYes.click();

			break;

		case NO:

			w.waitUntil(elementToBeClickable(radioITKnowledgeNo), 15);

			radioITKnowledgeNo.click();

			break;

		case SELF_TAUGHT:

			w.waitUntil(elementToBeClickable(radioITKnowledgeSelf), 15);

			radioITKnowledgeSelf.click();

			break;

		default:
			throw new IllegalArgumentException("Unhandled enum constant for IT knowledge: " + itKnowledge);
		}
	}

	/**
	 * Select the course name for which the trainee is applying
	 * 
	 * @param courseName
	 *        the course name to enter
	 */
	public void selectCourseName(String courseName) {

		Select dropdown = new Select(selectCourseName);

		dropdown.selectByVisibleText(courseName);
	}

	/**
	 * Select the current business sector of the trainee on the application form
	 * 
	 * @param businessSector
	 *        the business sector to enter
	 */
	public void selectBusinessSector(String businessSector) {

		Select dropdown = new Select(selectBusinessSector);

		dropdown.selectByValue(businessSector);
	}

	/**
	 * Select the {@link EnglishLevel} of the trainee on the application form
	 * 
	 * @param englishLevel
	 *        the English level to enter
	 */
	public void selectEnglishLevel(EnglishLevel englishLevel) {

		Select dropdown = new Select(selectEnglishLevel);

		dropdown.selectByValue(englishLevel.getLabel());
	}

	/**
	 * Enter the source from which the trainee heard about SIIT
	 * 
	 * @param heardFromSource
	 *        the source to enter
	 */
	public void enterHeardOfSIITFrom(String heardFromSource) {

		txtHeardOfSIIT.sendKeys(heardFromSource);
	}

	/**
	 * Check or uncheck the trainee's newsletter subscription
	 * 
	 * @param isSubscribedToNewsletter
	 *        the value of newsletter subscription
	 */
	public void checkNewsletterSubscription(boolean isSubscribedToNewsletter) {

		new WaitElement().waitUntil(elementToBeClickable(chkNewsletter), 15);

		Event.scrollTo(chkNewsletter);

		if (isSubscribedToNewsletter) {

			if (!chkNewsletter.isSelected()) {
				chkNewsletter.click(); // check checkbox
			}

		} else {

			if (chkNewsletter.isSelected()) {
				chkNewsletter.click(); // uncheck checkbox
			}
		}
	}

	/**
	 * Check or uncheck the trainee's terms & conditions acknowledgement
	 * 
	 * @param isAcknowledgeTerms
	 *        the value of terms & conditions acknowledgement
	 */
	public void checkTermsAcknowledgement(boolean isAcknowledgeTerms) {

		new WaitElement().waitUntil(elementToBeClickable(chkTermsAndConditions), 15);

		Event.scrollTo(chkTermsAndConditions);

		if (isAcknowledgeTerms) {

			if (!chkTermsAndConditions.isSelected()) {
				chkTermsAndConditions.click(); // check checkbox
			}

		} else {

			if (chkTermsAndConditions.isSelected()) {
				chkTermsAndConditions.click(); // uncheck checkbox
			}
		}
	}
	
	/**
	 * Submit the application form
	 */
	public void clickSubmitForm() {
		btnSubmit.click();
	}

}
