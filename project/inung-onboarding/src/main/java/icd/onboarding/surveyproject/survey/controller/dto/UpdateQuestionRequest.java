package icd.onboarding.surveyproject.survey.controller.dto;

import icd.onboarding.surveyproject.survey.service.domain.Option;
import icd.onboarding.surveyproject.survey.service.domain.Question;
import icd.onboarding.surveyproject.survey.service.enums.InputType;
import icd.onboarding.surveyproject.survey.service.validator.ValidInputType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record UpdateQuestionRequest(
		@NotBlank String name,
		String description,
		@ValidInputType String inputType,
		@Min(1) int sortOrder,
		boolean required,
		List<UpdateOptionRequest> options
) {
	public Question toDomain () {
		List<Option> domainOptions = options != null ?
				options.stream()
					   .map(UpdateOptionRequest::toDomain)
					   .toList() : List.of();

		return Question.create(
				name,
				description,
				InputType.valueOf(inputType),
				required,
				sortOrder,
				domainOptions
		);
	}
}
