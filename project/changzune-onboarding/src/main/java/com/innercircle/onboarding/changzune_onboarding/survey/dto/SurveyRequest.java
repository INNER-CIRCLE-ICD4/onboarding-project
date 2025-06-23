package com.innercircle.onboarding.changzune_onboarding.survey.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public class SurveyRequest {

    @NotBlank
    private String title; // 설문조사 이름

    private String description; // 설문조사 설명

    @NotNull
    @Size(min = 1, max = 10)
    private List<QuestionRequest> questions; // 설문 항목 리스트

    // 내부 클래스로 질문 항목 정의
    public static class QuestionRequest {

        @NotBlank
        private String name; // 항목 이름

        private String description; // 항목 설명

        @NotBlank
        private String type; // SHORT_TEXT, LONG_TEXT, SINGLE_CHOICE, MULTIPLE_CHOICE

        private boolean required; // 필수 여부

        private List<String> options; // 선택형일 경우 옵션

        // getter/setter
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
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

        public Long getId() {
            return 0L;
        }
    }

    // getter/setter
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