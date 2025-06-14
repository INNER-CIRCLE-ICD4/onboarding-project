package com.example.demo.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Question {

    @Id
    @GeneratedValue
    private Long id;

    // 항목 이름
    private String name;

    // 항목 설명
    private String description;

    // 항목 입력 형태
    @Enumerated(EnumType.STRING)
    private InputType inputType;

    // 항목 필수 여부
    private boolean required;

    @ManyToOne
    @JoinColumn(name = "survey_id")
    private Survey survey;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<QuestionOption> options = new ArrayList<>(); // 선택지
}

