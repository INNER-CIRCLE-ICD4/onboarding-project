package com.innercircle.survey.common.domain;

import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * 모든 엔티티의 기본 클래스
 * 공통 필드와 감사 기능을 제공합니다.
 */
@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Version
    @Column(name = "version")
    private Long version = 0L;

    /**
     * 엔티티 생성 시점 설정 (단위 테스트용)
     */
    protected void initializeTimestamps() {
        LocalDateTime now = LocalDateTime.now();
        if (this.createdAt == null) {
            this.createdAt = now;
        }
        if (this.updatedAt == null) {
            this.updatedAt = now;
        }
    }

    /**
     * 엔티티 생성 시점 설정
     */
    @PrePersist
    protected void onCreate() {
        initializeTimestamps();
    }

    /**
     * 엔티티 수정 시점 설정
     */
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    /**
     * 테스트 환경에서 버전 증가용 메서드
     * 실제 JPA 환경에서는 @Version이 자동으로 처리됨
     */
    protected void incrementVersionForTest() {
        if (this.version == null) {
            this.version = 1L;
        } else {
            this.version++;
        }
        onUpdate(); // 수정 시간도 업데이트
    }

    /**
     * 테스트 환경에서 초기 버전 설정용 메서드
     */
    protected void setInitialVersionForTest() {
        if (this.version == null || this.version == 0L) {
            this.version = 1L;
        }
    }
}
