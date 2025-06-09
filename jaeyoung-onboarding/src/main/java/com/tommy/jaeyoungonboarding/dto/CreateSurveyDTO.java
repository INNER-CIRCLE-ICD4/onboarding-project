package com.tommy.jaeyoungonboarding.dto;

import com.tommy.jaeyoungonboarding.entity.SurveyItemForm;
import lombok.Data;


@Data
public class CreateSurveyDTO {

    // Survey Entity
    private String title;
    private String description;

    // SurveyItem
    private String surveyItemTitle;
    private String surveyItemDescription;
    private SurveyItemForm surveyItemForm;
    private boolean surveyItemEssential;
}
