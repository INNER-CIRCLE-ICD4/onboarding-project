package com.icd.onboarding.survey.dto;

import com.icd.onboarding.survey.domain.Survey;
import lombok.*;
import org.springframework.util.ObjectUtils;

import java.util.Collections;
import java.util.List;

public class SurveyDto {

    @NoArgsConstructor
    @Getter
    @Setter
    public static class Create {
        private String name;
        private String description;
        private List<QuestionDto.Create> questions;

        public Survey toEntity() {
            return Survey.builder()
                    .name(name)
                    .description(description)
                    .questions(ObjectUtils.isEmpty(questions)
                                ? Collections.emptyList()
                                : questions.stream().map(QuestionDto.Create::toEntity).toList())
                    .build();
        }
    }

    @NoArgsConstructor
    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    public static class Read {
        private Long id;
        private String name;
        private String description;
        private List<QuestionDto.Read> questions;

        public static Read fromEntity(Survey survey) {
            return Read.builder()
                    .id(survey.getId())
                    .name(survey.getName())
                    .description(survey.getDescription())
                    .questions(ObjectUtils.isEmpty(survey.getQuestions())
                            ? Collections.emptyList()
                            : survey.getQuestions().stream().map(QuestionDto.Read::fromEntity).toList())
                    .build();
        }
    }

}
