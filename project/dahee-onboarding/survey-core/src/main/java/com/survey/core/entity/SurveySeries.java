package com.survey.core.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

/**
 * 설문 그룹(시리즈) 엔티티
 * 여러 개의 설문을 논리적으로 묶는 상위 그룹
 * (예: 출퇴근조사, 복지만족도 등)
 */
@Entity
@Table(name = "survey_series")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SurveySeries {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, unique=true)
    private String code;         // 비즈니스 키 (예: commuteSurvey - 설문조사 이름code)

    @Column(nullable=false)
    private String name;         // 그룹 이름

    private String description;  // 상세 설명

    @Column(nullable=false, updatable=false)
    private LocalDateTime createdAt = LocalDateTime.now();
}
