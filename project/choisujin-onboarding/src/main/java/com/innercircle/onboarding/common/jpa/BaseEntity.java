package com.innercircle.onboarding.common.jpa;

import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@DynamicUpdate
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
public abstract class BaseEntity {

    @Comment("생성일시")
    @CreatedDate
    @Column(name = "created_at", updatable = false, nullable = false, columnDefinition = "DATETIME(3)")
    private LocalDateTime createdAt;

    @Comment("수정일시")
    @LastModifiedDate
    @Column(name = "modified_at", nullable = false, columnDefinition = "DATETIME(3)")
    private LocalDateTime modifiedAt;

}
