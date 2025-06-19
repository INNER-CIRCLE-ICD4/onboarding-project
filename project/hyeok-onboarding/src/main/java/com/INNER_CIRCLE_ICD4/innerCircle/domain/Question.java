package com.INNER_CIRCLE_ICD4.innerCircle.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
    @JsonBackReference // \uD83D\uDD01 순환 참조 방지 (Survey \u2192 Question)
    private Survey survey;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderColumn(name = "choice_order")
    @JsonManagedReference // \uD83D\uDD01 순환 참조 방지 (Question \u2192 Choice)
    private List<Choice> choices = new ArrayList<>();

    // 생성자
    public Question(String title, String description, QuestionType type, boolean required) {
        this.title = title;
        this.description = description;
        this.type = type;
        this.required = required;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // 엔티티 수정 메서드
    public void update(String title, String description, QuestionType type, boolean required) {
        this.title = title;
        this.description = description;
        this.type = type;
        this.required = required;
        this.updatedAt = LocalDateTime.now();
    }

    // Survey 연관관계 설정
    public void setSurvey(Survey survey) {
        this.survey = survey;
    }

    // 선택지 추가 메서드
    public void addChoice(Choice choice) {
        if (choices.size() >= 20) {
            throw new IllegalStateException("선택지는 최대 20개까지만 허용됩니다.");
        }
        choice.setQuestion(this);
        this.choices.add(choice);
    }

    // 테스트 및 직렬화 용도: ID 수동 설정
    public void setId(UUID id) {
        this.id = id;
    }

    // 시간 자동 설정 (JPA 라이프사이클 콜백)
    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
