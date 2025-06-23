package com.group.surveyapp.domain.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 응답지(AnswerSheet)와 다대일 연관
    @ManyToOne
    @JoinColumn(name = "answer_sheet_id")
    private AnswerSheet answerSheet;

    // 질문(Question)과 다대일 연관
    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;

    // 실제 응답 값 (문자/JSON 등)
    @Lob
    @Column(name = "answer_value")
    private String answerValue;

    // 응답 값 세팅
    public void setAnswerValue(String answerValue) {
        this.answerValue = answerValue;
    }
}
