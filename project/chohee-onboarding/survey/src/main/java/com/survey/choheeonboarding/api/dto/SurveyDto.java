package com.survey.choheeonboarding.api.dto;

import java.util.List;

public class SurveyDto {


    public record CreateSurveyRequest(
            String title,
            String description,
            List<QuestionDto> questions
    ) {}

    public record UpdateSurveyRequest(
            String id,
            CreateSurveyRequest survey
    ) {}

    public record QuestionDto(
            String questionText,
            String questionDescription,
            String questionType,
            String isRequired,
            List<QuestionOptionDto> options
    ) {}

    public record QuestionOptionDto(
            String optionText
    ) {}



}
