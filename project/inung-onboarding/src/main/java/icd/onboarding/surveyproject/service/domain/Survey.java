package icd.onboarding.surveyproject.service.domain;

import icd.onboarding.surveyproject.service.exception.InSufficientQuestionException;
import icd.onboarding.surveyproject.service.exception.InValidSurveyInfoException;
import icd.onboarding.surveyproject.service.exception.MaxQuestionCountExceededException;
import io.micrometer.common.util.StringUtils;
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

	private static final int MAX_QUESTION_COUNT = 10;

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
		if (StringUtils.isBlank(title))
			throw new InValidSurveyInfoException();
		if (questions == null || questions.isEmpty())
			throw new InSufficientQuestionException();
		if (questions.size() > MAX_QUESTION_COUNT)
			throw new MaxQuestionCountExceededException();

		return new Survey(title, description, questions);
	}
}
