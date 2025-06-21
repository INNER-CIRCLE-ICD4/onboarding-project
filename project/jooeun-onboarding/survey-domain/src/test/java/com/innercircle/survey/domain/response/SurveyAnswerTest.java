package com.innercircle.survey.domain.response;

import com.innercircle.survey.common.domain.QuestionType;
import com.innercircle.survey.domain.survey.Survey;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("SurveyAnswer 엔티티 테스트")
class SurveyAnswerTest {

    @Nested
    @DisplayName("생성자 테스트")
    class ConstructorTest {

        @Test
        @DisplayName("단답형 답변을 생성할 수 있다")
        void shouldCreateShortTextAnswer() {
            // given
            String questionId = "Q001";
            String questionTitle = "이름을 입력해주세요";
            QuestionType questionType = QuestionType.SHORT_TEXT;
            List<String> answerValues = List.of("홍길동");

            // when
            SurveyAnswer answer = new SurveyAnswer(questionId, questionTitle, questionType, answerValues);

            // then
            assertThat(answer.getId()).isNotNull().hasSize(26); // ULID 길이
            assertThat(answer.getQuestionId()).isEqualTo(questionId);
            assertThat(answer.getQuestionTitle()).isEqualTo(questionTitle);
            assertThat(answer.getQuestionType()).isEqualTo(questionType);
            assertThat(answer.getAnswerValues()).containsExactlyElementsOf(answerValues);
            assertThat(answer.getCreatedAt()).isNotNull();
            assertThat(answer.getUpdatedAt()).isNotNull();
            assertThat(answer.getVersion()).isEqualTo(0L);
        }

        @Test
        @DisplayName("장문형 답변을 생성할 수 있다")
        void shouldCreateLongTextAnswer() {
            // given
            String questionId = "Q002";
            String questionTitle = "의견을 자세히 작성해주세요";
            QuestionType questionType = QuestionType.LONG_TEXT;
            List<String> answerValues = List.of("서비스가 전반적으로 만족스럽습니다. 다만 모바일 버전의 UI 개선이 필요할 것 같습니다.");

            // when
            SurveyAnswer answer = new SurveyAnswer(questionId, questionTitle, questionType, answerValues);

            // then
            assertThat(answer.getQuestionType()).isEqualTo(QuestionType.LONG_TEXT);
            assertThat(answer.getSingleAnswer()).isEqualTo(answerValues.get(0));
        }

        @Test
        @DisplayName("단일 선택 답변을 생성할 수 있다")
        void shouldCreateSingleChoiceAnswer() {
            // given
            String questionId = "Q003";
            String questionTitle = "선호하는 색상을 선택해주세요";
            QuestionType questionType = QuestionType.SINGLE_CHOICE;
            List<String> answerValues = List.of("파랑");

            // when
            SurveyAnswer answer = new SurveyAnswer(questionId, questionTitle, questionType, answerValues);

            // then
            assertThat(answer.getQuestionType()).isEqualTo(QuestionType.SINGLE_CHOICE);
            assertThat(answer.getSingleAnswer()).isEqualTo("파랑");
            assertThat(answer.getAllAnswers()).containsExactly("파랑");
        }

        @Test
        @DisplayName("다중 선택 답변을 생성할 수 있다")
        void shouldCreateMultipleChoiceAnswer() {
            // given
            String questionId = "Q004";
            String questionTitle = "관심 있는 주제를 모두 선택해주세요";
            QuestionType questionType = QuestionType.MULTIPLE_CHOICE;
            List<String> answerValues = List.of("기술", "스포츠", "여행");

            // when
            SurveyAnswer answer = new SurveyAnswer(questionId, questionTitle, questionType, answerValues);

            // then
            assertThat(answer.getQuestionType()).isEqualTo(QuestionType.MULTIPLE_CHOICE);
            assertThat(answer.getAllAnswers()).containsExactlyElementsOf(answerValues);
        }

        @Test
        @DisplayName("응답 값이 null인 경우 빈 리스트로 초기화된다")
        void shouldInitializeEmptyListWhenAnswerValuesIsNull() {
            // given
            String questionId = "Q001";
            String questionTitle = "선택사항";
            QuestionType questionType = QuestionType.SHORT_TEXT;

            // when
            SurveyAnswer answer = new SurveyAnswer(questionId, questionTitle, questionType, null);

            // then
            assertThat(answer.getAnswerValues()).isEmpty();
            assertThat(answer.isEmpty()).isTrue();
        }

        @Test
        @DisplayName("빈 응답 값들은 제거된다")
        void shouldRemoveEmptyAnswerValues() {
            // given
            String questionId = "Q001";
            String questionTitle = "질문";
            QuestionType questionType = QuestionType.MULTIPLE_CHOICE;
            List<String> answerValues = new ArrayList<>();
            answerValues.add("옵션1");
            answerValues.add("");
            answerValues.add("옵션2");
            answerValues.add("   ");
            answerValues.add("옵션3");

            // when
            SurveyAnswer answer = new SurveyAnswer(questionId, questionTitle, questionType, answerValues);

            // then
            assertThat(answer.getAllAnswers()).containsExactly("옵션1", "옵션2", "옵션3");
        }

        @Test
        @DisplayName("단일 응답 질문에 여러 답변이 있으면 예외가 발생한다")
        void shouldThrowExceptionWhenMultipleAnswersForSingleResponse() {
            // given
            String questionId = "Q001";
            String questionTitle = "단답형 질문";
            QuestionType questionType = QuestionType.SHORT_TEXT;
            List<String> multipleAnswers = List.of("답변1", "답변2");

            // when & then
            assertThatThrownBy(() -> new SurveyAnswer(questionId, questionTitle, questionType, multipleAnswers))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("단일 응답 질문에는 하나의 답변만 허용됩니다.");
        }

        @ParameterizedTest
        @MethodSource("provideSingleResponseQuestionTypes")
        @DisplayName("단일 응답 질문 타입들에 여러 답변이 있으면 예외가 발생한다")
        void shouldThrowExceptionForMultipleAnswersOnSingleResponseTypes(QuestionType questionType) {
            // given
            List<String> multipleAnswers = List.of("답변1", "답변2");

            // when & then
            assertThatThrownBy(() -> new SurveyAnswer("Q001", "질문", questionType, multipleAnswers))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("단일 응답 질문에는 하나의 답변만 허용됩니다.");
        }

        static Stream<QuestionType> provideSingleResponseQuestionTypes() {
            return Stream.of(
                QuestionType.SHORT_TEXT,
                QuestionType.LONG_TEXT,
                QuestionType.SINGLE_CHOICE
            );
        }
    }

    @Nested
    @DisplayName("설문조사 응답 할당 테스트")
    class AssignToResponseTest {

        @Test
        @DisplayName("설문조사 응답에 답변을 할당할 수 있다")
        void shouldAssignToSurveyResponse() {
            // given
            Survey survey = new Survey("설문조사", "설명");
            SurveyResponse surveyResponse = new SurveyResponse(survey, "응답자");
            SurveyAnswer answer = new SurveyAnswer("Q001", "질문", QuestionType.SHORT_TEXT, List.of("답변"));

            // when
            answer.assignToResponse(surveyResponse);

            // then
            assertThat(answer.getSurveyResponse()).isSameAs(surveyResponse);
        }
    }

    @Nested
    @DisplayName("답변 값 조회 테스트")
    class GetAnswerValuesTest {

        @Test
        @DisplayName("단일 답변 값을 조회할 수 있다")
        void shouldGetSingleAnswer() {
            // given
            SurveyAnswer answer = new SurveyAnswer("Q001", "질문", QuestionType.SHORT_TEXT, List.of("단일답변"));

            // when
            String singleAnswer = answer.getSingleAnswer();

            // then
            assertThat(singleAnswer).isEqualTo("단일답변");
        }

        @Test
        @DisplayName("답변이 없으면 단일 답변은 null을 반환한다")
        void shouldReturnNullForSingleAnswerWhenEmpty() {
            // given
            SurveyAnswer answer = new SurveyAnswer("Q001", "질문", QuestionType.SHORT_TEXT, Collections.emptyList());

            // when
            String singleAnswer = answer.getSingleAnswer();

            // then
            assertThat(singleAnswer).isNull();
        }

        @Test
        @DisplayName("모든 답변 값을 조회할 수 있다")
        void shouldGetAllAnswers() {
            // given
            List<String> originalAnswers = List.of("답변1", "답변2", "답변3");
            SurveyAnswer answer = new SurveyAnswer("Q001", "질문", QuestionType.MULTIPLE_CHOICE, originalAnswers);

            // when
            List<String> allAnswers = answer.getAllAnswers();

            // then
            assertThat(allAnswers).containsExactlyElementsOf(originalAnswers);
            assertThat(allAnswers).isNotSameAs(answer.getAnswerValues()); // 복사본 반환 확인
        }

        @Test
        @DisplayName("반환된 답변 리스트를 수정해도 원본에 영향을 주지 않는다")
        void shouldReturnImmutableCopyOfAnswers() {
            // given
            SurveyAnswer answer = new SurveyAnswer("Q001", "질문", QuestionType.MULTIPLE_CHOICE, List.of("답변1", "답변2"));
            List<String> allAnswers = answer.getAllAnswers();

            // when
            allAnswers.add("새로운답변");

            // then
            assertThat(answer.getAllAnswers()).hasSize(2);
            assertThat(answer.getAllAnswers()).doesNotContain("새로운답변");
        }
    }

    @Nested
    @DisplayName("답변 타입 확인 테스트")
    class AnswerTypeCheckTest {

        @Test
        @DisplayName("텍스트 형태 응답인지 확인할 수 있다")
        void shouldIdentifyTextAnswer() {
            // given
            SurveyAnswer shortTextAnswer = new SurveyAnswer("Q001", "질문", QuestionType.SHORT_TEXT, List.of("답변"));
            SurveyAnswer longTextAnswer = new SurveyAnswer("Q002", "질문", QuestionType.LONG_TEXT, List.of("답변"));
            SurveyAnswer singleChoiceAnswer = new SurveyAnswer("Q003", "질문", QuestionType.SINGLE_CHOICE, List.of("선택"));

            // when & then
            assertThat(shortTextAnswer.isTextAnswer()).isTrue();
            assertThat(longTextAnswer.isTextAnswer()).isTrue();
            assertThat(singleChoiceAnswer.isTextAnswer()).isFalse();
        }

        @Test
        @DisplayName("선택형 응답인지 확인할 수 있다")
        void shouldIdentifyChoiceAnswer() {
            // given
            SurveyAnswer shortTextAnswer = new SurveyAnswer("Q001", "질문", QuestionType.SHORT_TEXT, List.of("답변"));
            SurveyAnswer singleChoiceAnswer = new SurveyAnswer("Q002", "질문", QuestionType.SINGLE_CHOICE, List.of("선택"));
            SurveyAnswer multipleChoiceAnswer = new SurveyAnswer("Q003", "질문", QuestionType.MULTIPLE_CHOICE, List.of("선택1", "선택2"));

            // when & then
            assertThat(shortTextAnswer.isChoiceAnswer()).isFalse();
            assertThat(singleChoiceAnswer.isChoiceAnswer()).isTrue();
            assertThat(multipleChoiceAnswer.isChoiceAnswer()).isTrue();
        }

        @Test
        @DisplayName("단일 선택 응답인지 확인할 수 있다")
        void shouldIdentifySingleChoice() {
            // given
            SurveyAnswer singleChoiceAnswer = new SurveyAnswer("Q001", "질문", QuestionType.SINGLE_CHOICE, List.of("선택"));
            SurveyAnswer multipleChoiceAnswer = new SurveyAnswer("Q002", "질문", QuestionType.MULTIPLE_CHOICE, List.of("선택"));

            // when & then
            assertThat(singleChoiceAnswer.isSingleChoice()).isTrue();
            assertThat(multipleChoiceAnswer.isSingleChoice()).isFalse();
        }

        @Test
        @DisplayName("다중 선택 응답인지 확인할 수 있다")
        void shouldIdentifyMultipleChoice() {
            // given
            SurveyAnswer singleChoiceAnswer = new SurveyAnswer("Q001", "질문", QuestionType.SINGLE_CHOICE, List.of("선택"));
            SurveyAnswer multipleChoiceAnswer = new SurveyAnswer("Q002", "질문", QuestionType.MULTIPLE_CHOICE, List.of("선택"));

            // when & then
            assertThat(singleChoiceAnswer.isMultipleChoice()).isFalse();
            assertThat(multipleChoiceAnswer.isMultipleChoice()).isTrue();
        }
    }

    @Nested
    @DisplayName("빈 응답 확인 테스트")
    class IsEmptyTest {

        @Test
        @DisplayName("답변이 없으면 빈 응답이다")
        void shouldBeEmptyWhenNoAnswers() {
            // given
            SurveyAnswer answer = new SurveyAnswer("Q001", "질문", QuestionType.SHORT_TEXT, Collections.emptyList());

            // when & then
            assertThat(answer.isEmpty()).isTrue();
        }

        @Test
        @DisplayName("null 값만 있으면 빈 응답이다")
        void shouldBeEmptyWhenOnlyNullValues() {
            // given
            List<String> nullList = new ArrayList<>();
            nullList.add(null);
            SurveyAnswer answer = new SurveyAnswer("Q001", "질문", QuestionType.SHORT_TEXT, nullList);

            // when & then
            assertThat(answer.isEmpty()).isTrue();
        }

        @Test
        @DisplayName("공백만 있으면 빈 응답이다")
        void shouldBeEmptyWhenOnlyBlankValues() {
            // given
            SurveyAnswer answer = new SurveyAnswer("Q001", "질문", QuestionType.MULTIPLE_CHOICE, List.of("", "   ", "\t", "\n"));

            // when & then
            assertThat(answer.isEmpty()).isTrue();
        }

        @Test
        @DisplayName("유효한 답변이 있으면 빈 응답이 아니다")
        void shouldNotBeEmptyWhenHasValidAnswers() {
            // given
            SurveyAnswer answer = new SurveyAnswer("Q001", "질문", QuestionType.SHORT_TEXT, List.of("유효한답변"));

            // when & then
            assertThat(answer.isEmpty()).isFalse();
        }

        @Test
        @DisplayName("유효한 답변과 빈 값이 섞여있으면 빈 응답이 아니다")
        void shouldNotBeEmptyWhenHasMixedValues() {
            // given
            SurveyAnswer answer = new SurveyAnswer("Q001", "질문", QuestionType.MULTIPLE_CHOICE, List.of("", "유효한답변", "   "));

            // when & then
            assertThat(answer.isEmpty()).isFalse();
        }
    }

    @Nested
    @DisplayName("답변 값 검색 테스트")
    class ContainsValueTest {

        @Test
        @DisplayName("특정 값이 포함되어 있는지 확인할 수 있다")
        void shouldContainValue() {
            // given
            SurveyAnswer answer = new SurveyAnswer("Q001", "질문", QuestionType.MULTIPLE_CHOICE, List.of("빨강", "파랑", "노랑"));

            // when & then
            assertThat(answer.containsValue("빨강")).isTrue();
            assertThat(answer.containsValue("파랑")).isTrue();
            assertThat(answer.containsValue("노랑")).isTrue();
            assertThat(answer.containsValue("초록")).isFalse();
        }

        @Test
        @DisplayName("대소문자 구분 없이 검색할 수 있다")
        void shouldContainValueCaseInsensitive() {
            // given
            SurveyAnswer answer = new SurveyAnswer("Q001", "질문", QuestionType.SHORT_TEXT, List.of("Hello World"));

            // when & then
            assertThat(answer.containsValue("hello")).isTrue();
            assertThat(answer.containsValue("WORLD")).isTrue();
            assertThat(answer.containsValue("Hello")).isTrue();
            assertThat(answer.containsValue("world")).isTrue();
        }

        @Test
        @DisplayName("부분 문자열로 검색할 수 있다")
        void shouldContainPartialValue() {
            // given
            SurveyAnswer answer = new SurveyAnswer("Q001", "질문", QuestionType.SHORT_TEXT, List.of("안녕하세요"));

            // when & then
            assertThat(answer.containsValue("안녕")).isTrue();
            assertThat(answer.containsValue("하세요")).isTrue();
            assertThat(answer.containsValue("녕하")).isTrue();
            assertThat(answer.containsValue("감사")).isFalse();
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {"", "   ", "\t", "\n"})
        @DisplayName("null이나 빈 검색어는 false를 반환한다")
        void shouldReturnFalseForNullOrEmptySearchValue(String searchValue) {
            // given
            SurveyAnswer answer = new SurveyAnswer("Q001", "질문", QuestionType.SHORT_TEXT, List.of("답변"));

            // when & then
            assertThat(answer.containsValue(searchValue)).isFalse();
        }
    }

    @Nested
    @DisplayName("질문 제목 검색 테스트")
    class QuestionTitleContainsTest {

        @Test
        @DisplayName("질문 제목에 특정 값이 포함되어 있는지 확인할 수 있다")
        void shouldContainInQuestionTitle() {
            // given
            SurveyAnswer answer = new SurveyAnswer("Q001", "이름을 입력해주세요", QuestionType.SHORT_TEXT, List.of("홍길동"));

            // when & then
            assertThat(answer.questionTitleContains("이름")).isTrue();
            assertThat(answer.questionTitleContains("입력")).isTrue();
            assertThat(answer.questionTitleContains("주세요")).isTrue();
            assertThat(answer.questionTitleContains("나이")).isFalse();
        }

        @Test
        @DisplayName("질문 제목에서 대소문자 구분 없이 검색할 수 있다")
        void shouldContainInQuestionTitleCaseInsensitive() {
            // given
            SurveyAnswer answer = new SurveyAnswer("Q001", "Please Enter Your Name", QuestionType.SHORT_TEXT, List.of("John"));

            // when & then
            assertThat(answer.questionTitleContains("please")).isTrue();
            assertThat(answer.questionTitleContains("ENTER")).isTrue();
            assertThat(answer.questionTitleContains("name")).isTrue();
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {"", "   "})
        @DisplayName("null이나 빈 검색어는 false를 반환한다")
        void shouldReturnFalseForNullOrEmptySearchInQuestionTitle(String searchValue) {
            // given
            SurveyAnswer answer = new SurveyAnswer("Q001", "질문", QuestionType.SHORT_TEXT, List.of("답변"));

            // when & then
            assertThat(answer.questionTitleContains(searchValue)).isFalse();
        }

        @Test
        @DisplayName("질문 제목이 null인 경우 false를 반환한다")
        void shouldReturnFalseWhenQuestionTitleIsNull() {
            // given
            SurveyAnswer answer = new SurveyAnswer("Q001", null, QuestionType.SHORT_TEXT, List.of("답변"));

            // when & then
            assertThat(answer.questionTitleContains("검색어")).isFalse();
        }
    }

    @Nested
    @DisplayName("답변 요약 테스트")
    class GetAnswerSummaryTest {

        @Test
        @DisplayName("빈 응답의 요약을 조회할 수 있다")
        void shouldGetSummaryForEmptyAnswer() {
            // given
            SurveyAnswer answer = new SurveyAnswer("Q001", "질문", QuestionType.SHORT_TEXT, Collections.emptyList());

            // when
            String summary = answer.getAnswerSummary();

            // then
            assertThat(summary).isEqualTo("(응답 없음)");
        }

        @Test
        @DisplayName("단답형 응답의 요약을 조회할 수 있다")
        void shouldGetSummaryForShortTextAnswer() {
            // given
            SurveyAnswer answer = new SurveyAnswer("Q001", "질문", QuestionType.SHORT_TEXT, List.of("홍길동"));

            // when
            String summary = answer.getAnswerSummary();

            // then
            assertThat(summary).isEqualTo("홍길동");
        }

        @Test
        @DisplayName("장문형 응답의 요약을 조회할 수 있다")
        void shouldGetSummaryForLongTextAnswer() {
            // given
            SurveyAnswer answer = new SurveyAnswer("Q001", "질문", QuestionType.LONG_TEXT, List.of("긴 텍스트 답변입니다."));

            // when
            String summary = answer.getAnswerSummary();

            // then
            assertThat(summary).isEqualTo("긴 텍스트 답변입니다.");
        }

        @Test
        @DisplayName("단일 선택 응답의 요약을 조회할 수 있다")
        void shouldGetSummaryForSingleChoiceAnswer() {
            // given
            SurveyAnswer answer = new SurveyAnswer("Q001", "질문", QuestionType.SINGLE_CHOICE, List.of("파랑"));

            // when
            String summary = answer.getAnswerSummary();

            // then
            assertThat(summary).isEqualTo("파랑");
        }

        @Test
        @DisplayName("다중 선택 응답의 요약을 조회할 수 있다")
        void shouldGetSummaryForMultipleChoiceAnswer() {
            // given
            SurveyAnswer answer = new SurveyAnswer("Q001", "질문", QuestionType.MULTIPLE_CHOICE, List.of("빨강", "파랑", "노랑"));

            // when
            String summary = answer.getAnswerSummary();

            // then
            assertThat(summary).isEqualTo("빨강, 파랑, 노랑");
        }
    }

    @Nested
    @DisplayName("toString 테스트")
    class ToStringTest {

        @Test
        @DisplayName("toString이 올바른 형식을 반환한다")
        void shouldReturnCorrectToStringFormat() {
            // given
            SurveyAnswer answer = new SurveyAnswer("Q001", "이름을 입력해주세요", QuestionType.SHORT_TEXT, List.of("홍길동"));

            // when
            String toString = answer.toString();

            // then
            assertThat(toString).contains("SurveyAnswer{");
            assertThat(toString).contains("id='" + answer.getId() + "'");
            assertThat(toString).contains("questionId='Q001'");
            assertThat(toString).contains("questionTitle='이름을 입력해주세요'");
            assertThat(toString).contains("questionType=SHORT_TEXT");
            assertThat(toString).contains("answerSummary='홍길동'");
        }
    }

    @Nested
    @DisplayName("동등성 및 해시코드 테스트")
    class EqualsAndHashCodeTest {

        @Test
        @DisplayName("같은 ID를 가진 답변은 동등하다")
        void shouldBeEqualWhenSameId() {
            // given
            SurveyAnswer answer1 = new SurveyAnswer("Q001", "질문1", QuestionType.SHORT_TEXT, List.of("답변1"));
            SurveyAnswer answer2 = new SurveyAnswer("Q002", "질문2", QuestionType.LONG_TEXT, List.of("답변2"));
            
            // ID를 같게 만들기 위해 리플렉션 사용
            try {
                var idField = SurveyAnswer.class.getDeclaredField("id");
                idField.setAccessible(true);
                String sameId = answer1.getId();
                idField.set(answer2, sameId);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            // when & then
            assertThat(answer1).isEqualTo(answer2);
            assertThat(answer1.hashCode()).isEqualTo(answer2.hashCode());
        }

        @Test
        @DisplayName("다른 ID를 가진 답변은 동등하지 않다")
        void shouldNotBeEqualWhenDifferentId() {
            // given
            SurveyAnswer answer1 = new SurveyAnswer("Q001", "질문", QuestionType.SHORT_TEXT, List.of("답변"));
            SurveyAnswer answer2 = new SurveyAnswer("Q001", "질문", QuestionType.SHORT_TEXT, List.of("답변"));

            // when & then
            assertThat(answer1).isNotEqualTo(answer2);
        }

        @Test
        @DisplayName("자기 자신과는 동등하다")
        void shouldBeEqualToItself() {
            // given
            SurveyAnswer answer = new SurveyAnswer("Q001", "질문", QuestionType.SHORT_TEXT, List.of("답변"));

            // when & then
            assertThat(answer).isEqualTo(answer);
        }

        @Test
        @DisplayName("null과는 동등하지 않다")
        void shouldNotBeEqualToNull() {
            // given
            SurveyAnswer answer = new SurveyAnswer("Q001", "질문", QuestionType.SHORT_TEXT, List.of("답변"));

            // when & then
            assertThat(answer).isNotEqualTo(null);
        }

        @Test
        @DisplayName("다른 클래스와는 동등하지 않다")
        void shouldNotBeEqualToDifferentClass() {
            // given
            SurveyAnswer answer = new SurveyAnswer("Q001", "질문", QuestionType.SHORT_TEXT, List.of("답변"));
            String other = "other";

            // when & then
            assertThat(answer).isNotEqualTo(other);
        }

        @Test
        @DisplayName("ID가 null인 경우 hashCode는 0이다")
        void shouldReturnZeroHashCodeWhenIdIsNull() {
            // given
            SurveyAnswer answer = new SurveyAnswer("Q001", "질문", QuestionType.SHORT_TEXT, List.of("답변"));
            
            // ID를 null로 만들기 위해 리플렉션 사용
            try {
                var idField = SurveyAnswer.class.getDeclaredField("id");
                idField.setAccessible(true);
                idField.set(answer, null);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            // when & then
            assertThat(answer.hashCode()).isEqualTo(0);
        }
    }

    @Nested
    @DisplayName("실제 시나리오 테스트")
    class RealScenarioTest {

        @Test
        @DisplayName("검색 기능을 사용하는 시나리오")
        void shouldHandleSearchScenario() {
            // given - 다양한 답변들 생성
            SurveyAnswer nameAnswer = new SurveyAnswer("Q001", "성함을 입력해주세요", QuestionType.SHORT_TEXT, List.of("김철수"));
            SurveyAnswer ageAnswer = new SurveyAnswer("Q002", "연령대를 선택해주세요", QuestionType.SINGLE_CHOICE, List.of("30대"));
            SurveyAnswer hobbyAnswer = new SurveyAnswer("Q003", "취미를 모두 선택해주세요", QuestionType.MULTIPLE_CHOICE, List.of("독서", "영화감상", "스포츠"));
            SurveyAnswer feedbackAnswer = new SurveyAnswer("Q004", "서비스 개선 의견", QuestionType.LONG_TEXT, List.of("UI가 직관적이고 사용하기 편리합니다"));

            // when & then - 답변 내용 검색
            assertThat(nameAnswer.containsValue("김철")).isTrue();
            assertThat(nameAnswer.containsValue("이영")).isFalse();
            
            assertThat(ageAnswer.containsValue("30")).isTrue();
            assertThat(ageAnswer.containsValue("20")).isFalse();
            
            assertThat(hobbyAnswer.containsValue("독서")).isTrue();
            assertThat(hobbyAnswer.containsValue("영화")).isTrue();
            assertThat(hobbyAnswer.containsValue("음악")).isFalse();
            
            assertThat(feedbackAnswer.containsValue("직관적")).isTrue();
            assertThat(feedbackAnswer.containsValue("불편")).isFalse();

            // when & then - 질문 제목 검색
            assertThat(nameAnswer.questionTitleContains("성함")).isTrue();
            assertThat(ageAnswer.questionTitleContains("연령")).isTrue();
            assertThat(hobbyAnswer.questionTitleContains("취미")).isTrue();
            assertThat(feedbackAnswer.questionTitleContains("개선")).isTrue();
            assertThat(nameAnswer.questionTitleContains("나이")).isFalse();
        }

        @Test
        @DisplayName("답변 요약을 활용하는 시나리오")
        void shouldHandleAnswerSummaryScenario() {
            // given
            SurveyAnswer emptyAnswer = new SurveyAnswer("Q001", "선택사항", QuestionType.SHORT_TEXT, Collections.emptyList());
            SurveyAnswer textAnswer = new SurveyAnswer("Q002", "의견", QuestionType.LONG_TEXT, List.of("전반적으로 만족합니다"));
            SurveyAnswer singleChoiceAnswer = new SurveyAnswer("Q003", "만족도", QuestionType.SINGLE_CHOICE, List.of("매우 만족"));
            SurveyAnswer multipleChoiceAnswer = new SurveyAnswer("Q004", "개선사항", QuestionType.MULTIPLE_CHOICE, List.of("UI", "성능", "기능"));

            // when & then
            assertThat(emptyAnswer.getAnswerSummary()).isEqualTo("(응답 없음)");
            assertThat(textAnswer.getAnswerSummary()).isEqualTo("전반적으로 만족합니다");
            assertThat(singleChoiceAnswer.getAnswerSummary()).isEqualTo("매우 만족");
            assertThat(multipleChoiceAnswer.getAnswerSummary()).isEqualTo("UI, 성능, 기능");
        }
    }

    static Stream<Arguments> provideQuestionTypesAndExpectedMethods() {
        return Stream.of(
            Arguments.of(QuestionType.SHORT_TEXT, true, false, false, false),
            Arguments.of(QuestionType.LONG_TEXT, true, false, false, false),
            Arguments.of(QuestionType.SINGLE_CHOICE, false, true, true, false),
            Arguments.of(QuestionType.MULTIPLE_CHOICE, false, true, false, true)
        );
    }

    @ParameterizedTest
    @MethodSource("provideQuestionTypesAndExpectedMethods")
    @DisplayName("질문 타입에 따라 올바른 타입 체크 메소드 결과를 반환한다")
    void shouldReturnCorrectTypeCheckResults(QuestionType questionType, boolean isText, boolean isChoice, boolean isSingle, boolean isMultiple) {
        // given
        SurveyAnswer answer = new SurveyAnswer("Q001", "질문", questionType, List.of("답변"));

        // when & then
        assertThat(answer.isTextAnswer()).isEqualTo(isText);
        assertThat(answer.isChoiceAnswer()).isEqualTo(isChoice);
        assertThat(answer.isSingleChoice()).isEqualTo(isSingle);
        assertThat(answer.isMultipleChoice()).isEqualTo(isMultiple);
    }
}
