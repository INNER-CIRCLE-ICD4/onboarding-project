package kr.co.fastcampus.onboarding.hyeongminonboarding.domain.api.survey.dto.request;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
public class SurveyUpdateRequest {

    @NotNull
    private Long id;

    @NotBlank
    private String title;

    private String description;

    @Size(min = 1, max = 10)
    private List<QuestionSurveyRequest> questions;


    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class QuestionSurveyRequest {

        private Long id;            // 기존 질문이 있는 경우 재활용할 ID, 신규는 null

        @NotBlank
        private String title;

        private String detail;

        @NotBlank
        private QuestionType type;

        private boolean required;

        @Valid
        private List<@NotBlank String> options; // 선택형일 때만 사용, 서비스에서 조건 검사
    }

}
