package icd.onboarding.surveyproject.service.exception;

import icd.onboarding.surveyproject.service.enums.QuestionErrors;

public class InvalidQuestionException extends IllegalArgumentException{
	public InvalidQuestionException(QuestionErrors questionError) {
		super(questionError.value());
	}
}
