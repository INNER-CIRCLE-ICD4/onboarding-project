package com.survey.core.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.*;

/**
 * 응답 그룹화, 메타 정보 보관
 * submitted, responderId 필터, 페이징 가능
 *
 * 설문 제출 시점, 사용자 정보
 *
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

    /** 어떤 버전의 Survey에 대한 응답인가? */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "survey_id", nullable = false)
    private Survey survey;

    @Column(nullable = false, updatable = false)
    private LocalDateTime submittedAt = LocalDateTime.now();

    /** 익명 또는 로그인 사용자 식별자 */
    @Column(nullable = false)
    private String responderId;

    @OneToMany(
            mappedBy = "response",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<SurveyResponseItem> answers = new ArrayList<>();

    public void addAnswer(SurveyResponseItem item) {
        item.setResponse(this);
        this.answers.add(item);
    }
}
