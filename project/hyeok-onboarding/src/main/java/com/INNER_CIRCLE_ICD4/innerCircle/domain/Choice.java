package com.INNER_CIRCLE_ICD4.innerCircle.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
    private int choiceIndex;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    @JsonBackReference // ✅ 순환 참조 방지
    private Question question;

    public Choice(String text, int choiceIndex) {
        this.text = text;
        this.choiceIndex = choiceIndex;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public Choice(String text, Question question) {
        this.text = text;
        this.question = question;
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