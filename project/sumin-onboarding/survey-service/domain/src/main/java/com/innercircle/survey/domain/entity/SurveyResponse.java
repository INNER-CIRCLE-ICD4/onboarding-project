package com.innercircle.survey.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
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

    @Column(nullable = false)
    private UUID surveyId;

    //제출된 응답들(JSON)
    @Lob
    @Column(nullable = false)
    private String answersJson;

    //당시 질문 스냅샷(JSON)
    @Lob
    @Column(nullable = false)
    private String snapshotJson;

    @Column(nullable = false, updatable = false)
    private LocalDateTime submittedAt;

    @PrePersist
    public void prePersist() {
        this.submittedAt = LocalDateTime.now();
    }

    public SurveyResponse(UUID surveyId, String answersJson, String snapshotJson) {
        this.surveyId = surveyId;
        this.answersJson = answersJson;
        this.snapshotJson = snapshotJson;
    }
}
