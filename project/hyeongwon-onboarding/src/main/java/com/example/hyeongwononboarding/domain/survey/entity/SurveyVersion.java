package com.example.hyeongwononboarding.domain.survey.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 설문조사 버전 엔티티
 * 설문조사의 각 버전별 정보와 질문 목록을 관리합니다.
 */
@Entity
@Table(name = "survey_versions", uniqueConstraints = @UniqueConstraint(name = "uk_survey_version", columnNames = {"survey_id", "version"}))
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SurveyVersion {
    @Id
    @Column(name = "id", length = 36)
    private String id;

    @Column(name = "survey_id", length = 36, nullable = false)
    private String surveyId;

    @Column(name = "version", nullable = false)
    private Integer version;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "survey_id", insertable = false, updatable = false)
    private Survey survey;

    @OneToMany(mappedBy = "surveyVersion", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<SurveyQuestion> questions = new ArrayList<>();

    @Builder
    public SurveyVersion(String id, String surveyId, Integer version, String title, String description, LocalDateTime createdAt) {
        this.id = id;
        this.surveyId = surveyId;
        this.version = version;
        this.title = title;
        this.description = description;
        this.createdAt = createdAt;
    }
}
