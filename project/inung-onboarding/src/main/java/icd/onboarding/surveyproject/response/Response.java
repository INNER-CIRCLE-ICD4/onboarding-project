package icd.onboarding.surveyproject.response;

import java.util.List;
import java.util.UUID;

public class Response {
	private final UUID responseId;
	private final UUID respondentId;
	private final List<?> answers;

	private UUID surveyId;
	private Integer surveyVersion;

	private Response (List<?> answers) {
		this.responseId = UUID.randomUUID();
		this.respondentId = UUID.randomUUID();
		this.answers = answers;
	}
}
