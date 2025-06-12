package com.survey.core.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

/**
 * 설문 조사 테이블
 */
@Entity
@Table(name = "survey_series")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class SurveySeries {
    @Id @GeneratedValue
    private Long id;

    @Column(nullable=false, unique=true)
    private String code;          // 비즈니스 키 (“commuteSurvey”)

    @Column(nullable=false)
    private String name;          // 사용자에게 보여줄 이름 (“출퇴근조사”)

    private String description;   // 상세 설명

    @Column(nullable=false, updatable=false)
    private LocalDateTime createdAt = LocalDateTime.now();

}

