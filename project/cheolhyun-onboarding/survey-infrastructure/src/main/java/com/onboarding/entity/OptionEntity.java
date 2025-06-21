package com.onboarding.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.UUID;

@Getter
@Entity
@Table(name = "options")
public class OptionEntity {
    @Id
    @GeneratedValue(generator = "uuid2")
    @Column(name="option_id", columnDefinition = "BINARY(16)", updatable = false, nullable = false)
    private UUID id;

    @Column(nullable = false)
    private String text;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    private QuestionEntity questionEntity;

    public OptionEntity(String text, QuestionEntity questionEntity) {
        this.text = text;
        this.questionEntity = questionEntity;
    }

    public OptionEntity(UUID id, String text, QuestionEntity questionEntity) {
        this.id = id;
        this.text = text;
        this.questionEntity = questionEntity;
    }

    public OptionEntity() {
    }
}
