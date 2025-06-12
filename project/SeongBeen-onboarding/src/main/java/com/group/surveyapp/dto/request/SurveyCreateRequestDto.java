package com.group.surveyapp.dto.request;

import com.group.surveyapp.domain.entity.QuestionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class SurveyCreateRequestDto {
    @NotBlank
    private String title;

    private String description;

    @NotEmpty
    @Size(min = 1, max = 10)
    private List<QuestionDto> questions;

    @Data
    public static class QuestionDto {
        @NotBlank
        private String name;

        private String description;

        @NotNull
        private QuestionType type;

        private boolean required;

        private List<String> candidates;
    }
}
