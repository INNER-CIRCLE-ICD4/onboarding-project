package com.fastcampus.survey.questionary.adapter.out.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "survey_content")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // JPA 기본 생성자
public class SurveyContentJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "form_id")
    @Setter
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
    @Setter
    private List<SurveyContentOptionJpaEntity> options;

    @Builder
    protected SurveyContentJpaEntity(Long id, String name, String describe, SurveyContentType type,
                                     boolean isRequired) {
        this.id = id;
        this.name = name;
        this.describe = describe;
        this.type = type;
        this.isRequired = isRequired;
    }
} 