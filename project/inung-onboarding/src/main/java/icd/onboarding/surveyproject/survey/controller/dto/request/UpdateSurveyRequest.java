package icd.onboarding.surveyproject.survey.controller.dto.request;

import icd.onboarding.surveyproject.survey.service.domain.Question;
import icd.onboarding.surveyproject.survey.service.domain.Survey;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.UUID;

public record UpdateSurveyRequest(
		@NotNull UUID id,
		@Min(1) int version,
		@NotBlank String title,
		String description,
		@Size(min = 1, max = 10, message = "질문은 최소 1개 이상, 최대 10개 이하로 등록해야 합니다.")
		@Valid
		List<UpdateQuestionRequest> questions
) {
	public Survey toDomain () {
		List<Question> domainQuestions = questions.stream()
												  .map(UpdateQuestionRequest::toDomain)
												  .toList();

		return Survey.create(title, description, domainQuestions);
	}
}
