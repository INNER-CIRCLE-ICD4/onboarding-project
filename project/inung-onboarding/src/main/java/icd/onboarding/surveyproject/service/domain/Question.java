package icd.onboarding.surveyproject.service.domain;

import icd.onboarding.surveyproject.service.enums.InputType;
import icd.onboarding.surveyproject.service.exception.InSufficientOptionException;
import icd.onboarding.surveyproject.service.exception.InvalidInputTypeException;
import icd.onboarding.surveyproject.service.exception.InvalidQuestionInfoException;
import icd.onboarding.surveyproject.service.exception.NotNegativeNumberException;
import lombok.Builder;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

@Getter
public class Question {
	private Survey survey;
	private @Nullable UUID id;
	private @NotNull String name;
	private @NotNull String description;
	private @NotNull InputType inputType;
	private @Nullable Boolean required;
	private @NotNull Integer sortOrder;

	private @Nullable List<Option> options;

	private Question (String name, String description, String inputType, Boolean required, Integer sortOrder, List<Option> options) {
		this.name = name;
		this.description = description;
		this.inputType = InputType.valueOf(inputType);
		this.required = required != null ? required : false;
		this.sortOrder = sortOrder;
		this.id = UUID.randomUUID();

		for (Option option : options) {
			option.setQuestion(this);
		}
		this.options = options;
	}

	public static Question create (String name, String description, String inputType, Boolean required, Integer sortOrder, List<Option> options) {
		if (name == null || name.isBlank())
			throw new InvalidQuestionInfoException();
		if (description == null || description.isBlank())
			throw new InvalidQuestionInfoException();
		if (sortOrder < 0)
			throw new NotNegativeNumberException();
		if (!InputType.contains(inputType))
			throw new InvalidInputTypeException();
		if (options == null || options.isEmpty())
			throw new InSufficientOptionException();

		return new Question(name, description, inputType, required, sortOrder, options);
	}

	protected void setSurvey (Survey survey) {
		this.survey = survey;
	}
}
