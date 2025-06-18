package com.example.hyeongwononboarding.domain.survey.dto.request;

import lombok.*;
import java.util.List;

/**
 * 설문 응답 제출 요청 DTO
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubmitSurveyResponseRequest {
    private String surveyVersionId;
    private String respondentEmail; // 익명 응답 가능
    private List<AnswerDto> answers;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class AnswerDto {
        private String questionId;
        private String answerText;
        private List<String> selectedOptionIds; // 선택형 문항의 경우
    }
}
