package com.INNER_CIRCLE_ICD4.innerCircle.survey.request.domain;


import jakarta.persistence.*;

@Entity
public class QuestionOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "option_value")
    private String optionValue; // 선택지 내용

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    private Question question;

    // === Getters/Setters ===
    public Long getId() {
        return id;
    }

    public String getOptionValue() {
        return optionValue;
    }

    public void setOptionValue(String optionValue) {
        this.optionValue = optionValue;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }
}