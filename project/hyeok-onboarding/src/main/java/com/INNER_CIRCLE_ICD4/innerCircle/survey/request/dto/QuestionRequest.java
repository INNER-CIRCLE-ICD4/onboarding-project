package com.INNER_CIRCLE_ICD4.innerCircle.survey.request.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

import com.INNER_CIRCLE_ICD4.innerCircle.survey.request.dto.QuestionType;

public class QuestionRequest {

    @NotBlank
    private String questionText;

    private String description;

    @NotNull
    private QuestionType type;

    private boolean required;

    private List<String> options;

    // Getters and Setters
    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public QuestionType getType() {
        return type;
    }

    public void setType(QuestionType type) {
        this.type = type;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }
}