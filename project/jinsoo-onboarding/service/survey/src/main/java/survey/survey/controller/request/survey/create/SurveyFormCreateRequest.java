package survey.survey.controller.request.survey.create;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record SurveyFormCreateRequest(
        @NotBlank(message = "설문조사 이름은 필수입니다") String title,
        @NotBlank(message = "설문조사 설명은 필수입니다") String description,
        @NotNull(message = "질문 목록은 필수입니다")
        @Size(min = 1, max = 10, message = "질문은 1개 이상 10개 이하여야 합니다")
        @Valid List<QuestionCreateRequest> questionList) {
    public SurveyFormCreateRequest(String title, String description, List<QuestionCreateRequest> questionList) {
        this.title = title;
        this.description = description;
        this.questionList = questionList;
    }
}