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

    @ManyToOne
    private Survey survey;

    private String name;

    private String description;

    @Enumerated(EnumType.STRING)
    private QuestionType type;

    private boolean required;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Option> options = new ArrayList<>();

    @Builder
    private Question(Survey survey, String name, String description, QuestionType type, boolean required, List<Option> options) {
        this.survey = survey;
        this.name = name;
        this.description = description;
        this.type = type;
        this.required = required;
        if (!ObjectUtils.isEmpty(options)) {
            options.forEach(this::addOption);
        }
    }

    private void addOption(Option option) {
        option.linkToQuestion(this);
        this.options.add(option);
    }

    public void linkToSurvey(Survey survey) {
        this.survey = survey;
    }
}
