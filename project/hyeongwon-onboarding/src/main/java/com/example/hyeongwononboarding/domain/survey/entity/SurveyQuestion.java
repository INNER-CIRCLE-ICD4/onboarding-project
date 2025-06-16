package com.example.hyeongwononboarding.domain.survey.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 설문조사 질문 엔티티
 * 각 설문 버전별 질문 정보와 옵션을 관리합니다.
 */
@Entity
@Table(name = "survey_questions")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SurveyQuestion {
    @Id
    @Column(name = "id", length = 36)
    private String id;

    @Column(name = "survey_version_id", length = 36, nullable = false)
    private String surveyVersionId;

    @Column(name = "question_order", nullable = false)
    private Integer questionOrder;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "input_type", nullable = false)
    private QuestionInputType inputType;

    @Column(name = "is_required", nullable = false)
    private Boolean isRequired = false;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "survey_version_id", insertable = false, updatable = false)
    private SurveyVersion surveyVersion;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OrderBy("optionOrder ASC")
    private List<QuestionOption> options = new ArrayList<>();

    @Builder
    public SurveyQuestion(String id, String surveyVersionId, Integer questionOrder, String name, String description, QuestionInputType inputType, Boolean isRequired) {
        this.id = id;
        this.surveyVersionId = surveyVersionId;
        this.questionOrder = questionOrder;
        this.name = name;
        this.description = description;
        this.inputType = inputType;
        this.isRequired = isRequired != null ? isRequired : false;
    }
    
    /**
     * QuestionOption을 추가하는 헬퍼 메서드
     * @param option 추가할 QuestionOption
     */
    public void addOption(QuestionOption option) {
        this.options.add(option);
        option.setQuestion(this);
    }
}
