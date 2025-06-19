package icd.onboarding.surveyproject.survey.controller.dto;

import icd.onboarding.surveyproject.survey.service.domain.Option;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record UpdateOptionRequest(
		@NotBlank String text,
		@Min(1) int sortOrder
) {
	public Option toDomain () {
		return Option.create(text, sortOrder);
	}
}
