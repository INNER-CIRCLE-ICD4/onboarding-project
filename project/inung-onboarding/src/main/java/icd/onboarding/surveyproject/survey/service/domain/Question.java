package icd.onboarding.surveyproject.survey.service.domain;

import icd.onboarding.surveyproject.survey.service.enums.InputType;
import icd.onboarding.surveyproject.survey.service.exception.*;
import lombok.Getter;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Getter
public class Question {
	private Survey survey;
	private final UUID id;
	private final String name;
	private final String description;
	private final InputType inputType;
	private final boolean required;
	private final Integer sortOrder;

	private final List<Option> options;

	private static final int MAX_OPTION_COUNT = 10;

	private Question (String name, String description, String inputType, boolean required, Integer sortOrder, List<Option> options) {
		this.name = name;
		this.description = description;
		this.inputType = InputType.valueOf(inputType);
		this.required = required;
		this.sortOrder = sortOrder;
		this.id = UUID.randomUUID();

		for (Option option : options) {
			option.assignQuestion(this);
		}
		this.options = List.copyOf(options);
	}

	public static Question create (String name, String description, String inputType, boolean required, Integer sortOrder, List<Option> options) {
		if (!InputType.contains(inputType))
			throw new InvalidInputTypeException();

		Question question = new Question(name, description, inputType, required, sortOrder, options);
		question.validateOptions();

		return question;
	}

	protected void assignSurvey (Survey survey) {
		this.survey = survey;
	}

	public void validateOptions () {
		if (!inputType.isSelectType()) return;

		if (options.isEmpty()) {
			throw new InSufficientOptionException();
		}
	}

	public void validateRequiredAnswer (List<Answer> answers) {
		if (required) {
			boolean hasAnswer = answers.stream().anyMatch(answer -> answer.getQuestionId().equals(this.id));

			if (!hasAnswer)
				throw new RequiredQuestionNotAnsweredException();
		}
	}
}
