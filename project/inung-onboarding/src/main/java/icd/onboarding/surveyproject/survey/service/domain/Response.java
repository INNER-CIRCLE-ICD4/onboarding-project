package icd.onboarding.surveyproject.survey.service.domain;

import java.util.List;
import java.util.UUID;

public class Response {
	private final UUID responseId;
	private final UUID respondentId;
	private final UUID surveyId;
	private final Integer surveyVersion;
	private final List<Answer> answers;

	private Response (UUID surveyId, Integer surveyVersion, List<Answer> answers) {
		this.responseId = UUID.randomUUID();
		this.respondentId = UUID.randomUUID();
		this.surveyId = surveyId;
		this.surveyVersion = surveyVersion;

		for (Answer answer : answers) {
			answer.assignResponse(this);
		}

		this.answers = List.copyOf(answers);
	}

	public static Response create (UUID surveyId, Integer surveyVersion, List<Answer> answers) {
		return new Response(surveyId, surveyVersion, answers);
	}
}
