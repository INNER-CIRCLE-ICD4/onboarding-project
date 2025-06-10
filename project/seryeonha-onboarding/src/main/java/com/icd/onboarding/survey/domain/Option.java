package com.icd.onboarding.survey.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Option {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Question question;

    private String content;

    @Builder
    public Option(Question question, String content) {
        this.question = question;
        this.content = content;
    }

    @Builder
    public Option(Long id, Question question, String content) {
        this.id = id;
        this.question = question;
        this.content = content;
    }

    public void linkToQuestion(Question question) {
        this.question = question;
    }
}
