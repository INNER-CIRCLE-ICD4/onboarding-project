package com.example.hyeongwononboarding.domain.survey.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * 설문 응답 상세 엔티티
 * - response_answers 테이블 매핑
 * - 각 문항별 응답 정보 저장
 */
@Entity
@Table(name = "response_answers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseAnswer {
    @Id
    @Column(length = 36)
    private String id; // UUID 7

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "response_id", nullable = false)
    private SurveyResponse surveyResponse;

    @Column(nullable = false, length = 36)
    private String questionId;

    @Column(columnDefinition = "TEXT")
    private String answerText;

    @Column(columnDefinition = "TEXT")
    private String selectedOptionIds; // JSON 배열 문자열로 저장
}
