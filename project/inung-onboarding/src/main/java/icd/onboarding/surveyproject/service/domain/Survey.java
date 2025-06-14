package icd.onboarding.surveyproject.service.domain;

import icd.onboarding.surveyproject.service.exception.InSufficientQuestionException;
import icd.onboarding.surveyproject.service.exception.InValidSurveyInfoException;
import lombok.Getter;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Getter
public class Survey {
	private final UUID id;
	private final String title;
	private final String description;

	private List<Question> questions;

	private Survey (String title, String description, List<Question> questions) {
		this.title = title;
		this.description = description;
		this.id = UUID.randomUUID();

		for (Question question : questions) {
			question.setSurvey(this);
		}
		this.questions = Collections.unmodifiableList(questions);
	}

	public static Survey create (String title, String description, List<Question> questions) {
		if (title == null || title.isBlank())
			throw new InValidSurveyInfoException();
		if (questions == null || questions.isEmpty())
			throw new InSufficientQuestionException();

		return new Survey(title, description, questions);
	}
}
