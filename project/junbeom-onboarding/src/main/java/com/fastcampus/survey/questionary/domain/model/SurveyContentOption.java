package com.fastcampus.survey.questionary.domain.model;


import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
public class SurveyContentOption {

    private Long id;
    private Long contentId;
    private String text;

}