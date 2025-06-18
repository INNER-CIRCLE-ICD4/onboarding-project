package icd.onboarding.surveyproject.survey.controller.dto;

import icd.onboarding.surveyproject.survey.service.domain.Survey;

import java.util.UUID;

public record CreateSurveyResponse(
		UUID id
) {
	public static CreateSurveyResponse fromDomain (Survey survey) {
		return new CreateSurveyResponse(survey.getId());
	}
}
