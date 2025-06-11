package com.INNER_CIRCLE_ICD4.innerCircle.onboarding.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Choice {

    @Id
    @GeneratedValue
    private UUID id;

    private String text;

    private int choiceOrder;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    private Question question;

    public Choice(String text, int choiceOrder) {
        this.text = text;
        this.choiceOrder = choiceOrder;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void updateText(String newText) {
        this.text = newText;
        this.updatedAt = LocalDateTime.now();
    }

    public void setQuestion(Question question) {
        this.question = question;
    }
}
