package com.example.hyeongwononboarding.domain.survey.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 설문조사 엔티티
 * 설문 기본 정보, 버전, 생성/수정일시 등을 관리합니다.
 */
@Entity
@Table(name = "surveys")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Survey {
    @Id
    @Column(name = "id", length = 36)
    private String id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "current_version", nullable = false)
    private Integer currentVersion = 1;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "survey", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<SurveyVersion> versions = new ArrayList<>();

    @Builder
    public Survey(String id, String title, String description, LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        if (createdAt != null) {
            this.createdAt = createdAt;
        }
        this.currentVersion = 1;
    }

    public void updateCurrentVersion(int version) {
        this.currentVersion = version;
    }
    
    /**
     * 설문 제목과 설명을 업데이트합니다.
     *
     * @param title 새 제목
     * @param description 새 설명
     */
    public void update(String title, String description) {
        this.title = title;
        this.description = description;
    }
}
