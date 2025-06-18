package icd.onboarding.surveyproject.survey.service.domain;

import icd.onboarding.surveyproject.survey.fixtures.SurveyFixtures;
import icd.onboarding.surveyproject.survey.service.exception.RequiredQuestionNotAnsweredException;
import icd.onboarding.surveyproject.survey.service.exception.TooManyAnswersException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ResponseTest {

	final UUID surveyId = UUID.randomUUID();
	final int surveyVersion = 1;
	final String respondentId = "test-id";

	@Nested
	class ValidateRequiredAnswers {

		@Test
		@DisplayName("필수 질문이 응답에 모두 포함되어 있지 않으면 예외를 발생시킨다.")
		void throwExceptionWhenRequiredQuestionsNotAnswered () {
			// given
			Survey survey = SurveyFixtures.basicSurveyWithRequiredQuestion();
			List<Answer> incompleteAnswers = List.of();
			Response response = Response.create(
					surveyId,
					surveyVersion,
					respondentId,
					incompleteAnswers
			);

			// when & then
			assertThrows(
					RequiredQuestionNotAnsweredException.class,
					() -> response.validateRequiredAnswers(survey)
			);
		}

		@Test
		@DisplayName("필수 질문이 응답에 모두 포함되어 있으면 예외가 발생하지 않는다.")
		void shouldPassWhenAllRequiredQuestionsAnswered () {
			// given
			Survey survey = SurveyFixtures.basicSurveyWithRequiredQuestion();
			UUID requiredQuestionId = survey.getQuestions().get(0).getId();

			List<Answer> answers = List.of(Answer.create("응답", requiredQuestionId));
			Response response = Response.create(
					surveyId,
					surveyVersion,
					respondentId,
					answers
			);

			// when & then
			assertDoesNotThrow(() -> response.validateRequiredAnswers(survey));
		}
	}

	@Nested
	class ValidateSingleSelectAnswers {

		@Test
		@DisplayName("SINGLE_SELECT 질문에 대해 2개 이상의 응답이 존재하면 예외를 발생시킨다.")
		void throwExceptionWhenTooManyAnswersToSingleSelect () {
			// given
			Survey survey = SurveyFixtures.basicSurveyWithSingleSelectQuestion();
			UUID questionId = survey.getQuestions().get(0).getId();

			List<Answer> multipleAnswers = List.of(
					Answer.create("1", questionId),
					Answer.create("2", questionId)
			);

			Response response = Response.create(
					surveyId,
					surveyVersion,
					respondentId,
					multipleAnswers
			);

			// when & then
			assertThrows(
					TooManyAnswersException.class,
					() -> response.validateSingleSelectAnswers(survey)
			);
		}

		@Test
		@DisplayName("SINGLE_SELECT 질문에 대해 하나의 응답만 존재하면 예외가 발생하지 않는다.")
		void shouldPassWhenSingleAnswerToSingleSelect () {
			// given
			Survey survey = SurveyFixtures.basicSurveyWithSingleSelectQuestion();
			UUID questionId = survey.getQuestions().get(0).getId();

			List<Answer> answers = List.of(Answer.create("1", questionId));
			Response response = Response.create(
					surveyId,
					surveyVersion,
					respondentId,
					answers
			);

			// when & then
			assertDoesNotThrow(() -> response.validateSingleSelectAnswers(survey));
		}
	}
}