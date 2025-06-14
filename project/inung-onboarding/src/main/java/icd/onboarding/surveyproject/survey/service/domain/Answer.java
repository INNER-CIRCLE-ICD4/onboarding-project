package icd.onboarding.surveyproject.survey.service.domain;

import lombok.Getter;

import java.util.UUID;

@Getter
public class Answer {
	private final String text;
	private final UUID questionId;

	private Response response;

	private Answer (String text, UUID questionId) {
		this.text = text;
		this.questionId = questionId;
	}

	public static Answer create (String text, UUID questionId) {
		return new Answer(text, questionId);
	}

	protected void assignResponse (Response response) {
		this.response = response;
	}
}
