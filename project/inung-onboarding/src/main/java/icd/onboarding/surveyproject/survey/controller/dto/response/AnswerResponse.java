package icd.onboarding.surveyproject.survey.controller.dto.response;

import icd.onboarding.surveyproject.survey.service.domain.Answer;

import java.util.UUID;

public record AnswerResponse (
		String text,
		UUID questionId
) {
	public static AnswerResponse fromDomain(Answer answer) {
		return new AnswerResponse(answer.getText(), answer.getQuestionId());
	}
}
