package com.INNER_CIRCLE_ICD4.innerCircle.survey.request.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.Valid;

import java.util.List;

public class CreateSurveyRequest {

    @NotBlank
    private String title;

    private String description;

    @Size(min = 1, max = 10)
    @Valid
    private List<QuestionRequest> questions;

    // Getters and Setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<QuestionRequest> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuestionRequest> questions) {
        this.questions = questions;
    }
}