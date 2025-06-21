package com.innercircle.onboarding.changzune_onboarding.survey.domain;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.List;


@Getter
@Entity
public class Question {
    public enum QuestionType {
        SHORT_TEXT, LONG_TEXT, SINGLE_CHOICE, MULTIPLE_CHOICE
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;        // 항목 이름
    private String description; // 항목 설명

    @Enumerated(EnumType.STRING)
    private QuestionType type;  // 입력 형태

    private boolean required;   // 필수 여부

    @ElementCollection
    private List<String> options; // 단일/다중 선택형 옵션

    @ManyToOne
    @JoinColumn(name = "survey_id")
    private Survey survey;

    // getter/setter

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setType(QuestionType type) {
        this.type = type;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public void setSurvey(Survey survey) {
        this.survey = survey;
    }


}
