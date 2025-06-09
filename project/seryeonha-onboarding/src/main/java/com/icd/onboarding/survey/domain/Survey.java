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
public class Survey {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    @OneToMany(mappedBy = "survey", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Question> questions = new ArrayList<>();

    @Builder
    public Survey(String name, String description, List<Question> questions) {
        this.name = name;
        this.description = description;
        if (!ObjectUtils.isEmpty(questions)) {
            questions.forEach(this::addQuestion);
        }
    }

    private void addQuestion(Question question) {
        question.linkToSurvey(this);
        this.questions.add(question);
    }
}
