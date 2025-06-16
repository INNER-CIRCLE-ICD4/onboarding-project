package survey.survey.entity.surveyquestion;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import survey.common.snowflake.Snowflake;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SurveyQuestionTest {
    Snowflake questionId = new Snowflake();

    @DisplayName("SurveyQuestion 생성 시 모든 필드가 올바르게 설정된다")
    @Test
    void create_ShouldSetAllFieldsCorrectly() {
        // given
        Long id = questionId.nextId();
        Long surveyFormId = 1L;
        int questionIndex = 0;
        String name = "테스트 질문";
        String description = "테스트 질문 설명";
        InputType inputType = InputType.SHORT_ANSWER;
        boolean required = true;

        // when
        SurveyQuestion surveyQuestion = SurveyQuestion.create(
                surveyFormId, id, questionIndex, name, description, inputType, required
        );

        // then
        assertThat(surveyQuestion.getQuestionId()).isEqualTo(id);
        assertThat(surveyQuestion.getQuestionIndex()).isEqualTo(questionIndex);
        assertThat(surveyQuestion.getName()).isEqualTo(name);
        assertThat(surveyQuestion.getDescription()).isEqualTo(description);
        assertThat(surveyQuestion.getInputType()).isEqualTo(inputType);
        assertThat(surveyQuestion.isRequired()).isEqualTo(required);
        assertThat(surveyQuestion.getCandidates()).isEmpty();
        assertThat(surveyQuestion.getSurveyFormId()).isEqualTo(surveyFormId);
    }


    @DisplayName("후보 항목을 추가할 수 있다")
    @Test
    void addCandidate_ShouldAddCandidatesToList() {
        // given
        Long surveyFormId = 1L;
        Long id = questionId.nextId();
        SurveyQuestion surveyQuestion = SurveyQuestion.create(
                surveyFormId, id, 0, "객관식 질문", "설명", InputType.MULTIPLE_CHOICE, true
        );
        CheckCandidate candidate1 = CheckCandidate.of(0,"선택지 1");
        CheckCandidate candidate2 = CheckCandidate.of(1,"선택지 2");

        List<CheckCandidate> candidates = List.of(candidate1, candidate2);
        // when
        surveyQuestion.addCandidates(candidates);

        // then
        assertThat(surveyQuestion.getCandidates()).hasSize(2);
        assertThat(surveyQuestion.getCandidates()).containsExactly(candidate1, candidate2);
    }

    @DisplayName("다양한 InputType으로 생성할 수 있다")
    @Test
    void create_WithDifferentInputTypes_ShouldWork() {
        // given & when & then
        Long surveyFormId = 1L;
        for (InputType inputType : InputType.values()) {
            SurveyQuestion surveyQuestion = SurveyQuestion.create(
                    surveyFormId, 1L, 0, "질문", "설명", inputType, true
            );

            assertThat(surveyQuestion.getInputType()).isEqualTo(inputType);
        }
    }
}
