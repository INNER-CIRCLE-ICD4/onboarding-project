package com.survey.common.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SurveyAnswerResponseDto {
    private Long responseId;
    private Long surveyId;
    private String uuid;
    private LocalDateTime submittedAt;
    private List<Answer> answers;

    @Getter
    @AllArgsConstructor
    public static class Answer {
        private Long itemId;
        private String question;
        private String answer;
    }
}
