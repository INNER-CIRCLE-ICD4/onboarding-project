package com.icd.onboarding.survey.dto;

import com.icd.onboarding.survey.domain.Answer;
import lombok.*;

import java.util.List;

public class AnswerDto {

    @NoArgsConstructor
    @Getter
    @Setter
    public static class Create {
        private Long surveyId;
        private Integer surveyVersion;
        private List<AnswerDetailDto.Create> details;

        public Answer toEntity() {
            return Answer.builder()
                    .surveyId(surveyId)
                    .surveyVersion(surveyVersion)
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
        private Long surveyId;
        private Integer surveyVersion;
        private List<AnswerDetailDto.Read> answerDetails;

//        public static Read of(Answer answer) {
//            return Read.builder()
//                    .id(answer.getId())
//                    .surveyId(answer.getSurveyId())
//                    .surveyVersion(answer.getSurveyVersion())
//                    .answerDetails(answer.getAnswerDetails().stream().map(AnswerDetailDto.Read::of).toList())
//                    .build();
//        }
    }
}
