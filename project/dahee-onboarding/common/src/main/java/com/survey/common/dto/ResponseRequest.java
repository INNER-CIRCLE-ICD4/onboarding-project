package com.survey.common.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class ResponseRequest {
    @NotBlank
    private String uuid;              // 익명 응답자 식별자

    @NotEmpty
    private List<Answer> answers;     // 문항별 응답 리스트

    @Getter @Setter
    public static class Answer {
        @NotNull
        private Long itemId;           // SurveyItem.id

        @NotBlank
        private String answer;         // 실제 답변
    }
}
