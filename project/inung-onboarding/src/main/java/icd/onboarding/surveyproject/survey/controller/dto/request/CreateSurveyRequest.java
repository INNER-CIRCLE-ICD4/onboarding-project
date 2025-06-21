package icd.onboarding.surveyproject.survey.controller.dto.request;

import icd.onboarding.surveyproject.survey.service.domain.Question;
import icd.onboarding.surveyproject.survey.service.domain.Survey;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

public record CreateSurveyRequest(
		@NotBlank(message = "설문 조사의 제목은 필수 입력 항목입니다.")
		String title,
		String description,
		@Size(min = 1, max = 10, message = "질문은 최소 1개 이상 최대 10개 이하로 등록할 수 있습니다.")
		@Valid
		List<CreateQuestionRequest> questions
) {
	public Survey toDomain () {
		List<Question> domainQuestions = questions.stream()
												  .map(CreateQuestionRequest::toDomain)
												  .toList();

		return Survey.create(title, description, domainQuestions);
	}
}
