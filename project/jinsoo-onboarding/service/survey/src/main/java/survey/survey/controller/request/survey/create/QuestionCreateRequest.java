package survey.survey.controller.request.survey.create;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import survey.survey.entity.surveyquestion.InputType;

import java.util.ArrayList;
import java.util.List;

public record QuestionCreateRequest(
        @NotBlank(message = "질문 이름은 필수입니다") String name, int questionIndex,
        @NotBlank(message = "질문 설명은 필수입니다") String description,
        @NotNull(message = "입력 형태는 필수입니다") InputType inputType, boolean required,
        @Valid List<CandidateCreateRequest> candidates){
}
