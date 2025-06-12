package com.innercircle.survey.domain.event;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 설문조사 이벤트 엔티티 테스트
 */
@DisplayName("설문조사 이벤트 테스트")
class SurveyEventTest {

    @Test
    @DisplayName("설문조사 이벤트 생성 시 모든 필드가 올바르게 설정되는지 확인")
    void 설문조사_이벤트_생성_테스트() {
        // Given
        String surveyId = "test-survey-id";
        SurveyEventType eventType = SurveyEventType.SURVEY_CREATED;
        String eventData = "{\"title\": \"새 설문조사\", \"questionCount\": 3}";
        String actorInfo = "admin@example.com";

        // When
        SurveyEvent event = new SurveyEvent(surveyId, eventType, eventData, actorInfo);

        // Then
        assertThat(event.getId()).isNotNull();
        assertThat(event.getSurveyId()).isEqualTo(surveyId);
        assertThat(event.getEventType()).isEqualTo(eventType);
        assertThat(event.getEventData()).isEqualTo(eventData);
        assertThat(event.getActorInfo()).isEqualTo(actorInfo);
        assertThat(event.getCreatedAt()).isNotNull();
    }

    @Test
    @DisplayName("이벤트 타입별 분류 기능 테스트")
    void 이벤트_타입별_분류_기능_테스트() {
        // 구조 변경 이벤트들
        assertThat(SurveyEventType.QUESTION_ADDED.isStructuralChange()).isTrue();
        assertThat(SurveyEventType.QUESTION_UPDATED.isStructuralChange()).isTrue();
        assertThat(SurveyEventType.QUESTION_DEACTIVATED.isStructuralChange()).isTrue();
        assertThat(SurveyEventType.QUESTIONS_RESTRUCTURED.isStructuralChange()).isTrue();

        // 비구조 변경 이벤트들
        assertThat(SurveyEventType.SURVEY_CREATED.isStructuralChange()).isFalse();
        assertThat(SurveyEventType.SURVEY_INFO_UPDATED.isStructuralChange()).isFalse();
        assertThat(SurveyEventType.RESPONSE_SUBMITTED.isStructuralChange()).isFalse();

        // 응답 관련 이벤트
        assertThat(SurveyEventType.RESPONSE_SUBMITTED.isResponseRelated()).isTrue();
        assertThat(SurveyEventType.SURVEY_CREATED.isResponseRelated()).isFalse();
    }
}
