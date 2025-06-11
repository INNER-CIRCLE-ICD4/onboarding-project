package com.fastcampus.survey.questionary.adapter.out.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "survey_content")
public class SurveyContentJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "form_id")
    private SurveyFormJpaEntity form;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(length = 50)
    private String describe;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private SurveyContentType type;

    @Column(nullable = false)
    private boolean isRequired;

    @OneToMany(mappedBy = "content", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SurveyContentOptionJpaEntity> options;

    // getter, setter, 생성자 등
} 