package icd.onboarding.surveyproject.survey.controller.consts;

public enum ErrorCodes {

	/* 404 Not Found */
	SURVEY_NOT_FOUND("설문 조사를 찾을 수 없습니다."),

	/* 400 Bad Request */
	UNSUPPORTED_OPTION("옵션을 지원하지 않는 질문 유형입니다."),
	INSUFFICIENT_OPTION("선택형 질문에는 옵션이 필수로 입력되어야 합니다."),
	REQUIRED_ANSWERS("필수 질문에는 응답이 존재해야 합니다."),
	TOO_MANY_ANSWER("단일 선택형 질문에는 복수 답변이 불가능 합니다."),
	DUPLICATE_RESPONSE("중복 제출은 불가능 합니다.");

	public final String message;

	ErrorCodes (String message) {
		this.message = message;
	}
}
