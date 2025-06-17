package icd.onboarding.surveyproject.survey.controller.consts;

public enum ErrorCodes {
	SURVEY_NOT_FOUND("설문 조사를 찾을 수 없습니다.", "SURVEY_NOT_FOUND");

	public final String message;
	public final String code;

	ErrorCodes (String message, String code) {
		this.message = message;
		this.code = code;
	}
}
