package com.onboarding.entity;

import com.onboarding.model.QuestionType;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Table(name = "questions")
public class QuestionEntity {
    @Id
    @GeneratedValue(generator = "uuid2")
    @Column(name="question_id", columnDefinition = "BINARY(16)", updatable = false, nullable = false)
    private UUID id;

    @Column(nullable = false)
    private String title;

    @Column(name="description")
    private String description;

    @Column(name = "question_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private QuestionType type;

    @OneToMany(mappedBy = "questionEntity", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<OptionEntity> options = new ArrayList<>();

    @Column(name = "is_required", nullable = false)
    private boolean isRequired;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "survey_id")
    private SurveyEntity surveyEntity;

    @Column(name = "deleted", nullable = false)
    private boolean deleted = false;

    public QuestionEntity(String title, String description, QuestionType type, boolean required, SurveyEntity surveyEntity) {
        this.title = title;
        this.description = description;
        this.type = type;
        this.isRequired = required;
        this.surveyEntity = surveyEntity;
    }

    public QuestionEntity() {

    }

    public void setOptions(List<OptionEntity> options) {
        this.options = options;
    }
}
