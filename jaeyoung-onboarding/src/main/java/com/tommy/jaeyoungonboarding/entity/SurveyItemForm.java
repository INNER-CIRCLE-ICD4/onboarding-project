package com.tommy.jaeyoungonboarding.entity;

public enum SurveyItemForm {
    SHORT_SENTENCE("단답형"),
    LONG_SENTENCE("장문형"),
    SINGLE_SELECTION("단일 선택 리스트"),
    MULTIPLE_SELECTION("다중 선택 리스트");

    private String itemSurveyForm;

    SurveyItemForm(String itemSurveyForm) {
        this.itemSurveyForm = itemSurveyForm;
    }

    public String getItemSurveyForm(){
        return itemSurveyForm;
    }
}
