package kr.co.fastcampus.onboarding.hyeongminonboarding.domain.api.survey.dto.request;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubmitSurveyAnswersRequest {
    @NotEmpty
    @Valid
    private List<SubmitAnswerRequest> answers;
}
