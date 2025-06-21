package com.innercircle.survey.domain.summary;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;

import java.time.LocalDateTime;

/**
 * 설문조사 응답 통계 요약 (읽기 전용 뷰)
 * 
 * 성능 최적화를 위한 집계 데이터를 제공합니다.
 * 실제 운영에서는 별도의 배치 작업으로 생성하거나
 * 데이터베이스 뷰를 활용하는 것을 권장합니다.
 */
@Entity
@Immutable
@Subselect("""
    SELECT 
        s.id as survey_id,
        s.title as survey_title,
        COUNT(DISTINCT sr.id) as total_responses,
        COUNT(DISTINCT sa.id) as total_answers,
        MIN(sr.created_at) as first_response_at,
        MAX(sr.created_at) as last_response_at,
        s.version as survey_version
    FROM surveys s
    LEFT JOIN survey_responses sr ON s.id = sr.survey_id
    LEFT JOIN survey_answers sa ON sr.id = sa.response_id
    GROUP BY s.id, s.title, s.version
    """)
@Table(name = "survey_response_summary")
@Getter
@NoArgsConstructor
public class SurveyResponseSummary {

    @Id
    @Column(name = "survey_id", length = 26)
    private String surveyId;

    @Column(name = "survey_title")
    private String surveyTitle;

    @Column(name = "total_responses")
    private Long totalResponses;

    @Column(name = "total_answers")
    private Long totalAnswers;

    @Column(name = "first_response_at")
    private LocalDateTime firstResponseAt;

    @Column(name = "last_response_at")
    private LocalDateTime lastResponseAt;

    @Column(name = "survey_version")
    private Long surveyVersion;

    /**
     * 응답율 계산 (응답한 항목 수 / 전체 질문 수)
     * 
     * @param totalQuestions 전체 질문 수
     * @return 응답율 (0.0 ~ 1.0)
     */
    public double calculateResponseRate(int totalQuestions) {
        if (totalQuestions == 0 || totalResponses == 0) {
            return 0.0;
        }
        
        double expectedAnswers = (double) totalQuestions * totalResponses;
        return Math.min(1.0, totalAnswers / expectedAnswers);
    }

    /**
     * 활성 상태 확인 (최근 응답이 있는지)
     * 
     * @param daysBefore 기준 일수
     * @return 활성 상태 여부
     */
    public boolean isActiveWithin(int daysBefore) {
        if (lastResponseAt == null) {
            return false;
        }
        
        return lastResponseAt.isAfter(LocalDateTime.now().minusDays(daysBefore));
    }

    /**
     * 설문조사 ID를 통한 동등성 비교
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        SurveyResponseSummary summary = (SurveyResponseSummary) obj;
        return surveyId != null && surveyId.equals(summary.surveyId);
    }

    @Override
    public int hashCode() {
        return surveyId != null ? surveyId.hashCode() : 0;
    }
}
