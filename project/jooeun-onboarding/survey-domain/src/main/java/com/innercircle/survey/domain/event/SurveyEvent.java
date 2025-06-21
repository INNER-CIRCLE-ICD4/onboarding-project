package com.innercircle.survey.domain.event;

import com.innercircle.survey.common.domain.BaseEntity;
import com.innercircle.survey.common.util.UlidGenerator;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 설문조사 관련 이벤트 추적 엔티티
 * 
 * 설문조사의 생성, 수정, 삭제 등의 이력을 추적합니다.
 * 향후 이벤트 소싱 패턴 적용 시 기반이 됩니다.
 */
@Entity
@Table(name = "survey_events", indexes = {
    @Index(name = "idx_event_survey_id", columnList = "survey_id"),
    @Index(name = "idx_event_type_time", columnList = "event_type, created_at"),
    @Index(name = "idx_event_survey_time", columnList = "survey_id, created_at")
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SurveyEvent extends BaseEntity {

    @Id
    @Column(name = "id", length = 26)
    private String id;

    @NotBlank(message = "설문조사 ID는 필수입니다.")
    @Column(name = "survey_id", nullable = false, length = 26)
    private String surveyId;

    @NotNull(message = "이벤트 타입은 필수입니다.")
    @Enumerated(EnumType.STRING)
    @Column(name = "event_type", nullable = false)
    private SurveyEventType eventType;

    @Column(name = "event_data", columnDefinition = "TEXT")
    private String eventData;

    @Column(name = "actor_info", length = 100)
    private String actorInfo;

    /**
     * 설문조사 이벤트 생성
     *
     * @param surveyId 설문조사 ID
     * @param eventType 이벤트 타입
     * @param eventData 이벤트 상세 데이터 (JSON)
     * @param actorInfo 이벤트 수행자 정보
     */
    public SurveyEvent(String surveyId, SurveyEventType eventType, String eventData, String actorInfo) {
        this.id = UlidGenerator.generate();
        this.surveyId = surveyId;
        this.eventType = eventType;
        this.eventData = eventData;
        this.actorInfo = actorInfo;
        initializeTimestamps();
    }

    /**
     * 설문조사 이벤트 ID를 통한 동등성 비교
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        SurveyEvent event = (SurveyEvent) obj;
        return id != null && id.equals(event.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
