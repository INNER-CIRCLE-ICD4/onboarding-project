package icd.onboarding.surveyproject.survey.fixtures;

import icd.onboarding.surveyproject.survey.controller.dto.request.SubmitAnswerRequest;
import icd.onboarding.surveyproject.survey.controller.dto.request.SubmitResponseRequest;
import icd.onboarding.surveyproject.survey.service.domain.Response;

import java.util.List;
import java.util.UUID;

public class ResponseFixtures {
	static final String respondentId = "respondent-id";

	public static SubmitResponseRequest validSubmitRequest () {
		UUID questionId1 = UUID.randomUUID();
		UUID questionId2 = UUID.randomUUID();

		return new SubmitResponseRequest(
				respondentId,
				List.of(
						new SubmitAnswerRequest(questionId1, "답변 1"),
						new SubmitAnswerRequest(questionId2, "답변 2")
				)
		);
	}

	public static SubmitResponseRequest submitRequestWithoutRequiredAnswer () {
		UUID requiredQuestionId = UUID.randomUUID();

		return new SubmitResponseRequest(
				respondentId,
				List.of(
						new SubmitAnswerRequest(requiredQuestionId, "")
				)
		);
	}

	public static SubmitResponseRequest submitRequestMissingRequiredQuestion () {
		return new SubmitResponseRequest(
				respondentId,
				List.of()
		);
	}

	public static SubmitResponseRequest submitRequestWithMultipleAnswersForSingleSelect () {
		UUID singleSelectQuestionId = UUID.randomUUID();

		return new SubmitResponseRequest(
				respondentId,
				List.of(
						new SubmitAnswerRequest(singleSelectQuestionId, "답 1"),
						new SubmitAnswerRequest(singleSelectQuestionId, "답 2")
				)
		);
	}

	public static Response basicResponse () {
		return Response.create(
				UUID.randomUUID(),
				1,
				respondentId,
				List.of()
		);
	}
}