package icd.onboarding.surveyproject.service.domain;

import icd.onboarding.surveyproject.service.enums.InputType;
import icd.onboarding.surveyproject.service.enums.QuestionErrors;
import icd.onboarding.surveyproject.service.exception.InvalidQuestionException;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
public class Question {
	private @NotNull UUID surveyId;
	private @NotNull Integer surveyVersion;
	private @Nullable UUID id;
	private @NotNull String name;
	private @NotNull String description;
	private @NotNull InputType inputType;
	private @Nullable Boolean required;
	private @NotNull Integer sortOrder;

	private @Nullable List<Option> options;

	public static Question create (UUID surveyId, Integer surveyVersion, String name, String description, String inputType, Boolean required, Integer sortOrder) {
		if (surveyId == null || surveyVersion == null)
			throw new InvalidQuestionException(QuestionErrors.INVALID_SURVEY_INFO);
		if (name == null || name.isBlank())
			throw new InvalidQuestionException(QuestionErrors.EMPTY_QUESTION_INFO);
		if (description == null || description.isBlank())
			throw new InvalidQuestionException(QuestionErrors.EMPTY_QUESTION_INFO);
		if (sortOrder < 0)
			throw new InvalidQuestionException(QuestionErrors.NOT_NEGATIVE_NUMBER);
		if (!InputType.contains(inputType)) {
			throw new InvalidQuestionException(QuestionErrors.INVALID_INPUT_TYPE);
		}

		return Question.builder()
					   .id(UUID.randomUUID())
					   .surveyId(surveyId)
					   .surveyVersion(surveyVersion)
					   .name(name)
					   .description(description)
					   .inputType(InputType.valueOf(inputType))
					   .required(required)
					   .sortOrder(sortOrder)
					   .build();
	}
}
