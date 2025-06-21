package com.onboarding.api.dto;

import com.onboarding.model.survey.Survey;

public record SurveyReq(
        String title,
        String description
) {

    public static Survey toDomain(SurveyReq surveyReq) {
        return Survey.builder()
                .title(surveyReq.title())
                .description(surveyReq.description())
            .build();
    }
}
