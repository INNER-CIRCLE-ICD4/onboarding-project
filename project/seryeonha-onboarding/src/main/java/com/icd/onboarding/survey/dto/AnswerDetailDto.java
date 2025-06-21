package com.icd.onboarding.survey.dto;

import com.icd.onboarding.survey.domain.Answer;
import com.icd.onboarding.survey.domain.AnswerDetail;
import lombok.*;

public class AnswerDetailDto {

    @NoArgsConstructor
    @Getter
    @Setter
    public static class Create {
        private Long questionId;
        private String textAnswer;
        private Long selectedOptionId;

        public AnswerDetail toEntity(Answer answer) {
            return AnswerDetail.builder()
                    .answerId(answer.getId())
                    .questionId(questionId)
                    .textAnswer(textAnswer)
                    .selectedOptionId(selectedOptionId)
                    .build();
        }
    }

    @NoArgsConstructor
    @Getter
    @Setter
    @AllArgsConstructor
    @Builder
    public static class Read {
        private Long answerId;
        private String questionName;
        private String answer;

        public static Read of(Long answerId, String questionName, String answer) {
            return Read.builder()
                    .answerId(answerId)
                    .questionName(questionName)
                    .answer(answer)
                    .build();
        }
    }
}
