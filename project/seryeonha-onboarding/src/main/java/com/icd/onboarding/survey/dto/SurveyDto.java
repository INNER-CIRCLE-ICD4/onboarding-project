package com.icd.onboarding.survey.dto;

import com.icd.onboarding.survey.domain.Option;
import com.icd.onboarding.survey.domain.Question;
import com.icd.onboarding.survey.domain.Survey;
import lombok.*;

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
                    .build();
        }
    }

    @NoArgsConstructor
    @Getter
    @Setter
    public static class Update {
        private String name;
        private String description;
        private List<QuestionDto.Create> questions;
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
        private Integer version;

        public static Read of(Survey survey) {
            return Read.builder()
                    .id(survey.getId())
                    .name(survey.getName())
                    .description(survey.getDescription())
                    .version(survey.getVersion())
                    .build();
        }
    }

}
