package icd.onboarding.surveyproject.survey.controller.dto;

import icd.onboarding.surveyproject.survey.service.domain.Response;

import java.util.UUID;

public record SubmitResponseResponse(
		UUID responseId
) {
	public static SubmitResponseResponse fromDomain (Response response) {
		return new SubmitResponseResponse(response.getResponseId());
	}
}
