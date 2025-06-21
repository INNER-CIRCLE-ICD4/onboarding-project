package com.innercircle.onboarding.answer.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

public class AnswerDto {

    @ToString
    @Getter
    @NoArgsConstructor
    public static class Create {
        @NotNull
        private Long questionSeq;
        @Size(max=100)
        private String content;

        public Answer toEntity(String originQuestion) {
            return Answer.builder()
                    .questionSeq(questionSeq)
                    .originQuestion(originQuestion)
                    .content(content)
                    .build();
        }

    }

}
