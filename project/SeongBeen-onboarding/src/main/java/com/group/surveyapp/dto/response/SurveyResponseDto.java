package com.group.surveyapp.dto.response;

import com.group.surveyapp.domain.entity.Question;
import com.group.surveyapp.domain.entity.QuestionType;
import com.group.surveyapp.domain.entity.Survey;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SurveyResponseDto {

    private Long surveyId;
    private String title;
    private String description;
    private String createdAt; // 또는 LocalDateTime

    private List<QuestionDto> questions;

    public static SurveyResponseDto from(Survey survey) {
        if (survey == null) return null;

        return SurveyResponseDto.builder()
                .surveyId(survey.getId())
                .title(survey.getTitle())
                .description(survey.getDescription())
                .createdAt(survey.getCreatedAt() != null ? survey.getCreatedAt().toString() : null)
                .questions(
                        survey.getQuestions() != null ?
                                survey.getQuestions().stream()
                                        .map(QuestionDto::from)
                                        .collect(Collectors.toList())
                                : List.of()
                )
                .build();
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class QuestionDto {
        private Long questionId;
        private String name;
        private String description;
        private QuestionType type;
        private boolean required;
        private List<String> candidates;

        public static QuestionDto from(Question question) {
            if (question == null) return null;

            return QuestionDto.builder()
                    .questionId(question.getId())
                    .name(question.getName())
                    .description(question.getDescription())
                    .type(question.getType())
                    .required(question.isRequired())
                    .candidates(question.getCandidates())
                    .build();
        }
    }
}
