package com.survey.core.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * 설문 엔티티 (버전별 관리)
 * 설문 그룹(SurveySeries)에 소속, 문항/응답과 1:N 구조지만 FK(Long)로만 연결
 */
@Entity
@Table(name = "survey")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Survey {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false)
    private Long seriesId;    // 설문 그룹 FK (SurveySeries.id)

    @Column(nullable = false)
    private int version = 1;  // 버전 (문항 변경 시 새 버전 생성)

    @Column(nullable = false)
    private String title;     // 설문 제목

    private String description;   // 설문 설명

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    private LocalDateTime startDate;    // 설문 시작일 (nullable)
    private LocalDateTime endDate;      // 설문 종료일 (nullable)

    private Boolean isOpen = true;      // 공개 여부 (true: 공개, false: 비공개)
}
