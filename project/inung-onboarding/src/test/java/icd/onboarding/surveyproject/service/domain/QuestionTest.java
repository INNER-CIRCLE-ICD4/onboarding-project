package icd.onboarding.surveyproject.service.domain;

import icd.onboarding.surveyproject.common.NotImplementedTestException;
import icd.onboarding.surveyproject.service.enums.InputType;
import icd.onboarding.surveyproject.service.enums.QuestionErrors;
import icd.onboarding.surveyproject.service.exception.InvalidQuestionException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.CsvSources;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.ValueSources;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class QuestionTest {
	final UUID validSurveyId = UUID.randomUUID();
	final Integer validSurveyVersion = 1;
	final String validName = "질문 1";
	final String validDescription = "질문 설명";
	final Integer validSortOrder = 1;
	final String validInputType = "SHORT_TEXT";
	final Boolean validRequired = true;

	@Test
	@DisplayName("설문 id 및 버전이 null인 경우 예외를 반환 한다.")
	void throwWhenQuestionSurveyInfoIsNull () {
		// when
		Exception ex = assertThrows(
				InvalidQuestionException.class,
				() -> Question.create(null, null, validName, validDescription, validInputType, validRequired, validSortOrder)
		);

		// then
		assertEquals(QuestionErrors.INVALID_SURVEY_INFO.value(), ex.getMessage());
	}

	@ParameterizedTest
	@CsvSource({"null, ''", "'', null"})
	@DisplayName("질문의 이름, 설명이 null이거나 비어있는 경우 예외를 반환 한다.")
	void throwWhenQuestionNameOrDescriptionIsNullAndEmpty (String name, String description) {
		// when
		Exception ex = assertThrows(
				InvalidQuestionException.class,
				() -> Question.create(validSurveyId, validSurveyVersion, name, description, validInputType, validRequired, validSortOrder)
		);

		// then
		assertEquals(QuestionErrors.EMPTY_QUESTION_INFO.value(), ex.getMessage());
	}

	@Test
	@DisplayName("질문의 정렬 순서가 음수인 경우 예외를 반환 한다.")
	void throwWhenQuestionSortOrderIsNegativeNumber () {
		// when
		Exception ex = assertThrows(
				InvalidQuestionException.class,
				() -> Question.create(validSurveyId, validSurveyVersion, validName, validDescription, validInputType, validRequired, -1)
		);

		// then
		assertEquals(QuestionErrors.NOT_NEGATIVE_NUMBER.value(), ex.getMessage());
	}

	@Test
	@DisplayName("입력 형태가 InputType Enum에 포함되지 않은 경우 예외를 반환 한다.")
	void throwWhenQuestionInputTypeIsNotContains () {
		// when
		Exception ex = assertThrows(
				InvalidQuestionException.class,
				() -> Question.create(validSurveyId, validSurveyVersion, validName, validDescription, "INVALID_TYPE", validRequired, validSortOrder)
		);

		// then
		assertEquals(QuestionErrors.INVALID_INPUT_TYPE.value(), ex.getMessage());
	}

	@Test
	@DisplayName("입력 형태가 SINGLE_SELECT, MULTI_SELECT 경우 옵션이 최소 1개 이상 존재해야 한다.")
	void throwWhenQuestionOptionsIsEmpty () {
		throw new NotImplementedTestException();
	}

	@Test
	@DisplayName("질문의 입력 값에 이상이 없는 경우 정상적으로 객체가 생성됩니다.")
	void shouldCreateQuestionWhenInputsAreValid () {
		// when
		Question question = Question.create(validSurveyId, validSurveyVersion, validName, validDescription, validInputType, validRequired, validSortOrder);

		// then
		assertNotNull(question);
		assertNotNull(question.getId());
		assertEquals(validSurveyId, question.getSurveyId());
		assertEquals(validSurveyVersion, question.getSurveyVersion());
		assertEquals(validName, question.getName());
		assertEquals(validDescription, question.getDescription());
		assertEquals(validInputType, question.getInputType());
		assertEquals(validRequired, question.getRequired());
		assertEquals(validSortOrder, question.getSortOrder());
	}
}
