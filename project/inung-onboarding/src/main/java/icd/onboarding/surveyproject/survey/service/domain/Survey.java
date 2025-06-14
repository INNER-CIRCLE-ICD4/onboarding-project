package icd.onboarding.surveyproject.survey.service.domain;

import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
public class Survey {
	private final UUID id;
	private final String title;
	private final String description;
	private final int version;

	private final List<Question> questions;

	private Survey (String title, String description, List<Question> questions, int version) {
		this.title = title;
		this.description = description;
		this.id = UUID.randomUUID();
		this.version = version;

		for (Question question : questions) {
			question.assignSurvey(this);
		}
		this.questions = List.copyOf(questions);
	}

	public static Survey create (String title, String description, List<Question> questions) {
		return new Survey(title, description, questions, 1);
	}
}
