package icd.onboarding.surveyproject.service.enums;

public enum QuestionErrors {
	INVALID_SURVEY_INFO("설문에 대한 정보가 없습니다."),
	EMPTY_QUESTION_INFO("질문 제목 및 설명은 필수 입력 값 입니다."),
	NOT_NEGATIVE_NUMBER("정렬 순서는 음수가 될 수 없습니다."),
	INVALID_INPUT_TYPE("입력 형태가 올바르지 않습니다.");

	final String message;

	QuestionErrors (String message) {
		this.message = message;
	}

	public String value () {
		return message;
	}
}
