package com.onboarding.entity;

import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Table(name = "survey")
public class SurveyEntity {

    @Id
    @GeneratedValue(generator = "uuid2")
    @Column(name="survey_id", columnDefinition = "BINARY(16)", updatable = false, nullable = false)
    private UUID id;

    @Column(nullable = false)
    private String title;

    @Column(name="description")
    private String description;

    @OneToMany(mappedBy = "surveyEntity", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<QuestionEntity> questions = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @CreationTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public SurveyEntity(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public SurveyEntity() {

    }

    public void setQuestions(List<QuestionEntity> questionList) {
        this.questions = questionList;
    }
}
