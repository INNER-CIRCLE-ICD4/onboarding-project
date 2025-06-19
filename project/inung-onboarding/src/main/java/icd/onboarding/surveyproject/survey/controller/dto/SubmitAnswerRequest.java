package icd.onboarding.surveyproject.survey.controller.dto;

import icd.onboarding.surveyproject.survey.service.domain.Answer;

import java.util.UUID;

public record SubmitAnswerRequest(
		UUID questionId,
		String textValue
) {
	public Answer toDomain() {
		return Answer.create(textValue, questionId);
	}
}
