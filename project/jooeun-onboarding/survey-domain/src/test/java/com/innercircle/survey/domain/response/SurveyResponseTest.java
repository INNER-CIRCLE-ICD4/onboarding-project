package com.innercircle.survey.domain.response;

import com.innercircle.survey.common.domain.QuestionType;
import com.innercircle.survey.domain.survey.Survey;
import com.innercircle.survey.domain.survey.SurveyQuestion;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("SurveyResponse 엔티티 테스트")
class SurveyResponseTest {

    @Nested
    @DisplayName("생성자 테스트")
    class ConstructorTest {

        @Test
        @DisplayName("설문조사와 응답자 정보로 응답을 생성할 수 있다")
        void shouldCreateResponseWithSurveyAndRespondentInfo() {
            // given
            Survey survey = new Survey("고객 만족도 조사", "서비스 개선을 위한 조사입니다.");
            String respondentInfo = "김철수 (customer@example.com)";

            // when
            SurveyResponse response = new SurveyResponse(survey, respondentInfo);

            // then
            assertThat(response.getId()).isNotNull().hasSize(26); // ULID 길이
            assertThat(response.getSurvey()).isSameAs(survey);
            assertThat(response.getRespondentInfo()).isEqualTo(respondentInfo);
            assertThat(response.getAnswers()).isEmpty();
            assertThat(response.getCreatedAt()).isNotNull();
            assertThat(response.getUpdatedAt()).isNotNull();
            assertThat(response.getVersion()).isEqualTo(0L);
        }

        @Test
        @DisplayName("응답자 정보 없이 응답을 생성할 수 있다")
        void shouldCreateResponseWithoutRespondentInfo() {
            // given
            Survey survey = new Survey("익명 설문조사", "익명으로 참여 가능한 조사입니다.");

            // when
            SurveyResponse response = new SurveyResponse(survey, null);

            // then
            assertThat(response.getSurvey()).isSameAs(survey);
            assertThat(response.getRespondentInfo()).isNull();
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {"", "   ", "익명", "홍길동", "user@example.com"})
        @DisplayName("다양한 응답자 정보로 응답을 생성할 수 있다")
        void shouldCreateResponseWithVariousRespondentInfo(String respondentInfo) {
            // given
            Survey survey = new Survey("설문조사", "설명");

            // when
            SurveyResponse response = new SurveyResponse(survey, respondentInfo);

            // then
            assertThat(response.getRespondentInfo()).isEqualTo(respondentInfo);
        }
    }

    @Nested
    @DisplayName("답변 추가 테스트")
    class AddAnswerTest {

        @Test
        @DisplayName("개별 항목 응답을 추가할 수 있다")
        void shouldAddAnswer() {
            // given
            Survey survey = new Survey("설문조사", "설명");
            SurveyResponse response = new SurveyResponse(survey, "응답자");
            SurveyAnswer answer = new SurveyAnswer("Q001", "이름을 입력해주세요", QuestionType.SHORT_TEXT, List.of("홍길동"));

            // when
            response.addAnswer(answer);

            // then
            assertThat(response.getAnswers()).hasSize(1);
            assertThat(response.getAnswers().get(0)).isSameAs(answer);
            assertThat(answer.getSurveyResponse()).isSameAs(response);
        }

        @Test
        @DisplayName("여러 개의 답변을 추가할 수 있다")
        void shouldAddMultipleAnswers() {
            // given
            Survey survey = new Survey("설문조사", "설명");
            SurveyResponse response = new SurveyResponse(survey, "응답자");
            
            SurveyAnswer answer1 = new SurveyAnswer("Q001", "이름", QuestionType.SHORT_TEXT, List.of("홍길동"));
            SurveyAnswer answer2 = new SurveyAnswer("Q002", "나이", QuestionType.SHORT_TEXT, List.of("30"));
            SurveyAnswer answer3 = new SurveyAnswer("Q003", "선호색상", QuestionType.SINGLE_CHOICE, List.of("파랑"));

            // when
            response.addAnswer(answer1);
            response.addAnswer(answer2);
            response.addAnswer(answer3);

            // then
            assertThat(response.getAnswers()).hasSize(3);
            assertThat(response.getAnswers()).containsExactly(answer1, answer2, answer3);
            
            // 모든 답변이 올바르게 할당되었는지 확인
            assertThat(answer1.getSurveyResponse()).isSameAs(response);
            assertThat(answer2.getSurveyResponse()).isSameAs(response);
            assertThat(answer3.getSurveyResponse()).isSameAs(response);
        }
    }

    @Nested
    @DisplayName("특정 질문 답변 조회 테스트")
    class GetAnswerByQuestionIdTest {

        @Test
        @DisplayName("특정 질문의 답변을 조회할 수 있다")
        void shouldGetAnswerByQuestionId() {
            // given
            Survey survey = new Survey("설문조사", "설명");
            SurveyResponse response = new SurveyResponse(survey, "응답자");
            
            String questionId = "Q001";
            SurveyAnswer answer = new SurveyAnswer(questionId, "이름", QuestionType.SHORT_TEXT, List.of("홍길동"));
            response.addAnswer(answer);

            // when
            SurveyAnswer foundAnswer = response.getAnswerByQuestionId(questionId);

            // then
            assertThat(foundAnswer).isSameAs(answer);
        }

        @Test
        @DisplayName("존재하지 않는 질문 ID로 조회하면 null을 반환한다")
        void shouldReturnNullWhenQuestionIdNotFound() {
            // given
            Survey survey = new Survey("설문조사", "설명");
            SurveyResponse response = new SurveyResponse(survey, "응답자");
            
            SurveyAnswer answer = new SurveyAnswer("Q001", "이름", QuestionType.SHORT_TEXT, List.of("홍길동"));
            response.addAnswer(answer);

            // when
            SurveyAnswer foundAnswer = response.getAnswerByQuestionId("Q999");

            // then
            assertThat(foundAnswer).isNull();
        }

        @Test
        @DisplayName("여러 답변 중에서 특정 질문의 답변을 조회할 수 있다")
        void shouldGetSpecificAnswerAmongMultiple() {
            // given
            Survey survey = new Survey("설문조사", "설명");
            SurveyResponse response = new SurveyResponse(survey, "응답자");
            
            SurveyAnswer answer1 = new SurveyAnswer("Q001", "이름", QuestionType.SHORT_TEXT, List.of("홍길동"));
            SurveyAnswer answer2 = new SurveyAnswer("Q002", "나이", QuestionType.SHORT_TEXT, List.of("30"));
            SurveyAnswer answer3 = new SurveyAnswer("Q003", "선호색상", QuestionType.SINGLE_CHOICE, List.of("파랑"));
            
            response.addAnswer(answer1);
            response.addAnswer(answer2);
            response.addAnswer(answer3);

            // when
            SurveyAnswer foundAnswer = response.getAnswerByQuestionId("Q002");

            // then
            assertThat(foundAnswer).isSameAs(answer2);
        }
    }

    @Nested
    @DisplayName("응답 완료 확인 테스트")
    class IsCompletedTest {

        @Test
        @DisplayName("답변이 있으면 완료된 응답이다")
        void shouldBeCompletedWhenHasAnswers() {
            // given
            Survey survey = new Survey("설문조사", "설명");
            SurveyResponse response = new SurveyResponse(survey, "응답자");
            SurveyAnswer answer = new SurveyAnswer("Q001", "이름", QuestionType.SHORT_TEXT, List.of("홍길동"));
            response.addAnswer(answer);

            // when & then
            assertThat(response.isCompleted()).isTrue();
        }

        @Test
        @DisplayName("답변이 없으면 완료되지 않은 응답이다")
        void shouldNotBeCompletedWhenNoAnswers() {
            // given
            Survey survey = new Survey("설문조사", "설명");
            SurveyResponse response = new SurveyResponse(survey, "응답자");

            // when & then
            assertThat(response.isCompleted()).isFalse();
        }

        @Test
        @DisplayName("여러 답변이 있으면 완료된 응답이다")
        void shouldBeCompletedWhenHasMultipleAnswers() {
            // given
            Survey survey = new Survey("설문조사", "설명");
            SurveyResponse response = new SurveyResponse(survey, "응답자");
            
            response.addAnswer(new SurveyAnswer("Q001", "이름", QuestionType.SHORT_TEXT, List.of("홍길동")));
            response.addAnswer(new SurveyAnswer("Q002", "나이", QuestionType.SHORT_TEXT, List.of("30")));

            // when & then
            assertThat(response.isCompleted()).isTrue();
        }
    }

    @Nested
    @DisplayName("응답 통계 테스트")
    class GetAnsweredQuestionCountTest {

        @Test
        @DisplayName("응답한 질문 수를 조회할 수 있다")
        void shouldGetAnsweredQuestionCount() {
            // given
            Survey survey = new Survey("설문조사", "설명");
            SurveyResponse response = new SurveyResponse(survey, "응답자");

            // when & then
            assertThat(response.getAnsweredQuestionCount()).isEqualTo(0);

            // given - 답변 추가
            response.addAnswer(new SurveyAnswer("Q001", "이름", QuestionType.SHORT_TEXT, List.of("홍길동")));
            
            // when & then
            assertThat(response.getAnsweredQuestionCount()).isEqualTo(1);

            // given - 답변 추가
            response.addAnswer(new SurveyAnswer("Q002", "나이", QuestionType.SHORT_TEXT, List.of("30")));
            response.addAnswer(new SurveyAnswer("Q003", "선호색상", QuestionType.SINGLE_CHOICE, List.of("파랑")));
            
            // when & then
            assertThat(response.getAnsweredQuestionCount()).isEqualTo(3);
        }
    }

    @Nested
    @DisplayName("동등성 및 해시코드 테스트")
    class EqualsAndHashCodeTest {

        @Test
        @DisplayName("같은 ID를 가진 응답은 동등하다")
        void shouldBeEqualWhenSameId() {
            // given
            Survey survey1 = new Survey("설문1", "설명1");
            Survey survey2 = new Survey("설문2", "설명2");
            SurveyResponse response1 = new SurveyResponse(survey1, "응답자1");
            SurveyResponse response2 = new SurveyResponse(survey2, "응답자2");
            
            // ID를 같게 만들기 위해 리플렉션 사용
            try {
                var idField = SurveyResponse.class.getDeclaredField("id");
                idField.setAccessible(true);
                String sameId = response1.getId();
                idField.set(response2, sameId);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            // when & then
            assertThat(response1).isEqualTo(response2);
            assertThat(response1.hashCode()).isEqualTo(response2.hashCode());
        }

        @Test
        @DisplayName("다른 ID를 가진 응답은 동등하지 않다")
        void shouldNotBeEqualWhenDifferentId() {
            // given
            Survey survey = new Survey("설문조사", "설명");
            SurveyResponse response1 = new SurveyResponse(survey, "응답자");
            SurveyResponse response2 = new SurveyResponse(survey, "응답자");

            // when & then
            assertThat(response1).isNotEqualTo(response2);
        }

        @Test
        @DisplayName("자기 자신과는 동등하다")
        void shouldBeEqualToItself() {
            // given
            Survey survey = new Survey("설문조사", "설명");
            SurveyResponse response = new SurveyResponse(survey, "응답자");

            // when & then
            assertThat(response).isEqualTo(response);
        }

        @Test
        @DisplayName("null과는 동등하지 않다")
        void shouldNotBeEqualToNull() {
            // given
            Survey survey = new Survey("설문조사", "설명");
            SurveyResponse response = new SurveyResponse(survey, "응답자");

            // when & then
            assertThat(response).isNotEqualTo(null);
        }

        @Test
        @DisplayName("다른 클래스와는 동등하지 않다")
        void shouldNotBeEqualToDifferentClass() {
            // given
            Survey survey = new Survey("설문조사", "설명");
            SurveyResponse response = new SurveyResponse(survey, "응답자");
            String other = "other";

            // when & then
            assertThat(response).isNotEqualTo(other);
        }

        @Test
        @DisplayName("ID가 null인 경우 hashCode는 0이다")
        void shouldReturnZeroHashCodeWhenIdIsNull() {
            // given
            Survey survey = new Survey("설문조사", "설명");
            SurveyResponse response = new SurveyResponse(survey, "응답자");
            
            // ID를 null로 만들기 위해 리플렉션 사용
            try {
                var idField = SurveyResponse.class.getDeclaredField("id");
                idField.setAccessible(true);
                idField.set(response, null);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            // when & then
            assertThat(response.hashCode()).isEqualTo(0);
        }
    }

    @Nested
    @DisplayName("실제 시나리오 테스트")
    class RealScenarioTest {

        @Test
        @DisplayName("완전한 설문조사 응답 시나리오")
        void shouldHandleCompleteResponseScenario() {
            // given - 설문조사 생성
            Survey survey = new Survey("고객 만족도 조사", "서비스 개선을 위한 조사");
            
            SurveyQuestion nameQuestion = new SurveyQuestion("이름", "성함을 입력해주세요", QuestionType.SHORT_TEXT, true);
            SurveyQuestion ageQuestion = new SurveyQuestion("연령대", "연령대를 선택해주세요", QuestionType.SINGLE_CHOICE, true, 
                List.of("10대", "20대", "30대", "40대", "50대 이상"));
            SurveyQuestion satisfactionQuestion = new SurveyQuestion("만족도", "서비스 만족도를 평가해주세요", QuestionType.SINGLE_CHOICE, true,
                List.of("매우 불만족", "불만족", "보통", "만족", "매우 만족"));
            SurveyQuestion improvementQuestion = new SurveyQuestion("개선사항", "개선이 필요한 부분을 모두 선택해주세요", QuestionType.MULTIPLE_CHOICE, false,
                List.of("응답 속도", "UI/UX", "기능", "고객 서비스", "가격"));
            SurveyQuestion feedbackQuestion = new SurveyQuestion("기타 의견", "추가 의견을 자유롭게 작성해주세요", QuestionType.LONG_TEXT, false);
            
            survey.addQuestion(nameQuestion);
            survey.addQuestion(ageQuestion);
            survey.addQuestion(satisfactionQuestion);
            survey.addQuestion(improvementQuestion);
            survey.addQuestion(feedbackQuestion);

            // when - 응답 생성 및 답변 추가
            SurveyResponse response = new SurveyResponse(survey, "홍길동 (hong@example.com)");
            
            response.addAnswer(new SurveyAnswer(nameQuestion.getId(), "이름", QuestionType.SHORT_TEXT, List.of("홍길동")));
            response.addAnswer(new SurveyAnswer(ageQuestion.getId(), "연령대", QuestionType.SINGLE_CHOICE, List.of("30대")));
            response.addAnswer(new SurveyAnswer(satisfactionQuestion.getId(), "만족도", QuestionType.SINGLE_CHOICE, List.of("만족")));
            response.addAnswer(new SurveyAnswer(improvementQuestion.getId(), "개선사항", QuestionType.MULTIPLE_CHOICE, List.of("UI/UX", "기능")));
            response.addAnswer(new SurveyAnswer(feedbackQuestion.getId(), "기타 의견", QuestionType.LONG_TEXT, List.of("전체적으로 만족하지만 모바일 버전 개선이 필요합니다.")));

            // then - 응답 검증
            assertThat(response.isCompleted()).isTrue();
            assertThat(response.getAnsweredQuestionCount()).isEqualTo(5);
            assertThat(response.getSurvey()).isSameAs(survey);
            assertThat(response.getRespondentInfo()).isEqualTo("홍길동 (hong@example.com)");

            // 각 질문별 답변 확인
            SurveyAnswer nameAnswer = response.getAnswerByQuestionId(nameQuestion.getId());
            assertThat(nameAnswer).isNotNull();
            assertThat(nameAnswer.getSingleAnswer()).isEqualTo("홍길동");

            SurveyAnswer ageAnswer = response.getAnswerByQuestionId(ageQuestion.getId());
            assertThat(ageAnswer).isNotNull();
            assertThat(ageAnswer.getSingleAnswer()).isEqualTo("30대");

            SurveyAnswer satisfactionAnswer = response.getAnswerByQuestionId(satisfactionQuestion.getId());
            assertThat(satisfactionAnswer).isNotNull();
            assertThat(satisfactionAnswer.getSingleAnswer()).isEqualTo("만족");

            SurveyAnswer improvementAnswer = response.getAnswerByQuestionId(improvementQuestion.getId());
            assertThat(improvementAnswer).isNotNull();
            assertThat(improvementAnswer.getAllAnswers()).containsExactly("UI/UX", "기능");

            SurveyAnswer feedbackAnswer = response.getAnswerByQuestionId(feedbackQuestion.getId());
            assertThat(feedbackAnswer).isNotNull();
            assertThat(feedbackAnswer.getSingleAnswer()).isEqualTo("전체적으로 만족하지만 모바일 버전 개선이 필요합니다.");
        }

        @Test
        @DisplayName("부분 응답 시나리오 (필수가 아닌 질문은 건너뛰기)")
        void shouldHandlePartialResponseScenario() {
            // given
            Survey survey = new Survey("간단한 설문", "필수만 답변");
            SurveyQuestion requiredQuestion = new SurveyQuestion("이름", "필수 입력", QuestionType.SHORT_TEXT, true);
            SurveyQuestion optionalQuestion = new SurveyQuestion("의견", "선택 입력", QuestionType.LONG_TEXT, false);
            
            survey.addQuestion(requiredQuestion);
            survey.addQuestion(optionalQuestion);

            // when - 필수 질문만 답변
            SurveyResponse response = new SurveyResponse(survey, null);
            response.addAnswer(new SurveyAnswer(requiredQuestion.getId(), "이름", QuestionType.SHORT_TEXT, List.of("익명")));

            // then
            assertThat(response.isCompleted()).isTrue();
            assertThat(response.getAnsweredQuestionCount()).isEqualTo(1);
            assertThat(response.getAnswerByQuestionId(requiredQuestion.getId())).isNotNull();
            assertThat(response.getAnswerByQuestionId(optionalQuestion.getId())).isNull();
        }
    }
}
