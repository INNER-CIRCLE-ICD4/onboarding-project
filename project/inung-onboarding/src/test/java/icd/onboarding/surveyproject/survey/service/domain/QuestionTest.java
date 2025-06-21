package icd.onboarding.surveyproject.survey.service.domain;

import icd.onboarding.surveyproject.survey.fixtures.QuestionFixtures;
import icd.onboarding.surveyproject.survey.service.enums.InputType;
import icd.onboarding.surveyproject.survey.service.exception.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class QuestionTest {
	final String validName = "질문 1";
	final String validDescription = "질문 설명";
	final Integer validSortOrder = 1;
	final Boolean validRequired = true;

	@ParameterizedTest
	@ValueSource(strings = {"SINGLE_SELECT", "MULTI_SELECT"})
	@DisplayName("입력 형태가 SINGLE_SELECT, MULTI_SELECT인 경우 옵션이 존재하지 않으면 예외를 발생")
	void throwWhenQuestionOptionsIsEmpty (InputType inputType) {
		// given
		List<Option> emptyOptions = Collections.emptyList();

		// when & then
		assertThrows(
				InSufficientOptionException.class,
				() -> Question.create(validName, validDescription, inputType, validRequired, validSortOrder, emptyOptions)
		);
	}

	@ParameterizedTest
	@ValueSource(strings = {"SHORT_TEXT", "LONG_TEXT"})
	@DisplayName("입력 형태가 SHORT_TEXT, LONG_TEXT인 경우 옵션이 존재하면 예외를 발생")
	void throwExceptionWhenNotSelectTypeQuestionHasOption (InputType inputType) {
		// given
		List<Option> options = List.of(Option.create("옵션 1", 1));

		// when
		assertThrows(
				UnsupportedOptionException.class,
				() -> Question.create(validName, validDescription, inputType, validRequired, validSortOrder, options)
		);
	}

	@Test
	@DisplayName("필수 질문인 경우 응답이 존재하지 않으면 예외를 발생")
	void throwExceptionWhenRequiredQuestionNotHasAnswers () {
		// given
		List<Answer> emptyAnswers = Collections.emptyList();
		Question question = QuestionFixtures.singleSelectQuestion();

		// when & then
		assertThrows(
				RequiredQuestionNotAnsweredException.class,
				() -> question.validateRequiredAnswer(emptyAnswers)
		);
	}
}
