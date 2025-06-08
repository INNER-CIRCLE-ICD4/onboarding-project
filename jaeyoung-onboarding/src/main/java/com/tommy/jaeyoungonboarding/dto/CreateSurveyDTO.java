package com.tommy.jaeyoungonboarding.dto;

import com.tommy.jaeyoungonboarding.entity.SurveyItemForm;
import lombok.Data;

import java.util.UUID;

@Data
public class CreateSurveyDTO {

    // Survey Entity
    private String title;
    private String description;

    // SurveyItem
    private String surveyItemTitle;
    private String surveyItemEDescription;
    private SurveyItemForm surveyItemForm;
    private boolean itemEssential;
    private UUID surveyId;
}
