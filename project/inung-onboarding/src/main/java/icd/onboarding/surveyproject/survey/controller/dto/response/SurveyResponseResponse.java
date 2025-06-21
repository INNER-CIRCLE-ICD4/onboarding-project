package icd.onboarding.surveyproject.survey.controller.dto.response;

import icd.onboarding.surveyproject.survey.service.domain.Response;

import java.util.List;
import java.util.UUID;

public record SurveyResponseResponse(
		UUID responseId,
		String respondentId,
		List<AnswerResponse> answers
) {
	public static SurveyResponseResponse fromDomain (Response response) {
		return new SurveyResponseResponse(
				response.getResponseId(),
				response.getRespondentId(),
				response.getAnswers().stream().map(AnswerResponse::fromDomain).toList()
		);
	}
}
