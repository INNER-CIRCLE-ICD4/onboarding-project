package icd.onboarding.surveyproject.service.domain;

import icd.onboarding.surveyproject.service.enums.InputType;
import icd.onboarding.surveyproject.service.exception.*;
import io.micrometer.common.util.StringUtils;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
public class Question {
	private Survey survey;
	private final UUID id;
	private final String name;
	private final String description;
	private final InputType inputType;
	private final Boolean required;
	private final Integer sortOrder;

	private final List<Option> options;

	private static final int MAX_OPTION_COUNT = 10;

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
		if (StringUtils.isBlank(name) || StringUtils.isBlank(description))
			throw new InvalidQuestionInfoException();
		if (sortOrder < 0)
			throw new NotNegativeNumberException();
		if (!InputType.contains(inputType))
			throw new InvalidInputTypeException();
		if (options == null || options.isEmpty())
			throw new InSufficientOptionException();
		if (options.size() > MAX_OPTION_COUNT)
			throw new MaxOptionCountExceededException();

		return new Question(name, description, inputType, required, sortOrder, options);
	}

	protected void setSurvey (Survey survey) {
		this.survey = survey;
	}
}
