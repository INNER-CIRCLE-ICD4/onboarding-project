package survey.survey.entity.surveyform;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import survey.common.snowflake.Snowflake;
import survey.survey.entity.surveyquestion.InputType;
import survey.survey.entity.surveyquestion.SurveyQuestion;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SurveyFormTest {
    Snowflake surveyFormId = new Snowflake();
    Snowflake questionId = new Snowflake();

    @DisplayName("SurveyForm 생성 시 모든 필드가 올바르게 설정된다")
    @Test
    void create_ShouldSetAllFieldsCorrectly() {
        // given
        SurveyFormId surveyFormId = SurveyFormId.create(this.surveyFormId.nextId());
        String title = "테스트 설문";
        String description = "테스트 설명";
        Long surveyId = 1L;
        LocalDateTime beforeCreate = LocalDateTime.now();

        // when
        SurveyForm surveyForm = SurveyForm.create(surveyFormId, title, description, surveyId);

        // then
        assertThat(surveyForm.getSurveyFormId()).isEqualTo(surveyFormId);
        assertThat(surveyForm.getTitle()).isEqualTo(title);
        assertThat(surveyForm.getDescription()).isEqualTo(description);
        assertThat(surveyForm.getSurveyId()).isEqualTo(surveyId);
        assertThat(surveyForm.getCreatedAt()).isAfter(beforeCreate);
        assertThat(surveyForm.getModifiedAt()).isEqualTo(surveyForm.getCreatedAt());
        assertThat(surveyForm.getSurveyQuestionList()).isEmpty();
    }

    @Test
    @DisplayName("질문 추가 시 양방향 연관관계가 올바르게 설정된다")
    void addQuestion_ShouldSetBidirectionalRelationship() {
        // given
        SurveyForm surveyForm = SurveyForm.create(
                SurveyFormId.create(this.surveyFormId.nextId()),
                "테스트 설문",
                "테스트 설명",
                1L
        );
        SurveyQuestion question = SurveyQuestion.create(
                questionId.nextId(),
                0,
                "테스트 질문",
                "테스트 질문 설명",
                InputType.SHORT_ANSWER,
                true
        );

        // when
        surveyForm.addQuestion(question);

        // then
        assertThat(surveyForm.getSurveyQuestionList()).hasSize(1);
        assertThat(surveyForm.getSurveyQuestionList()).contains(question);
        assertThat(question.getSurveyForm()).isEqualTo(surveyForm);



    }

    @DisplayName("여러 질문을 추가할 수 있다")
    @Test
    void addQuestion_ShouldAllowMultipleQuestions() {
        // given
        SurveyForm surveyForm = SurveyForm.create(
                SurveyFormId.create(this.surveyFormId.nextId()),
                "테스트 설문",
                "테스트 설명",
                1L
        );
        SurveyQuestion question1 = SurveyQuestion.create(
                questionId.nextId(),
                0,
                "테스트 질문1",
                "테스트 질문 설명1",
                InputType.SHORT_ANSWER,
                true
        );

        SurveyQuestion question2 = SurveyQuestion.create(
                questionId.nextId(),
                1,
                "테스트 질문2",
                "테스트 질문 설명2",
                InputType.LONG_ANSWER,
                false
        );

        // when
        surveyForm.addAllQuestions(List.of(question1, question2));

        // then
        assertThat(surveyForm.getSurveyQuestionList()).hasSize(2);
        assertThat(surveyForm.getSurveyQuestionList()).containsExactly(question1, question2);
    }

    @DisplayName("중복 질문 추가 시 무시된다")
    @Test
    void addQuestion_ShouldIgnoreDuplicates() {
        // given
        SurveyForm surveyForm = SurveyForm.create(
                SurveyFormId.create(this.surveyFormId.nextId()),
                "테스트 설문",
                "테스트 설명",
                1L
        );
        SurveyQuestion question = SurveyQuestion.create(
                questionId.nextId(),
                0,
                "테스트 질문",
                "테스트 질문 설명",
                InputType.SHORT_ANSWER,
                true
        );

        // when
        surveyForm.addQuestion(question);
        surveyForm.addQuestion(question); // 중복 추가

        // then
        assertThat(surveyForm.getSurveyQuestionList()).hasSize(1); // 여전히 1개
    }
}