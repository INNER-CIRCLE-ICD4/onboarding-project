package icd.onboarding.surveyproject.survey.controller.dto;

import icd.onboarding.surveyproject.survey.service.domain.Survey;

import java.util.UUID;

public record UpdateSurveyResponse(
		UUID id,
		int version
) {
	public static UpdateSurveyResponse fromDomain (Survey survey) {
		return new UpdateSurveyResponse(survey.getId(), survey.getVersion());
	}
}
