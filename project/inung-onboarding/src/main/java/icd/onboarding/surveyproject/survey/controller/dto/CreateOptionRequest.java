package icd.onboarding.surveyproject.survey.controller.dto;

import icd.onboarding.surveyproject.survey.service.domain.Option;

public record CreateOptionRequest(
		String text,
		int sortOrder
) {
	public Option toDomain () {
		return Option.create(text, sortOrder);
	}
}
