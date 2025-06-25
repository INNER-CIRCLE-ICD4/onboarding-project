package com.survey.core.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * 문항별 응답 엔티티
 * 하나의 응답(SurveyResponse)에서 각 문항(SurveyItem)에 대한 답변을 별도 저장
 */
@Entity
@Table(name = "survey_response_item")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SurveyResponseItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long responseId;     // 응답 FK (SurveyResponse.id)

    @Column(nullable = false)
    private Long surveyItemId;   // 문항 FK (SurveyItem.id)

    @Column(nullable = false)
    private String questionText; // 응답 시점의 질문 스냅샷 (변경 이력 대비)

    @Column(nullable = false)
    private String answer;       // 실제 답변(텍스트/옵션)
}

