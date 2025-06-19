package icd.onboarding.surveyproject.survey.controller.dto;


import icd.onboarding.surveyproject.survey.service.domain.Answer;
import icd.onboarding.surveyproject.survey.service.domain.Response;

import java.util.List;
import java.util.UUID;

public record SubmitResponseRequest(
		String respondentId,
		List<SubmitAnswerRequest> answers
) {
	public Response toDomain (UUID surveyId, int surveyVersion) {
		List<Answer> domainAnswers = answers.stream()
											.map(SubmitAnswerRequest::toDomain)
											.toList();
		return Response.create(
				surveyId,
				surveyVersion,
				respondentId,
				domainAnswers
		);
	}
}
