package com.INNER_CIRCLE_ICD4.innerCircle.survey.request.domain;


import jakarta.persistence.*;

@Entity
public class QuestionOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String optionValue; // 선택지 내용

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    private Question question;

    // === Getters/Setters ===
    public Long getId() {
        return id;
    }

    public String getValue() {
        return optionValue;
    }

    public void setValue(String value) {
        this.optionValue = value;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }
}