package com.survey.core.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.*;

/**
 * 설문 응답 엔티티
 * 익명 응답자(UUID 기준) 1회 응답 정보, 설문 버전과 연결
 */
@Entity
@Table(name = "survey_response")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SurveyResponse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long surveyId;   // 설문 FK (Survey.id, 버전별 응답 구분)

    @Column(nullable = false, updatable = false)
    private String uuid;     // 익명 응답자 UUID (쿠키/LocalStorage 기반)

    @Column(nullable = false, updatable = false)
    private LocalDateTime submittedAt = LocalDateTime.now();
}
