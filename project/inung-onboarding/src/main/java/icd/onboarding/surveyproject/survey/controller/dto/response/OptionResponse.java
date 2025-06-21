package icd.onboarding.surveyproject.survey.controller.dto.response;

import icd.onboarding.surveyproject.survey.service.domain.Option;

import java.util.UUID;

public record OptionResponse(
		UUID id,
		String text,
		int sortOrder
) {
	public static OptionResponse fromDomain(Option option) {
		return new OptionResponse(
				option.getId(),
				option.getText(),
				option.getSortOrder()
		);
	}
}
