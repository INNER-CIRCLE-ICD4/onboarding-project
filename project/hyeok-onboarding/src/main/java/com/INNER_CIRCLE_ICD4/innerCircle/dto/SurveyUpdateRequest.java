package com.INNER_CIRCLE_ICD4.innerCircle.dto;

import java.util.List;

public class SurveyUpdateRequest {
    private String title;
    private String description;
    private List<QuestionUpdateRequest> questions;

    public SurveyUpdateRequest() {}

    public SurveyUpdateRequest(String title, String description, List<QuestionUpdateRequest> questions) {
        this.title = title;
        this.description = description;
        this.questions = questions;
    }

    // Getters & Setters
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public List<QuestionUpdateRequest> getQuestions() { return questions; }
    public void setQuestions(List<QuestionUpdateRequest> questions) { this.questions = questions; }
}
