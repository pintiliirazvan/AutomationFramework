package automation.runnable;

import static automation.pom.siit.constants.EnglishLevel.ADVANCED;
import static automation.pom.siit.constants.ITKnowledge.SELF_TAUGHT;
import static automation.pom.siit.constants.Occupation.FREELANCER;
import static java.time.Month.AUGUST;

import java.time.LocalDate;

import org.junit.Test;

import automation.core.SeleneseTest;
import automation.pom.siit.AdmissionsPage;
import automation.pom.siit.ApplyToCoursePage;
import automation.pom.siit.HomePage;
import automation.pom.siit.menu.MainMenu;

/**
 * Sample test class
 * 
 * @author alexgabor
 *
 */
public class DemoTest extends SeleneseTest {

	/**
	 * Test steps:<br>
	 * 
	 * <b>1.</b> Open the browser and navigate to website address</br>
	 * <b>2.</b> Navigate to "Admissions" page via the main menu</br>
	 * <b>3.</b> Search for available courses using some keywords</br>
	 * <b>4.</b> Select one of the results (a course)</br>
	 * <b>5.</b> Fill the form and submit the application</br>
	 */
	@Test
	public void testApplyToCourse() {

		HomePage homePage = new HomePage(getWebDriver());

		MainMenu menu = homePage.getMenu();

		AdmissionsPage admissionPage = menu.goToAdmissionsPage();

		admissionPage.search("Automation course");

		admissionPage.selectSearchResult("Test Automation Cluj-Napoca");

		fillCourseApplicationForm();
	}

	/**
	 * Fill-in and submit the course application form
	 */
	private void fillCourseApplicationForm() {

		ApplyToCoursePage applyToCourseForm = new ApplyToCoursePage(getWebDriver());

		applyToCourseForm.enterFirstName("John");

		applyToCourseForm.enterLastName("Doe");

		applyToCourseForm.enterBirthDate(LocalDate.of(1993, AUGUST, 10));

		applyToCourseForm.enterEmailAddress("automation_trainee@gmail.com");

		applyToCourseForm.enterPhoneNumber("0722111111");

		applyToCourseForm.enterLinkedInProfile("https://www.linkedin.com/abc");

		applyToCourseForm.enterAddress("Str. Neasfaltata, nr. -1");

		applyToCourseForm.selectCity("Iași");

		applyToCourseForm.selectOccupation(FREELANCER);

		applyToCourseForm.checkITKnowledge(SELF_TAUGHT);

		applyToCourseForm.selectCourseName("Test Automation - Mai 2019");

		applyToCourseForm.selectBusinessSector("Altele");

		applyToCourseForm.selectEnglishLevel(ADVANCED);

		applyToCourseForm.enterHeardOfSIITFrom("De pe Facebook");

		applyToCourseForm.checkNewsletterSubscription(true);

		applyToCourseForm.checkTermsAcknowledgement(true);

		// applyToCourseForm.clickSubmitForm(); // would be fun
	}

}
