package com.survey.core.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 개별 응답: 어떤 문항에 답변했는지 FK 추적,
 * 질문 텍스트 복제(questionText), 실제 답변(answer)
 */
@Entity
@Table(name = "survey_response_item")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SurveyResponseItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 어떤 SurveyResponse 소속인지 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "response_id", nullable = false)
    @Setter
    private SurveyResponse response;

    /** 원본 SurveyItem의 아이디(버전 N) */
    @Column(nullable = false)
    private Long surveyItemId;

    /** 질문 텍스트 스냅샷 */
    @Column(nullable = false)
    private String questionText;

    /** 사용자 답변(단답 or 선택지 값) */
    @Column(nullable = false)
    private String answer;
}

