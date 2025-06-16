package com.icd.onboarding.survey.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long surveyId;

    private int order;

    private String name;

    private String description;

    @Enumerated(EnumType.STRING)
    private QuestionType type;

    private boolean required;

    private boolean active;

    @Builder
    public Question(Long surveyId, int order, String name, String description, QuestionType type, boolean required, boolean active) {
        this.surveyId = surveyId;
        this.order = order;
        this.name = name;
        this.description = description;
        this.type = type;
        this.required = required;
        this.active = active;
    }
}
