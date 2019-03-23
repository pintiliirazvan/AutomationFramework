package automation.core.browser;

/**
 * Enum holding the browsers supported by the automated tests
 * 
 * @author alexgabor
 *
 */
public enum BrowserName {

	FIREFOX("Firefox"), 
	CHROME("Chrome"), 
	SAFARI("Safari");

	private final String label;

	BrowserName(String label) {
		this.label = label;
	}

	@Override
	public String toString() {
		return label;
	}

}