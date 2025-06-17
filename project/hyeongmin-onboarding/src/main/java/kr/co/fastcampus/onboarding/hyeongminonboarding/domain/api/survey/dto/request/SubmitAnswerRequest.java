package kr.co.fastcampus.onboarding.hyeongminonboarding.domain.api.survey.dto.request;


import jakarta.validation.constraints.NotNull;
import kr.co.fastcampus.onboarding.hyeongminonboarding.domain.entity.enums.QuestionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class SubmitAnswerRequest {
    @NotNull
    private Long questionId;

    private String answerText; // 단답/장문형일 경우 (ValidCheck 안됨 서비스단에서 함)
    private List<Long> selectedOptionIds; // 선택형일 경우 (ValidCheck 암됨 서비스단에서 함)
}
