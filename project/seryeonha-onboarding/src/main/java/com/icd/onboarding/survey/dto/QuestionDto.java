package com.icd.onboarding.survey.dto;

import com.icd.onboarding.survey.domain.Question;
import com.icd.onboarding.survey.domain.QuestionType;
import lombok.*;

import java.util.List;

public class QuestionDto {

    @NoArgsConstructor
    @Getter
    @Setter
    public static class Create {
        private int order;
        private String name;
        private String description;
        private QuestionType type;
        private boolean required;
        private boolean active;
        private List<OptionDto.Create> options;
    }

    @NoArgsConstructor
    @Getter
    @Setter
    public static class Update {
        private String name;
        private String description;
        private QuestionType type;
        private boolean required;
        private List<OptionDto.Update> options;
    }

    @NoArgsConstructor
    @Getter
    @Setter
    @AllArgsConstructor
    @Builder
    public static class Read {
        private Long id;
        private String name;
        private String description;
        private QuestionType type;
        private boolean required;
        private List<OptionDto.Read> options;

        public static Read fromEntity(Question question) {
            return Read.builder()
                    .id(question.getId())
                    .name(question.getName())
                    .description(question.getDescription())
                    .type(question.getType())
                    .required(question.isRequired())
                    .build();
        }
    }
}
