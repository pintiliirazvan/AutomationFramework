package automation.pom.siit.constants;

/**
 * Enum holding the available selection values for trainee's occupation, on the course application form
 * 
 * @author alexgabor
 *
 */
public enum Occupation {
	
	EMPLOYED("Angajat"),
	STUDENT("Student"),
	FREELANCER("Liber profesionist"),
	UNEMPLOYED("Fără ocupație");
	
	private String label;
	
	Occupation(String label) {
		this.label = label;
	}

	/**
	 * Get the label/value of this enum constant, as displayed on the interface
	 * 
	 * @return
	 */
	public String getLabel() {
		return label;
	}

}
