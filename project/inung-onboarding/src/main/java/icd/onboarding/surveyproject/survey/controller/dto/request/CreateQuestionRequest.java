package icd.onboarding.surveyproject.survey.controller.dto.request;

import icd.onboarding.surveyproject.survey.service.domain.Option;
import icd.onboarding.surveyproject.survey.service.domain.Question;
import icd.onboarding.surveyproject.survey.service.enums.InputType;
import icd.onboarding.surveyproject.survey.service.validator.ValidInputType;

import java.util.List;

public record CreateQuestionRequest(
		String name,
		String description,
		@ValidInputType String inputType,
		boolean required,
		int sortOrder,
		List<CreateOptionRequest> options
) {
	public Question toDomain () {
		List<Option> domainOptions = options != null ?
				options.stream()
					   .map(CreateOptionRequest::toDomain)
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
