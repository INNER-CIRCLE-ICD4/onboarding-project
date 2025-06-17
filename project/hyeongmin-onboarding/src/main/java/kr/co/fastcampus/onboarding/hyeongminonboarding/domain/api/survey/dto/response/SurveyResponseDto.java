package kr.co.fastcampus.onboarding.hyeongminonboarding.domain.api.survey.dto.response;


import kr.co.fastcampus.onboarding.hyeongminonboarding.domain.entity.enums.QuestionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SurveyResponseDto {
    private Long surveyId;
    private String title;
    private String description;
    private List<QuestionResponseDto> questions;


    @Data
    @Builder
    public static class QuestionResponseDto {
        private Long id;
        private String title;
        private String detail;
        private QuestionType type;
        private boolean required;
        private List<QuestionOptionResponseDto> options;
    }


    @Data
    @Builder
    public static class QuestionOptionResponseDto {
        private Long id;
        private String value;
    }
}
