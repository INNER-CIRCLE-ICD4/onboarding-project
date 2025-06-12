package icd.onboarding.surveyproject.service.domain;

import icd.onboarding.surveyproject.service.enums.InputType;
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

	public static Question create (UUID surveyId, Integer surveyVersion, String name, String description, InputType inputType, Boolean required, Integer sortOrder) {
		if (surveyId == null || surveyVersion == null) throw new IllegalArgumentException();
		if (name == null || name.isBlank()) throw new IllegalArgumentException();
		if (description == null || description.isBlank()) throw new IllegalArgumentException();
		if (inputType == null) throw new IllegalArgumentException();
		if (sortOrder < 0) throw new IllegalArgumentException();

		return Question.builder()
					   .id(UUID.randomUUID())
					   .surveyId(surveyId)
					   .surveyVersion(surveyVersion)
					   .name(name)
					   .description(description)
					   .inputType(inputType)
					   .required(required != null ? required : false)
					   .sortOrder(sortOrder)
					   .build();
	}
}
