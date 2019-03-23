package automation.core.properties;

import automation.core.browser.BrowserName;

/**
 * Interface describing the generic test properties
 * 
 * @author alexgabor
 *
 */
public interface TestProperties {

	/**
	 * Get the URL that the tests will navigate to, in the browser page
	 *
	 * @return
	 */
	String getURL();

	/**
	 * Set the URL that the tests will navigate to
	 * 
	 * @param url
	 *        the url to set
	 * @throws Exception
	 */
	void setURL(String url) throws Exception;

	/**
	 * Get the {@link BrowserName} used to run the tests
	 *
	 * @return
	 */
	BrowserName getBrowserName();

	/**
	 * Set the {@link BrowserName} used to run the tests
	 *
	 * @param browserName
	 *        the {@link BrowserName} used to run the tests
	 * @throws Exception
	 */
	void setBrowserName(BrowserName browserName) throws Exception;
}
