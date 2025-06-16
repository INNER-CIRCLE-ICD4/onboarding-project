package icd.onboarding.surveyproject.survey.service.enums;

public enum InputType {
	SHORT_TEXT,
	LONG_TEXT,
	SINGLE_SELECT,
	MULTI_SELECT;

	public boolean isSelectType () {
		return this == SINGLE_SELECT || this == MULTI_SELECT;
	}
}
