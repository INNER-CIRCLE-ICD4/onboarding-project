package com.INNER_CIRCLE_ICD4.innerCircle.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Survey {

    @Id
    @GeneratedValue
    private UUID id;

    private String title;
    private String description;

    @Version
    private int version = 1;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @OneToMany(
            mappedBy = "survey",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @OrderColumn(name = "question_order")
    @JsonIgnore // 직렬화 무한 루프 방지
    private List<Question> questions = new ArrayList<>();

    public Survey(String title, String description) {
        this.title = title;
        this.description = description;
    }

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
        this.version++;
    }

    public void update(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public void addQuestion(Question question) {
        question.setSurvey(this);
        this.questions.add(question);
    }

    public void replaceQuestions(List<Question> newQuestions) {
        this.questions.clear();
        newQuestions.forEach(this::addQuestion);
    }
}
