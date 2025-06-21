package icd.onboarding.surveyproject.survey.service.domain;

import icd.onboarding.surveyproject.survey.service.enums.InputType;
import icd.onboarding.surveyproject.survey.service.exception.*;
import lombok.Getter;

import java.util.ArrayList;
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

	private final List<Option> options = new ArrayList<>();

	private Question (String name, String description, InputType inputType, boolean required, Integer sortOrder, List<Option> inputOptions) {
		this.name = name;
		this.description = description;
		this.inputType = inputType;
		this.required = required;
		this.sortOrder = sortOrder;
		this.id = UUID.randomUUID();

		if (inputOptions != null) {
			for (Option option : inputOptions) {
				options.add(option);
				option.assignQuestion(this);
			}
		}
	}

	public static Question create (String name, String description, InputType inputType, boolean required, Integer sortOrder, List<Option> inputOptions) {
		Question question = new Question(name, description, inputType, required, sortOrder, inputOptions);
		if (inputType.isSelectType() && (inputOptions == null || inputOptions.isEmpty()))
			throw new InSufficientOptionException();
		if (inputType.isTextType() && !inputOptions.isEmpty())
			throw new UnsupportedOptionException();

		return question;
	}

	protected void assignSurvey (Survey survey) {
		this.survey = survey;
	}

	public void validateRequiredAnswer (List<Answer> answers) {
		if (!required) return;

		boolean hasAnswer = answers.stream().anyMatch(answer -> answer.getQuestionId().equals(this.id));

		if (!hasAnswer)
			throw new RequiredQuestionNotAnsweredException();
	}
}
