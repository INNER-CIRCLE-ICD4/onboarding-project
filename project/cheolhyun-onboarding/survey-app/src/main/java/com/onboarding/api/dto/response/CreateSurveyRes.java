package com.onboarding.api.dto.response;

import lombok.Data;

import java.util.UUID;


@Data
public class CreateSurveyRes
{
    private String surveyId;
    private String message;

    public CreateSurveyRes(UUID serviceId, String message) {
        this.surveyId = serviceId.toString();
        this.message = message;
    }
}
