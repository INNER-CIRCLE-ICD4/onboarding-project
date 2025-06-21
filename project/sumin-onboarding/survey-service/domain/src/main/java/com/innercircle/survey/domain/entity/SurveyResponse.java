package com.innercircle.survey.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "survey_response")
@Getter
@NoArgsConstructor
public class SurveyResponse {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "survey_id", nullable = false)
    private Survey survey;

    //제출된 응답들(JSON)
    @Lob
    @Column(nullable = false)
    private String answersJson;

    //질문 스냅샷(JSON)
    @Lob
    @Column(nullable = false)
    private String snapshotJson;

    @Column(nullable = false, updatable = false)
    private LocalDateTime submittedAt;

    @PrePersist
    public void prePersist() {
        this.submittedAt = LocalDateTime.now();
    }

    public SurveyResponse(Survey survey, String answersJson, String snapshotJson) {
        this.survey = survey;
        this.answersJson = answersJson;
        this.snapshotJson = snapshotJson;
    }
}
