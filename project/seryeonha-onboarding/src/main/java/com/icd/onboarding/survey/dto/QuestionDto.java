package com.icd.onboarding.survey.dto;

import com.icd.onboarding.survey.domain.Question;
import com.icd.onboarding.survey.domain.QuestionType;
import lombok.*;
import org.springframework.util.ObjectUtils;

import java.util.Collections;
import java.util.List;

public class QuestionDto {

    @NoArgsConstructor
    @Getter
    @Setter
    public static class Create {
        private String name;
        private String description;
        private QuestionType type;
        private boolean required;
        private List<OptionDto.Create> options;

        public Question toEntity() {
            return Question.builder()
                    .name(name)
                    .description(description)
                    .type(type)
                    .required(required)
                    .options(ObjectUtils.isEmpty(options)
                            ? Collections.emptyList()
                            : options.stream().map(OptionDto.Create::toEntity).toList())
                    .build();
        }
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
                    .options(ObjectUtils.isEmpty(question.getOptions())
                            ? Collections.emptyList()
                            : question.getOptions().stream().map(OptionDto.Read::fromEntity).toList())
                    .build();
        }
    }
}
