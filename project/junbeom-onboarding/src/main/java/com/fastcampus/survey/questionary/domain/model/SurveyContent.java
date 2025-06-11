package com.fastcampus.survey.questionary.domain.model;

import lombok.Getter;


import java.util.List;

@Getter
public class SurveyContent {
    private Long id;
    private Long formId;
    private String name;
    private String describe;
    private SurveyContentType type;
    private boolean isRequired;
    private List<SurveyContentOption> options;


} 