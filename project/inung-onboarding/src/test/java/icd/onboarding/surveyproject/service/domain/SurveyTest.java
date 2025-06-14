package icd.onboarding.surveyproject.service.domain;

import icd.onboarding.surveyproject.fixtures.QuestionFixtures;
import icd.onboarding.surveyproject.service.exception.InSufficientQuestionException;
import icd.onboarding.surveyproject.service.exception.InValidSurveyInfoException;
import icd.onboarding.surveyproject.service.exception.MaxQuestionCountExceededException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertThrows;

class SurveyTest {

	@ParameterizedTest
	@NullAndEmptySource
	@DisplayName("설문 조사 이름이 null 또는 비어있는 경우 예외를 반환 한다.")
	void throwExceptionWhenSurveyNameIsNullAndEmpty (String invalidTitle) {
		// given
		List<Question> questions = List.of(QuestionFixtures.basicQuestion());

		// when & then
		assertThrows(
				InValidSurveyInfoException.class,
				() -> Survey.create(invalidTitle, "설문 조사에 대한 설명", questions)
		);
	}

	@Test
	@DisplayName("설문 조사의 질문이 비어있는 경우 예외를 반환 한다.")
	void throwExceptionWhenQuestionsIsEmpty () {
		// given
		List<Question> emptyQuestions = Collections.emptyList();

		// when & then
		assertThrows(
				InSufficientQuestionException.class,
				() -> Survey.create("설문 조사 1", "설문 조사에 대한 설명", emptyQuestions)
		);
	}

	@Test
	@DisplayName("설문 조사의 질문이 10개를 초과한 경우 예외를 반환 한다.")
	void throwWhenQuestionIsOver10Count () {
		// given
		List<Question> overCountQuestions = IntStream.rangeClosed(1, 11)
													 .mapToObj(i -> QuestionFixtures.basicQuestion())
													 .toList();

		// when
		assertThrows(
				MaxQuestionCountExceededException.class,
				() -> Survey.create("설문 조사 1", "설문 조사에 대한 설명", overCountQuestions)
		);
	}
}
