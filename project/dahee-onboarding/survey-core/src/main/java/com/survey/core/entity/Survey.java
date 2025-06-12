package com.survey.core.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.LAZY;

/**
 *  설문 기본 정보 및 버전 관리
 *  설문 생성/수정에서 제목,설명 빼고 과거 설문과 새로운 설문을 분리하게 위해 code + version 조합
 */
@Entity
@Table(name = "survey")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Survey {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**설문 집합(그룹) 식별자*/
    @ManyToOne(fetch=LAZY)
    @JoinColumn(name="series_id", nullable=false)
    private SurveySeries series;

    /**수정할 때마다 version++*/
    @Column(nullable = false)
    private int version = 1;

    /**설문 조사 타이틀*/
    @Column(nullable = false)
    private String title;

    /**설문 상세 설명*/
    private String description;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    @OneToMany(
            mappedBy = "survey",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @OrderColumn(name = "question_order")
    private List<SurveyItem> items = new ArrayList<>();

    // Survey version 복제
    public Survey createNextVersion(String newTitle, String newDesc, List<SurveyItem> newItems) {
        Survey copy = new Survey();
        copy.series = this.series;
        copy.version = this.version + 1;
        copy.title = newTitle;
        copy.description = newDesc;
        newItems.forEach(item -> item.setSurvey(copy));
        copy.items = newItems;
        return copy;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
