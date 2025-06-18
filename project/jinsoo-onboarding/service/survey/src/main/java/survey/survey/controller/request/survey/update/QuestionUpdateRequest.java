package survey.survey.controller.request.survey.update;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import survey.survey.entity.surveyquestion.InputType;

import java.util.List;

public record QuestionUpdateRequest(@NotBlank(message = "질문 이름은 필수입니다") String name,
                                    int questionIndex,
                                    @NotBlank(message = "질문 설명은 필수입니다") String description,
                                    @NotNull(message = "입력 형태는 필수입니다") InputType inputType,
                                    boolean required,
                                    @Valid List<CandidateUpdateRequest> candidates) {
    public QuestionUpdateRequest(String name, int questionIndex, String description,
                                 InputType inputType, boolean required, List<CandidateUpdateRequest> candidates) {
        this.name = name;
        this.questionIndex = questionIndex;
        this.description = description;
        this.inputType = inputType;
        this.required = required;
        this.candidates = candidates;
    }
}
