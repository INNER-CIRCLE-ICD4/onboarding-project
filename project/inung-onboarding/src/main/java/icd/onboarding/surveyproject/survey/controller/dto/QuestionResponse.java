package icd.onboarding.surveyproject.survey.controller.dto;

import icd.onboarding.surveyproject.survey.service.domain.Question;

import java.util.List;
import java.util.UUID;

public record QuestionResponse(
		UUID id,
		String name,
		String description,
		String inputType,
		boolean required,
		int sortOrder,
		List<OptionResponse> options
) {
	public static QuestionResponse fromDomain (Question question) {
		return new QuestionResponse(
				question.getId(),
				question.getName(),
				question.getDescription(),
				question.getInputType().name(),
				question.isRequired(),
				question.getSortOrder(),
				question.getOptions().stream().map(OptionResponse::fromDomain).toList()
		);
	}
}
