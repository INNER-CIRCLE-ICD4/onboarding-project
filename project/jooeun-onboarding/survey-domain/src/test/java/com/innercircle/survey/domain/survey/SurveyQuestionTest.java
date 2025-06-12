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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("SurveyQuestion 엔티티 테스트")
class SurveyQuestionTest {

    @Nested
    @DisplayName("텍스트형 질문 생성자 테스트")
    class TextQuestionConstructorTest {

        @Test
        @DisplayName("단답형 질문을 생성할 수 있다")
        void shouldCreateShortTextQuestion() {
            // given
            String title = "이름을 입력해주세요";
            String description = "실명을 입력해주세요";
            boolean required = true;

            // when
            SurveyQuestion question = new SurveyQuestion(title, description, QuestionType.SHORT_TEXT, required);

            // then
            assertThat(question.getId()).isNotNull().hasSize(26);
            assertThat(question.getTitle()).isEqualTo(title);
            assertThat(question.getDescription()).isEqualTo(description);
            assertThat(question.getQuestionType()).isEqualTo(QuestionType.SHORT_TEXT);
            assertThat(question.isRequired()).isEqualTo(required);
            assertThat(question.isActive()).isTrue();
            assertThat(question.getOptions()).isEmpty();
        }

        @Test
        @DisplayName("장문형 질문을 생성할 수 있다")
        void shouldCreateLongTextQuestion() {
            // given
            String title = "의견을 자세히 적어주세요";
            String description = "개선사항이나 건의사항을 자유롭게 작성해주세요";
            boolean required = false;

            // when
            SurveyQuestion question = new SurveyQuestion(title, description, QuestionType.LONG_TEXT, required);

            // then
            assertThat(question.getQuestionType()).isEqualTo(QuestionType.LONG_TEXT);
            assertThat(question.isRequired()).isFalse();
            assertThat(question.getOptions()).isEmpty();
        }

        @Test
        @DisplayName("설명이 null인 텍스트형 질문을 생성할 수 있다")
        void shouldCreateTextQuestionWithNullDescription() {
            // given
            String title = "질문";
            
            // when
            SurveyQuestion question = new SurveyQuestion(title, null, QuestionType.SHORT_TEXT, true);

            // then
            assertThat(question.getDescription()).isNull();
        }
    }

    @Nested
    @DisplayName("선택형 질문 생성자 테스트")
    class ChoiceQuestionConstructorTest {

        @Test
        @DisplayName("단일 선택 질문을 생성할 수 있다")
        void shouldCreateSingleChoiceQuestion() {
            // given
            String title = "선호하는 색상을 선택해주세요";
            String description = "하나만 선택해주세요";
            List<String> options = List.of("빨강", "파랑", "노랑", "초록");

            // when
            SurveyQuestion question = new SurveyQuestion(title, description, QuestionType.SINGLE_CHOICE, true, options);

            // then
            assertThat(question.getQuestionType()).isEqualTo(QuestionType.SINGLE_CHOICE);
            assertThat(question.getOptions()).containsExactlyElementsOf(options);
        }

        @Test
        @DisplayName("다중 선택 질문을 생성할 수 있다")
        void shouldCreateMultipleChoiceQuestion() {
            // given
            String title = "관심 있는 주제를 모두 선택해주세요";
            String description = "복수 선택 가능합니다";
            List<String> options = List.of("기술", "스포츠", "여행", "음식", "영화");

            // when
            SurveyQuestion question = new SurveyQuestion(title, description, QuestionType.MULTIPLE_CHOICE, false, options);

            // then
            assertThat(question.getQuestionType()).isEqualTo(QuestionType.MULTIPLE_CHOICE);
            assertThat(question.getOptions()).containsExactlyElementsOf(options);
            assertThat(question.isRequired()).isFalse();
        }

        @Test
        @DisplayName("선택형 질문에 옵션이 없으면 예외가 발생한다")
        void shouldThrowExceptionWhenChoiceQuestionHasNoOptions() {
            // given
            List<String> emptyOptions = Collections.emptyList();

            // when & then
            assertThatThrownBy(() -> new SurveyQuestion("제목", "설명", QuestionType.SINGLE_CHOICE, true, emptyOptions))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("선택형 질문에는 최소 1개의 옵션이 필요합니다.");
        }

        @Test
        @DisplayName("선택형 질문에 null 옵션이 있으면 예외가 발생한다")
        void shouldThrowExceptionWhenChoiceQuestionHasNullOption() {
            // given
            List<String> optionsWithNull = new ArrayList<>();
            optionsWithNull.add(null);
            optionsWithNull.add("옵션1");
            optionsWithNull.add("옵션2");

            // when & then
            assertThatThrownBy(() -> new SurveyQuestion("제목", "설명", QuestionType.SINGLE_CHOICE, true, optionsWithNull))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("선택 옵션은 빈 값일 수 없습니다.");
        }

        @Test
        @DisplayName("선택형 질문에 빈 문자열 옵션이 있으면 예외가 발생한다")
        void shouldThrowExceptionWhenChoiceQuestionHasEmptyOption() {
            // given
            List<String> optionsWithEmpty = List.of("옵션1", "", "옵션3");

            // when & then
            assertThatThrownBy(() -> new SurveyQuestion("제목", "설명", QuestionType.SINGLE_CHOICE, true, optionsWithEmpty))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("선택 옵션은 빈 값일 수 없습니다.");
        }

        @Test
        @DisplayName("선택형 질문에 공백만 있는 옵션이 있으면 예외가 발생한다")
        void shouldThrowExceptionWhenChoiceQuestionHasBlankOption() {
            // given
            List<String> optionsWithBlank = List.of("옵션1", "   ", "옵션3");

            // when & then
            assertThatThrownBy(() -> new SurveyQuestion("제목", "설명", QuestionType.SINGLE_CHOICE, true, optionsWithBlank))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("선택 옵션은 빈 값일 수 없습니다.");
        }

        @Test
        @DisplayName("선택형 질문의 옵션 수가 최대치를 초과하면 예외가 발생한다")
        void shouldThrowExceptionWhenTooManyOptions() {
            // given
            List<String> tooManyOptions = Stream.generate(() -> "옵션")
                .limit(SurveyConstants.Question.MAX_OPTIONS_COUNT + 1)
                .toList();

            // when & then
            assertThatThrownBy(() -> new SurveyQuestion("제목", "설명", QuestionType.SINGLE_CHOICE, true, tooManyOptions))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("선택 옵션은 최대 " + SurveyConstants.Question.MAX_OPTIONS_COUNT + "개까지 가능합니다.");
        }

        @Test
        @DisplayName("선택형 질문의 옵션이 너무 길면 예외가 발생한다")
        void shouldThrowExceptionWhenOptionTooLong() {
            // given
            String tooLongOption = "a".repeat(SurveyConstants.Question.MAX_OPTION_LENGTH + 1);
            List<String> options = List.of("옵션1", tooLongOption);

            // when & then
            assertThatThrownBy(() -> new SurveyQuestion("제목", "설명", QuestionType.SINGLE_CHOICE, true, options))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("선택 옵션은 " + SurveyConstants.Question.MAX_OPTION_LENGTH + "자 이하여야 합니다.");
        }

        @Test
        @DisplayName("최대 개수의 옵션으로 선택형 질문을 생성할 수 있다")
        void shouldCreateChoiceQuestionWithMaxOptions() {
            // given
            List<String> maxOptions = Stream.iterate(1, i -> i + 1)
                .limit(SurveyConstants.Question.MAX_OPTIONS_COUNT)
                .map(i -> "옵션" + i)
                .toList();

            // when
            SurveyQuestion question = new SurveyQuestion("제목", "설명", QuestionType.SINGLE_CHOICE, true, maxOptions);

            // then
            assertThat(question.getOptions()).hasSize(SurveyConstants.Question.MAX_OPTIONS_COUNT);
        }
    }

    @Nested
    @DisplayName("설문조사 할당 테스트")
    class AssignToSurveyTest {

        @Test
        @DisplayName("설문조사에 질문을 할당할 수 있다")
        void shouldAssignToSurvey() {
            // given
            Survey survey = new Survey("설문조사", "설명");
            SurveyQuestion question = new SurveyQuestion("질문", "설명", QuestionType.SHORT_TEXT, true);
            int order = 1;

            // when
            question.assignToSurvey(survey, order);

            // then
            assertThat(question.getSurvey()).isSameAs(survey);
            assertThat(question.getDisplayOrder()).isEqualTo(order);
        }
    }

    @Nested
    @DisplayName("질문 정보 수정 테스트")
    class UpdateInfoTest {

        @Test
        @DisplayName("질문 정보를 수정할 수 있다")
        void shouldUpdateQuestionInfo() {
            // given
            SurveyQuestion question = new SurveyQuestion("기존 제목", "기존 설명", QuestionType.SHORT_TEXT, true);
            String newTitle = "새로운 제목";
            String newDescription = "새로운 설명";
            boolean newRequired = false;

            // when
            question.updateInfo(newTitle, newDescription, newRequired);

            // then
            assertThat(question.getTitle()).isEqualTo(newTitle);
            assertThat(question.getDescription()).isEqualTo(newDescription);
            assertThat(question.isRequired()).isEqualTo(newRequired);
        }
    }

    @Nested
    @DisplayName("선택 옵션 수정 테스트")
    class UpdateOptionsTest {

        @Test
        @DisplayName("선택형 질문의 옵션을 수정할 수 있다")
        void shouldUpdateOptions() {
            // given
            SurveyQuestion question = new SurveyQuestion("질문", "설명", QuestionType.SINGLE_CHOICE, true, List.of("기존1", "기존2"));
            List<String> newOptions = List.of("새옵션1", "새옵션2", "새옵션3");

            // when
            question.updateOptions(newOptions);

            // then
            assertThat(question.getOptions()).containsExactlyElementsOf(newOptions);
        }

        @Test
        @DisplayName("텍스트형 질문에는 옵션을 설정할 수 없다")
        void shouldThrowExceptionWhenUpdateOptionsOnTextQuestion() {
            // given
            SurveyQuestion question = new SurveyQuestion("질문", "설명", QuestionType.SHORT_TEXT, true);
            List<String> options = List.of("옵션1", "옵션2");

            // when & then
            assertThatThrownBy(() -> question.updateOptions(options))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("텍스트 입력형 질문에는 선택 옵션을 설정할 수 없습니다.");
        }
    }

    @Nested
    @DisplayName("활성화/비활성화 테스트")
    class ActivationTest {

        @Test
        @DisplayName("질문을 비활성화할 수 있다")
        void shouldDeactivateQuestion() {
            // given
            SurveyQuestion question = new SurveyQuestion("질문", "설명", QuestionType.SHORT_TEXT, true);
            assertThat(question.isActive()).isTrue();

            // when
            question.deactivate();

            // then
            assertThat(question.isActive()).isFalse();
        }

        @Test
        @DisplayName("질문을 활성화할 수 있다")
        void shouldActivateQuestion() {
            // given
            SurveyQuestion question = new SurveyQuestion("질문", "설명", QuestionType.SHORT_TEXT, true);
            question.deactivate();
            assertThat(question.isActive()).isFalse();

            // when
            question.activate();

            // then
            assertThat(question.isActive()).isTrue();
        }
    }

    @Nested
    @DisplayName("응답 검증 테스트")
    class ValidateAnswerTest {

        @Test
        @DisplayName("필수 질문에 응답이 없으면 검증 실패")
        void shouldFailValidationWhenRequiredQuestionHasNoAnswer() {
            // given
            SurveyQuestion question = new SurveyQuestion("필수 질문", "설명", QuestionType.SHORT_TEXT, true);

            // when & then
            assertThat(question.isValidAnswer(null)).isFalse();
            assertThat(question.isValidAnswer(Collections.emptyList())).isFalse();
        }

        @Test
        @DisplayName("비필수 질문에 응답이 없으면 검증 성공")
        void shouldPassValidationWhenOptionalQuestionHasNoAnswer() {
            // given
            SurveyQuestion question = new SurveyQuestion("비필수 질문", "설명", QuestionType.SHORT_TEXT, false);

            // when & then
            assertThat(question.isValidAnswer(null)).isTrue();
            assertThat(question.isValidAnswer(Collections.emptyList())).isTrue();
        }

        @Nested
        @DisplayName("텍스트형 응답 검증")
        class TextAnswerValidationTest {

            @Test
            @DisplayName("단답형 질문에 유효한 응답")
            void shouldValidateShortTextAnswer() {
                // given
                SurveyQuestion question = new SurveyQuestion("단답형", "설명", QuestionType.SHORT_TEXT, true);

                // when & then
                assertThat(question.isValidAnswer(List.of("짧은 답변"))).isTrue();
                assertThat(question.isValidAnswer(List.of("a".repeat(SurveyConstants.Response.MAX_TEXT_ANSWER_LENGTH)))).isTrue();
            }

            @Test
            @DisplayName("단답형 질문에 너무 긴 응답은 검증 실패")
            void shouldFailValidationForTooLongShortTextAnswer() {
                // given
                SurveyQuestion question = new SurveyQuestion("단답형", "설명", QuestionType.SHORT_TEXT, true);
                String tooLongAnswer = "a".repeat(SurveyConstants.Response.MAX_TEXT_ANSWER_LENGTH + 1);

                // when & then
                assertThat(question.isValidAnswer(List.of(tooLongAnswer))).isFalse();
            }

            @Test
            @DisplayName("장문형 질문에 유효한 응답")
            void shouldValidateLongTextAnswer() {
                // given
                SurveyQuestion question = new SurveyQuestion("장문형", "설명", QuestionType.LONG_TEXT, true);

                // when & then
                assertThat(question.isValidAnswer(List.of("긴 답변"))).isTrue();
                assertThat(question.isValidAnswer(List.of("a".repeat(SurveyConstants.Response.MAX_LONG_TEXT_ANSWER_LENGTH)))).isTrue();
            }

            @Test
            @DisplayName("장문형 질문에 너무 긴 응답은 검증 실패")
            void shouldFailValidationForTooLongLongTextAnswer() {
                // given
                SurveyQuestion question = new SurveyQuestion("장문형", "설명", QuestionType.LONG_TEXT, true);
                String tooLongAnswer = "a".repeat(SurveyConstants.Response.MAX_LONG_TEXT_ANSWER_LENGTH + 1);

                // when & then
                assertThat(question.isValidAnswer(List.of(tooLongAnswer))).isFalse();
            }

            @Test
            @DisplayName("텍스트형 질문에 여러 응답이 있으면 검증 실패")
            void shouldFailValidationForMultipleTextAnswers() {
                // given
                SurveyQuestion question = new SurveyQuestion("단답형", "설명", QuestionType.SHORT_TEXT, true);

                // when & then
                assertThat(question.isValidAnswer(List.of("답변1", "답변2"))).isFalse();
            }
        }

        @Nested
        @DisplayName("선택형 응답 검증")
        class ChoiceAnswerValidationTest {

            @Test
            @DisplayName("단일 선택 질문에 유효한 응답")
            void shouldValidateSingleChoiceAnswer() {
                // given
                List<String> options = List.of("옵션1", "옵션2", "옵션3");
                SurveyQuestion question = new SurveyQuestion("단일선택", "설명", QuestionType.SINGLE_CHOICE, true, options);

                // when & then
                assertThat(question.isValidAnswer(List.of("옵션1"))).isTrue();
                assertThat(question.isValidAnswer(List.of("옵션2"))).isTrue();
                assertThat(question.isValidAnswer(List.of("옵션3"))).isTrue();
            }

            @Test
            @DisplayName("단일 선택 질문에 존재하지 않는 옵션은 검증 실패")
            void shouldFailValidationForInvalidSingleChoiceOption() {
                // given
                List<String> options = List.of("옵션1", "옵션2", "옵션3");
                SurveyQuestion question = new SurveyQuestion("단일선택", "설명", QuestionType.SINGLE_CHOICE, true, options);

                // when & then
                assertThat(question.isValidAnswer(List.of("없는옵션"))).isFalse();
            }

            @Test
            @DisplayName("단일 선택 질문에 여러 응답이 있으면 검증 실패")
            void shouldFailValidationForMultipleSingleChoiceAnswers() {
                // given
                List<String> options = List.of("옵션1", "옵션2", "옵션3");
                SurveyQuestion question = new SurveyQuestion("단일선택", "설명", QuestionType.SINGLE_CHOICE, true, options);

                // when & then
                assertThat(question.isValidAnswer(List.of("옵션1", "옵션2"))).isFalse();
            }

            @Test
            @DisplayName("다중 선택 질문에 유효한 응답")
            void shouldValidateMultipleChoiceAnswer() {
                // given
                List<String> options = List.of("옵션1", "옵션2", "옵션3");
                SurveyQuestion question = new SurveyQuestion("다중선택", "설명", QuestionType.MULTIPLE_CHOICE, true, options);

                // when & then
                assertThat(question.isValidAnswer(List.of("옵션1"))).isTrue();
                assertThat(question.isValidAnswer(List.of("옵션1", "옵션2"))).isTrue();
                assertThat(question.isValidAnswer(List.of("옵션1", "옵션2", "옵션3"))).isTrue();
            }

            @Test
            @DisplayName("다중 선택 질문에 존재하지 않는 옵션이 포함되면 검증 실패")
            void shouldFailValidationForInvalidMultipleChoiceOption() {
                // given
                List<String> options = List.of("옵션1", "옵션2", "옵션3");
                SurveyQuestion question = new SurveyQuestion("다중선택", "설명", QuestionType.MULTIPLE_CHOICE, true, options);

                // when & then
                assertThat(question.isValidAnswer(List.of("옵션1", "없는옵션"))).isFalse();
            }
        }
    }

    @Nested
    @DisplayName("동등성 및 해시코드 테스트")
    class EqualsAndHashCodeTest {

        @Test
        @DisplayName("같은 ID를 가진 질문은 동등하다")
        void shouldBeEqualWhenSameId() {
            // given
            SurveyQuestion question1 = new SurveyQuestion("제목1", "설명1", QuestionType.SHORT_TEXT, true);
            SurveyQuestion question2 = new SurveyQuestion("제목2", "설명2", QuestionType.LONG_TEXT, false);
            
            // ID를 같게 만들기 위해 리플렉션 사용
            try {
                var idField = SurveyQuestion.class.getDeclaredField("id");
                idField.setAccessible(true);
                String sameId = question1.getId();
                idField.set(question2, sameId);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            // when & then
            assertThat(question1).isEqualTo(question2);
            assertThat(question1.hashCode()).isEqualTo(question2.hashCode());
        }

        @Test
        @DisplayName("다른 ID를 가진 질문은 동등하지 않다")
        void shouldNotBeEqualWhenDifferentId() {
            // given
            SurveyQuestion question1 = new SurveyQuestion("제목", "설명", QuestionType.SHORT_TEXT, true);
            SurveyQuestion question2 = new SurveyQuestion("제목", "설명", QuestionType.SHORT_TEXT, true);

            // when & then
            assertThat(question1).isNotEqualTo(question2);
        }

        @Test
        @DisplayName("자기 자신과는 동등하다")
        void shouldBeEqualToItself() {
            // given
            SurveyQuestion question = new SurveyQuestion("제목", "설명", QuestionType.SHORT_TEXT, true);

            // when & then
            assertThat(question).isEqualTo(question);
        }

        @Test
        @DisplayName("null과는 동등하지 않다")
        void shouldNotBeEqualToNull() {
            // given
            SurveyQuestion question = new SurveyQuestion("제목", "설명", QuestionType.SHORT_TEXT, true);

            // when & then
            assertThat(question).isNotEqualTo(null);
        }

        @Test
        @DisplayName("다른 클래스와는 동등하지 않다")
        void shouldNotBeEqualToDifferentClass() {
            // given
            SurveyQuestion question = new SurveyQuestion("제목", "설명", QuestionType.SHORT_TEXT, true);
            String other = "other";

            // when & then
            assertThat(question).isNotEqualTo(other);
        }

        @Test
        @DisplayName("ID가 null인 경우 hashCode는 0이다")
        void shouldReturnZeroHashCodeWhenIdIsNull() {
            // given
            SurveyQuestion question = new SurveyQuestion("제목", "설명", QuestionType.SHORT_TEXT, true);
            
            // ID를 null로 만들기 위해 리플렉션 사용
            try {
                var idField = SurveyQuestion.class.getDeclaredField("id");
                idField.setAccessible(true);
                idField.set(question, null);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            // when & then
            assertThat(question.hashCode()).isEqualTo(0);
        }
    }

    static Stream<Arguments> provideQuestionTypesForTextValidation() {
        return Stream.of(
            Arguments.of(QuestionType.SHORT_TEXT, false),
            Arguments.of(QuestionType.LONG_TEXT, false),
            Arguments.of(QuestionType.SINGLE_CHOICE, true),
            Arguments.of(QuestionType.MULTIPLE_CHOICE, true)
        );
    }

    @ParameterizedTest
    @MethodSource("provideQuestionTypesForTextValidation") 
    @DisplayName("질문 타입에 따라 텍스트형/선택형이 올바르게 구분된다")
    void shouldDistinguishTextAndChoiceQuestions(QuestionType questionType, boolean isChoiceType) {
        // given
        List<String> options = isChoiceType ? List.of("옵션1", "옵션2") : null;
        
        // when
        SurveyQuestion question = isChoiceType 
            ? new SurveyQuestion("제목", "설명", questionType, true, options)
            : new SurveyQuestion("제목", "설명", questionType, true);

        // then
        assertThat(question.getQuestionType().isChoiceType()).isEqualTo(isChoiceType);
        assertThat(question.getQuestionType().isTextType()).isEqualTo(!isChoiceType);
    }
}
