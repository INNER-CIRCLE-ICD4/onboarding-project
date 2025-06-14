package icd.onboarding.surveyproject.survey.service.enums;

public enum InputType {
	SHORT_TEXT,
	LONG_TEXT,
	SINGLE_SELECT,
	MULTI_SELECT;

	public static boolean contains (String name) {
		if (name == null) return false;

		for (InputType type : values()) {
			if (type.name().equalsIgnoreCase(name)) {
				return true;
			}
		}
		return false;
	}
}
