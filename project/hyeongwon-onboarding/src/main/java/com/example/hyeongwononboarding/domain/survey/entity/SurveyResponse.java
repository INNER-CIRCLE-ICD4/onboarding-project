package com.example.hyeongwononboarding.domain.survey.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * 설문 응답 엔티티
 * - survey_responses 테이블 매핑
 * - 설문 응답의 메타 정보 저장
 */
@Entity
@Table(name = "survey_responses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SurveyResponse {
    @Id
    @Column(length = 36)
    private String id; // UUID 7

    @Column(nullable = false, length = 36)
    private String surveyId;

    @Column(nullable = false, length = 36)
    private String surveyVersionId;

    @Column(length = 255)
    private String respondentEmail; // 익명 응답 가능

    @Column(nullable = false)
    private LocalDateTime submittedAt;

    @OneToMany(mappedBy = "surveyResponse", cascade = CascadeType.ALL)
    private List<ResponseAnswer> answers;

    @PrePersist
    public void prePersist() {
        this.id = this.id == null ? UUID.randomUUID().toString() : this.id;
        this.submittedAt = LocalDateTime.now();
    }
}
