package com.icd.onboarding.survey.dto;

import com.icd.onboarding.survey.domain.Option;
import com.icd.onboarding.survey.domain.Question;
import lombok.*;

public class OptionDto {

    @NoArgsConstructor
    @Getter
    @Setter
    public static class Create {
        private int orderNum;
        private String content;
        private boolean active;

        public Option toEntity(Question question) {
            return Option.builder()
                    .questionId(question.getId())
                    .orderNum(orderNum)
                    .content(content)
                    .active(active)
                    .build();
        }
    }

    @NoArgsConstructor
    @Getter
    @Setter
    public static class Update {
        private Long id;
        private String content;
    }

    @NoArgsConstructor
    @Getter
    @Setter
    @AllArgsConstructor
    @Builder
    public static class Read {
        private Long id;
        private Long questionId;
        private Integer orderNum;
        private String content;
        private boolean active;

        public static Read fromEntity(Option option) {
            return Read.builder()
                    .id(option.getId())
                    .questionId(option.getQuestionId())
                    .orderNum(option.getOrderNum())
                    .content(option.getContent())
                    .active(option.isActive())
                    .build();
        }
    }
}
