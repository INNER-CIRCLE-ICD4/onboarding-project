package icd.onboarding.surveyproject.survey.controller.dto;

import icd.onboarding.surveyproject.survey.service.domain.Survey;

import java.util.List;
import java.util.UUID;

public record SurveyResponse(
		UUID id,
		int version,
		String title,
		String description,
		List<QuestionResponse> questions
) {
	public static SurveyResponse formDomain (Survey survey) {
		return new SurveyResponse(
				survey.getId(),
				survey.getVersion(),
				survey.getTitle(),
				survey.getDescription(),
				survey.getQuestions().stream().map(QuestionResponse::fromDomain).toList()
		);
	}
}
