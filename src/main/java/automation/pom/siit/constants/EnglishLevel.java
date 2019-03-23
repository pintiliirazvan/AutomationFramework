package automation.pom.siit.constants;

/**
 * Enum holding the available selection values for trainee's English level, on the course application form
 * 
 * @author alexgabor
 *
 */
public enum EnglishLevel {
	
	BEGINNER("Începător"),
	AVERAGE("Mediu"),
	ADVANCED("Avansat");
	
	private String label;
	
	EnglishLevel(String label) {
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
