package com.innercircle.survey.domain.survey;

import com.innercircle.survey.common.constants.SurveyConstants;
import com.innercircle.survey.common.domain.QuestionType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Survey 엔티티 테스트")
class SurveyTest {

    @Nested
    @DisplayName("생성자 테스트")
    class ConstructorTest {

        @Test
        @DisplayName("정상적인 제목과 설명으로 설문조사를 생성할 수 있다")
        void shouldCreateSurveyWithValidTitleAndDescription() {
            // given
            String title = "고객 만족도 조사";
            String description = "서비스 개선을 위한 고객 만족도 조사입니다.";

            // when
            Survey survey = new Survey(title, description);

            // then
            assertThat(survey).isNotNull();
            assertThat(survey.getId()).isNotNull();
            assertThat(survey.getId()).hasSize(26); // ULID 길이
            assertThat(survey.getTitle()).isEqualTo(title);
            assertThat(survey.getDescription()).isEqualTo(description);
            assertThat(survey.getQuestions()).isEmpty();
            assertThat(survey.getCreatedAt()).isNotNull();
            assertThat(survey.getUpdatedAt()).isNotNull();
            assertThat(survey.getVersion()).isEqualTo(0L);
        }

        @Test
        @DisplayName("설명이 null인 경우에도 설문조사를 생성할 수 있다")
        void shouldCreateSurveyWithNullDescription() {
            // given
            String title = "설문조사";

            // when
            Survey survey = new Survey(title, null);

            // then
            assertThat(survey.getTitle()).isEqualTo(title);
            assertThat(survey.getDescription()).isNull();
        }

        @Test
        @DisplayName("설명이 빈 문자열인 경우에도 설문조사를 생성할 수 있다")
        void shouldCreateSurveyWithEmptyDescription() {
            // given
            String title = "설문조사";
            String description = "";

            // when
            Survey survey = new Survey(title, description);

            // then
            assertThat(survey.getTitle()).isEqualTo(title);
            assertThat(survey.getDescription()).isEqualTo(description);
        }
    }

    @Nested
    @DisplayName("설문조사 정보 수정 테스트")
    class UpdateInfoTest {

        @Test
        @DisplayName("제목과 설명을 수정할 수 있다")
        void shouldUpdateTitleAndDescription() {
            // given
            Survey survey = new Survey("기존 제목", "기존 설명");
            String newTitle = "새로운 제목";
            String newDescription = "새로운 설명";

            // when
            survey.updateInfo(newTitle, newDescription);

            // then
            assertThat(survey.getTitle()).isEqualTo(newTitle);
            assertThat(survey.getDescription()).isEqualTo(newDescription);
        }

        @Test
        @DisplayName("제목을 수정하고 설명을 null로 설정할 수 있다")
        void shouldUpdateTitleAndSetDescriptionToNull() {
            // given
            Survey survey = new Survey("기존 제목", "기존 설명");
            String newTitle = "새로운 제목";

            // when
            survey.updateInfo(newTitle, null);

            // then
            assertThat(survey.getTitle()).isEqualTo(newTitle);
            assertThat(survey.getDescription()).isNull();
        }
    }

    @Nested
    @DisplayName("설문 항목 추가 테스트")
    class AddQuestionTest {

        @Test
        @DisplayName("설문 항목을 추가할 수 있다")
        void shouldAddQuestion() {
            // given
            Survey survey = new Survey("설문조사", "설명");
            SurveyQuestion question = new SurveyQuestion("질문1", "설명1", QuestionType.SHORT_TEXT, true);

            // when
            survey.addQuestion(question);

            // then
            assertThat(survey.getQuestions()).hasSize(1);
            assertThat(survey.getQuestions().get(0)).isSameAs(question);
            assertThat(question.getSurvey()).isSameAs(survey);
            assertThat(question.getDisplayOrder()).isEqualTo(1);
        }

        @Test
        @DisplayName("여러 설문 항목을 순서대로 추가할 수 있다")
        void shouldAddMultipleQuestionsInOrder() {
            // given
            Survey survey = new Survey("설문조사", "설명");
            SurveyQuestion question1 = new SurveyQuestion("질문1", "설명1", QuestionType.SHORT_TEXT, true);
            SurveyQuestion question2 = new SurveyQuestion("질문2", "설명2", QuestionType.LONG_TEXT, false);
            SurveyQuestion question3 = new SurveyQuestion("질문3", "설명3", QuestionType.SINGLE_CHOICE, true, List.of("선택1", "선택2"));

            // when
            survey.addQuestion(question1);
            survey.addQuestion(question2);
            survey.addQuestion(question3);

            // then
            assertThat(survey.getQuestions()).hasSize(3);
            assertThat(question1.getDisplayOrder()).isEqualTo(1);
            assertThat(question2.getDisplayOrder()).isEqualTo(2);
            assertThat(question3.getDisplayOrder()).isEqualTo(3);
        }
    }

    @Nested
    @DisplayName("설문 항목 수정 테스트")
    class UpdateQuestionsTest {

        @Test
        @DisplayName("설문 항목들을 새로운 항목들로 교체할 수 있다")
        void shouldUpdateQuestions() {
            // given
            Survey survey = new Survey("설문조사", "설명");
            SurveyQuestion oldQuestion = new SurveyQuestion("기존 질문", "기존 설명", QuestionType.SHORT_TEXT, true);
            survey.addQuestion(oldQuestion);

            List<SurveyQuestion> newQuestions = List.of(
                new SurveyQuestion("새 질문1", "새 설명1", QuestionType.LONG_TEXT, false),
                new SurveyQuestion("새 질문2", "새 설명2", QuestionType.SINGLE_CHOICE, true, List.of("옵션1", "옵션2"))
            );

            // when
            survey.updateQuestions(newQuestions);

            // then
            assertThat(survey.getQuestions()).hasSize(3); // 기존 1개 + 새로운 2개
            assertThat(oldQuestion.isActive()).isFalse(); // 기존 질문 비활성화
            
            List<SurveyQuestion> activeQuestions = survey.getActiveQuestions();
            assertThat(activeQuestions).hasSize(2);
            assertThat(activeQuestions.get(0).getDisplayOrder()).isEqualTo(1);
            assertThat(activeQuestions.get(1).getDisplayOrder()).isEqualTo(2);
        }

        @ParameterizedTest
        @ValueSource(ints = {0, 11, 15, 20})
        @DisplayName("허용되지 않는 질문 개수로 수정하면 예외가 발생한다")
        void shouldThrowExceptionWhenInvalidQuestionCount(int count) {
            // given
            Survey survey = new Survey("설문조사", "설명");
            List<SurveyQuestion> questions = Stream.generate(() -> 
                new SurveyQuestion("질문", "설명", QuestionType.SHORT_TEXT, false))
                .limit(count)
                .toList();

            // when & then
            assertThatThrownBy(() -> survey.updateQuestions(questions))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(SurveyConstants.ErrorMessages.INVALID_QUESTION_COUNT);
        }

        @ParameterizedTest
        @ValueSource(ints = {1, 5, 10})
        @DisplayName("허용되는 질문 개수로는 정상적으로 수정된다")
        void shouldUpdateWithValidQuestionCount(int count) {
            // given
            Survey survey = new Survey("설문조사", "설명");
            List<SurveyQuestion> questions = Stream.generate(() -> 
                new SurveyQuestion("질문", "설명", QuestionType.SHORT_TEXT, false))
                .limit(count)
                .toList();

            // when
            survey.updateQuestions(questions);

            // then
            assertThat(survey.getActiveQuestions()).hasSize(count);
        }
    }

    @Nested
    @DisplayName("활성화된 설문 항목 조회 테스트")
    class GetActiveQuestionsTest {

        @Test
        @DisplayName("활성화된 설문 항목들만 조회할 수 있다")
        void shouldGetOnlyActiveQuestions() {
            // given
            Survey survey = new Survey("설문조사", "설명");
            SurveyQuestion activeQuestion1 = new SurveyQuestion("활성 질문1", "설명1", QuestionType.SHORT_TEXT, true);
            SurveyQuestion activeQuestion2 = new SurveyQuestion("활성 질문2", "설명2", QuestionType.LONG_TEXT, false);
            SurveyQuestion inactiveQuestion = new SurveyQuestion("비활성 질문", "설명3", QuestionType.SHORT_TEXT, true);
            
            survey.addQuestion(activeQuestion1);
            survey.addQuestion(activeQuestion2);
            survey.addQuestion(inactiveQuestion);
            
            inactiveQuestion.deactivate();

            // when
            List<SurveyQuestion> activeQuestions = survey.getActiveQuestions();

            // then
            assertThat(activeQuestions).hasSize(2);
            assertThat(activeQuestions).containsExactly(activeQuestion1, activeQuestion2);
            assertThat(activeQuestions).doesNotContain(inactiveQuestion);
        }

        @Test
        @DisplayName("활성화된 질문이 없으면 빈 리스트를 반환한다")
        void shouldReturnEmptyListWhenNoActiveQuestions() {
            // given
            Survey survey = new Survey("설문조사", "설명");
            SurveyQuestion inactiveQuestion = new SurveyQuestion("비활성 질문", "설명", QuestionType.SHORT_TEXT, true);
            survey.addQuestion(inactiveQuestion);
            inactiveQuestion.deactivate();

            // when
            List<SurveyQuestion> activeQuestions = survey.getActiveQuestions();

            // then
            assertThat(activeQuestions).isEmpty();
        }
    }

    @Nested
    @DisplayName("응답 가능 여부 확인 테스트")
    class IsAnswerableTest {

        @Test
        @DisplayName("활성화된 질문이 있으면 응답 가능하다")
        void shouldBeAnswerableWhenHasActiveQuestions() {
            // given
            Survey survey = new Survey("설문조사", "설명");
            SurveyQuestion question = new SurveyQuestion("질문", "설명", QuestionType.SHORT_TEXT, true);
            survey.addQuestion(question);

            // when & then
            assertThat(survey.isAnswerable()).isTrue();
        }

        @Test
        @DisplayName("활성화된 질문이 없으면 응답 불가능하다")
        void shouldNotBeAnswerableWhenNoActiveQuestions() {
            // given
            Survey survey = new Survey("설문조사", "설명");

            // when & then
            assertThat(survey.isAnswerable()).isFalse();
        }

        @Test
        @DisplayName("모든 질문이 비활성화되면 응답 불가능하다")
        void shouldNotBeAnswerableWhenAllQuestionsDeactivated() {
            // given
            Survey survey = new Survey("설문조사", "설명");
            SurveyQuestion question = new SurveyQuestion("질문", "설명", QuestionType.SHORT_TEXT, true);
            survey.addQuestion(question);
            question.deactivate();

            // when & then
            assertThat(survey.isAnswerable()).isFalse();
        }
    }

    @Nested
    @DisplayName("동등성 및 해시코드 테스트")
    class EqualsAndHashCodeTest {

        @Test
        @DisplayName("같은 ID를 가진 설문조사는 동등하다")
        void shouldBeEqualWhenSameId() {
            // given
            Survey survey1 = new Survey("제목1", "설명1");
            Survey survey2 = new Survey("제목2", "설명2");
            
            // ID를 같게 만들기 위해 리플렉션 사용
            try {
                var idField = Survey.class.getDeclaredField("id");
                idField.setAccessible(true);
                String sameId = survey1.getId();
                idField.set(survey2, sameId);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            // when & then
            assertThat(survey1).isEqualTo(survey2);
            assertThat(survey1.hashCode()).isEqualTo(survey2.hashCode());
        }

        @Test
        @DisplayName("다른 ID를 가진 설문조사는 동등하지 않다")
        void shouldNotBeEqualWhenDifferentId() {
            // given
            Survey survey1 = new Survey("제목", "설명");
            Survey survey2 = new Survey("제목", "설명");

            // when & then
            assertThat(survey1).isNotEqualTo(survey2);
        }

        @Test
        @DisplayName("자기 자신과는 동등하다")
        void shouldBeEqualToItself() {
            // given
            Survey survey = new Survey("제목", "설명");

            // when & then
            assertThat(survey).isEqualTo(survey);
        }

        @Test
        @DisplayName("null과는 동등하지 않다")
        void shouldNotBeEqualToNull() {
            // given
            Survey survey = new Survey("제목", "설명");

            // when & then
            assertThat(survey).isNotEqualTo(null);
        }

        @Test
        @DisplayName("다른 클래스와는 동등하지 않다")
        void shouldNotBeEqualToDifferentClass() {
            // given
            Survey survey = new Survey("제목", "설명");
            String other = "other";

            // when & then
            assertThat(survey).isNotEqualTo(other);
        }

        @Test
        @DisplayName("ID가 null인 경우 hashCode는 0이다")
        void shouldReturnZeroHashCodeWhenIdIsNull() {
            // given
            Survey survey = new Survey("제목", "설명");
            
            // ID를 null로 만들기 위해 리플렉션 사용
            try {
                var idField = Survey.class.getDeclaredField("id");
                idField.setAccessible(true);
                idField.set(survey, null);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            // when & then
            assertThat(survey.hashCode()).isEqualTo(0);
        }
    }

    static Stream<Arguments> provideTitleAndDescription() {
        return Stream.of(
            Arguments.of("제목", "설명"),
            Arguments.of("매우 긴 제목".repeat(10), "매우 긴 설명".repeat(50)),
            Arguments.of("A", ""),
            Arguments.of("특수문자!@#$%^&*()_+", "줄바꿈\n포함\t탭문자")
        );
    }

    @ParameterizedTest
    @MethodSource("provideTitleAndDescription")
    @DisplayName("다양한 제목과 설명으로 설문조사를 생성할 수 있다")
    void shouldCreateSurveyWithVariousTitleAndDescription(String title, String description) {
        // when
        Survey survey = new Survey(title, description);

        // then
        assertThat(survey.getTitle()).isEqualTo(title);
        assertThat(survey.getDescription()).isEqualTo(description);
    }
}
