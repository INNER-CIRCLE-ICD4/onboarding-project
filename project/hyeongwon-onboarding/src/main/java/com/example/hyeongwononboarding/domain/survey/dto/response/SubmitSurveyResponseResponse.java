package com.example.hyeongwononboarding.domain.survey.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import lombok.*;

/**
 * 설문 응답 제출 결과 DTO
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubmitSurveyResponseResponse {
    private String responseId;
    private String surveyId;
    private Integer surveyVersion;
    private String respondentEmail;
    private LocalDateTime submittedAt;
    private List<AnswerResponse> answers;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class AnswerResponse {
        private String questionId;
        private String questionName;
        private String answerText; // 주관식
        private List<SelectedOption> selectedOptions; // 객관식
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class SelectedOption {
        private String id;
        private String text;
    }
}
