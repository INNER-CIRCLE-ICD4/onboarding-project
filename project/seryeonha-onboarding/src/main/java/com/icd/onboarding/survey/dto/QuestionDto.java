package com.icd.onboarding.survey.dto;

import com.icd.onboarding.survey.domain.Question;
import com.icd.onboarding.survey.domain.QuestionType;
import com.icd.onboarding.survey.domain.Survey;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

public class QuestionDto {

    @NoArgsConstructor
    @Getter
    @Setter
    public static class Create {
        private int orderNum;
        private String name;
        private String description;
        private QuestionType type;
        private boolean required;
        private boolean active;
        private List<OptionDto.Create> options = new ArrayList<>();

        public Question toEntity(Survey survey, Integer tempId) {
            return Question.builder()
                    .surveyId(survey.getId())
                    .surveyVersion(survey.getVersion())
                    .orderNum(orderNum)
                    .name(name)
                    .description(description)
                    .type(type)
                    .required(required)
                    .active(active)
                    .tempId(tempId)
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

        public static Read of(Question question) {
            return Read.builder()
                    .id(question.getId())
                    .name(question.getName())
                    .description(question.getDescription())
                    .type(question.getType())
                    .required(question.isRequired())
                    .options(question.getOptions().stream().map(OptionDto.Read::of).toList())
                    .build();
        }
    }
}
