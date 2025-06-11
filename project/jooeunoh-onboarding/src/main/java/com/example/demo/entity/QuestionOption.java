package com.example.demo.entity;

import jakarta.persistence.*;

@Entity
public class QuestionOption {
    @Id
    @GeneratedValue
    private Long id;

    // 선택지 텍스트
    private String value;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    private Question question;
}

