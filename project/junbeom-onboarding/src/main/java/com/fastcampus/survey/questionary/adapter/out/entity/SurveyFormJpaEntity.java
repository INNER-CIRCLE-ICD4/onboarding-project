package com.fastcampus.survey.questionary.adapter.out.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "survey_form")
public class SurveyFormJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(length = 100)
    private String describe;

    @Column(name = "create_at")
    private LocalDateTime createAt;

    @OneToMany(mappedBy = "form", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SurveyContentJpaEntity> contents;

    // getter, setter, 생성자 등
} 