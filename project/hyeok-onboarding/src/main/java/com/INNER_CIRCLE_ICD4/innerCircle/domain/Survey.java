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

    private int version = 1;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "survey", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderColumn(name = "question_order")
    @JsonIgnore // ✅ 직렬화 무한루프 방지용
    private List<Question> questions = new ArrayList<>();

    public Survey(String title, String description) {
        this.title = title;
        this.description = description;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void update(String title, String description) {
        this.title = title;
        this.description = description;
        this.version += 1;
        this.updatedAt = LocalDateTime.now();
    }

    public void addQuestion(Question question) {
        question.setSurvey(this);
        this.questions.add(question);
    }
}
