package com.group.surveyapp.domain.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 설문조사와 다대일 관계
    @ManyToOne
    @JoinColumn(name = "survey_id")
    private Survey survey;

    private String name; // 항목 이름

    private String description; // 항목 설명

    @Enumerated(EnumType.STRING)
    private QuestionType type; // SHORT, LONG, SINGLE, MULTI

    private boolean required; // 필수 여부

    // 선택형인 경우 후보값 (ElementCollection)
    @ElementCollection
    @CollectionTable(
            name = "question_candidates",
            joinColumns = @JoinColumn(name = "question_id")
    )
    @Column(name = "candidate")
    private List<String> candidates;

}
