package com.INNER_CIRCLE_ICD4.innerCircle.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Question {

    @Id
    @GeneratedValue
    private UUID id;

    private String title;
    private String description;

    @Enumerated(EnumType.STRING)
    private QuestionType type;

    private boolean required;
    private boolean isDeleted = false;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "survey_id")
    private Survey survey;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderColumn(name = "choice_order")
    private List<Choice> choices = new ArrayList<>();

    public Question(String title, String description, QuestionType type, boolean required) {
        this.title = title;
        this.description = description;
        this.type = type;
        this.required = required;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void update(String title, String description, QuestionType type, boolean required) {
        this.title = title;
        this.description = description;
        this.type = type;
        this.required = required;
        this.updatedAt = LocalDateTime.now();
    }

    public void setSurvey(Survey survey) {
        this.survey = survey;
    }

    public void addChoice(Choice choice) {
        if (choices.size() >= 20) {
            throw new IllegalStateException("선택지는 최대 20개까지만 허용됩니다.");
        }
        choice.setQuestion(this);
        this.choices.add(choice);
    }

    // ✅ 테스트용 ID 설정 메서드
    public void setId(UUID id) {
        this.id = id;
    }
}
