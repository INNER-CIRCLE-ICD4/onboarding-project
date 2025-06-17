package icd.onboarding.surveyproject.survey.service.domain;

import lombok.Getter;

import java.util.UUID;

@Getter
public class Option {
	private Question question;
	private final UUID id;
	private final String text;
	private final Integer sortOrder;

	public Option (String text, Integer sortOrder) {
		this.id = UUID.randomUUID();
		this.text = text;
		this.sortOrder = sortOrder;
	}

	public static Option create (String text, Integer sortOrder) {
		return new Option(text, sortOrder);
	}

	protected void assignQuestion (Question question) {
		this.question = question;
	}
}
