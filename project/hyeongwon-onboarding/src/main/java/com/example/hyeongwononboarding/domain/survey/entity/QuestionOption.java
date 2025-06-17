package com.example.hyeongwononboarding.domain.survey.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 설문조사 질문 옵션 엔티티
 * 객관식/선택형 질문의 옵션 정보를 관리합니다.
 */
@Entity
@Table(name = "question_options")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QuestionOption {
    @Id
    @Column(name = "id", length = 36)
    private String id;

    @Column(name = "question_id", length = 36, nullable = false, insertable = false, updatable = false)
    private String questionId;

    @Column(name = "option_order", nullable = false)
    private Integer optionOrder;

    @Column(name = "option_text", nullable = false)
    private String optionText;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private SurveyQuestion question;

    @Builder
    public QuestionOption(String id, SurveyQuestion question, Integer optionOrder, String optionText) {
        this.id = id;
        this.question = question;
        this.questionId = question != null ? question.getId() : null;
        this.optionOrder = optionOrder;
        this.optionText = optionText;
    }
    
    /**
     * SurveyQuestion과의 양방향 관계를 설정합니다.
     * @param question 연결할 SurveyQuestion
     */
    public void setQuestion(SurveyQuestion question) {
        this.question = question;
        this.questionId = question != null ? question.getId() : null;
    }
}
