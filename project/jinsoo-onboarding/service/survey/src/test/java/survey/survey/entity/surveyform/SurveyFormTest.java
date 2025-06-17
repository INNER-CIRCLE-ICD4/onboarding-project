package survey.survey.entity.surveyform;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import survey.common.snowflake.Snowflake;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class SurveyFormTest {
    Snowflake surveyFormId = new Snowflake();

    @DisplayName("SurveyForm 생성 시 모든 필드가 올바르게 설정된다")
    @Test
    void create_ShouldSetAllFieldsCorrectly() {
        // given
        Long id = surveyFormId.nextId();
        String title = "테스트 설문";
        String description = "테스트 설명";
        LocalDateTime beforeCreate = LocalDateTime.now();

        // when
        SurveyForm surveyForm = SurveyForm.create(id, title, description);

        // then
        assertThat(surveyForm.getSurveyFormId()).isEqualTo(id);
        assertThat(surveyForm.getTitle()).isEqualTo(title);
        assertThat(surveyForm.getDescription()).isEqualTo(description);
        assertThat(surveyForm.getCreatedAt()).isAfter(beforeCreate);
        assertThat(surveyForm.getModifiedAt()).isEqualTo(surveyForm.getCreatedAt());
    }

    @Test
    @DisplayName("SurveyForm을 업데이트할 수 있다")
    void update_ShouldUpdateFields() {
        // given
        Long id = surveyFormId.nextId();
        SurveyForm surveyForm = SurveyForm.create(
                id,
                "원래 제목",
                "원래 설명"
        );

        String newTitle = "새 제목";
        String newDescription = "새 설명";

        // when
        surveyForm.update(newTitle, newDescription);

        // then
        assertThat(surveyForm.getTitle()).isEqualTo(newTitle);
        assertThat(surveyForm.getDescription()).isEqualTo(newDescription);
    }

    @Test
    @DisplayName("버전을 증가시킬 수 있다")
    void incrementVersion_ShouldIncrementVersion() {
        // given
        Long id = surveyFormId.nextId();
        SurveyForm surveyForm = SurveyForm.create(
                id,
                "테스트 설문",
                "테스트 설명"
        );
        LocalDateTime initialModifiedAt = surveyForm.getModifiedAt();

        // when
        surveyForm.incrementVersion();

        // then
        assertThat(surveyForm.getVersion()).isEqualTo(1L);
        assertThat(surveyForm.getModifiedAt()).isAfter(initialModifiedAt);
    }
}
