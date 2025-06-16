package survey.survey.controller.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import survey.survey.entity.surveyquestion.InputType;

import java.util.List;

public record SurveyFormCreateRequest(Long surveyId,
                                      @NotBlank(message = "설문조사 이름은 필수입니다") String title,
                                      @NotBlank(message = "설문조사 설명은 필수입니다") String description,
                                      @NotNull(message = "질문 목록은 필수입니다")
                                      @Size(min = 1, max = 10, message = "질문은 1개 이상 10개 이하여야 합니다")
                                      @Valid List<QuestionCreateRequest> questionList) {
    public SurveyFormCreateRequest(Long surveyId, String title, String description, List<QuestionCreateRequest> questionList) {
        this.surveyId = surveyId;
        this.title = title;
        this.description = description;
        this.questionList = questionList;
    }

    public record QuestionCreateRequest(
            @NotBlank(message = "질문 이름은 필수입니다") String name, int questionIndex,
            @NotBlank(message = "질문 설명은 필수입니다") String description,
            @NotNull(message = "입력 형태는 필수입니다") InputType inputType, boolean required,
            @Valid List<CandidateCreateRequest> candidates) {
        public QuestionCreateRequest(String name, int questionIndex, String description,
                                     InputType inputType, boolean required, List<CandidateCreateRequest> candidates) {
            this.name = name;
            this.questionIndex = questionIndex;
            this.description = description;
            this.inputType = inputType;
            this.required = required;
            this.candidates = candidates;
        }

        public record CandidateCreateRequest(
                int checkCandidateIndex,
                @NotBlank(message = "선택 항목 값은 필수입니다") String name) {
            public CandidateCreateRequest(int checkCandidateIndex, String name) {
                this.checkCandidateIndex = checkCandidateIndex;
                this.name = name;
            }
        }
    }
}