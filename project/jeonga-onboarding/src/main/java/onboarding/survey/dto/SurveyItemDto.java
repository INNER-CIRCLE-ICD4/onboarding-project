package onboarding.survey.dto;

import onboarding.survey.domain.InputType;

import java.util.List;

public record SurveyItemDto(
        String name,
        String description,
        InputType inputType,
        boolean required,
        List<String> choices
) {}