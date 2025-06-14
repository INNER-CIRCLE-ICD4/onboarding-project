package icd.onboarding.surveyproject.survey.service.domain;

import icd.onboarding.surveyproject.survey.service.enums.InputType;
import icd.onboarding.surveyproject.survey.service.exception.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class QuestionTest {
	final String validName = "질문 1";
	final String validDescription = "질문 설명";
	final Integer validSortOrder = 1;
	final String validInputType = "SHORT_TEXT";
	final Boolean validRequired = true;
	final List<Option> validOptions = List.of(Option.create("옵션 1", 1));

	@ParameterizedTest
	@CsvSource({"null, ''", "'', null"})
	@DisplayName("질문의 이름, 설명이 null이거나 비어있는 경우 예외를 반환 한다.")
	void throwWhenQuestionNameOrDescriptionIsNullAndEmpty (String invalidName, String invalidDescription) {
		// when & then
		assertThrows(
				InvalidQuestionInfoException.class,
				() -> Question.create(invalidName, invalidDescription, validInputType, validRequired, validSortOrder, validOptions)
		);
	}

	@Test
	@DisplayName("질문의 정렬 순서가 음수인 경우 예외를 반환 한다.")
	void throwWhenQuestionSortOrderIsNegativeNumber () {
		// when & then
		assertThrows(
				NotNegativeNumberException.class,
				() -> Question.create(validName, validDescription, validInputType, validRequired, -1, validOptions)
		);
	}

	@Test
	@DisplayName("입력 형태가 InputType Enum에 포함되지 않은 경우 예외를 반환 한다.")
	void throwWhenQuestionInputTypeIsNotContains () {
		// when & Then
		assertThrows(
				InvalidInputTypeException.class,
				() -> Question.create(validName, validDescription, "INVALID_TYPE", validRequired, validSortOrder, validOptions)
		);
	}

	@ParameterizedTest
	@ValueSource(strings = {"SINGLE_SELECT", "MULTI_SELECT"})
	@DisplayName("입력 형태가 SINGLE_SELECT, MULTI_SELECT인 경우 옵션이 존재하지 않으면 예외를 반환 한다.")
	void throwWhenQuestionOptionsIsEmpty (String inputType) {
		// given
		List<Option> emptyOptions = Collections.emptyList();

		// when
		assertThrows(
				InSufficientOptionException.class,
				() -> Question.create(validName, validDescription, inputType, validRequired, validSortOrder, emptyOptions)
		);
	}

	@Test
	@DisplayName("입력 형태가 SINGLE_SELECT, MULTI_SELECT인 경우 옵션이 10개 이상이면 예외를 반환 한다.")
	void throwWhenQuestionOptionIsOver10Count () {
		// given
		List<Option> overCountOptions = IntStream.rangeClosed(1, 11)
												 .mapToObj(i -> Option.create("질문 " + i, i))
												 .toList();

		// when
		assertThrows(
				MaxOptionCountExceededException.class,
				() -> Question.create(validName, validDescription, validInputType, validRequired, validSortOrder, overCountOptions)
		);
	}

	@Test
	@DisplayName("질문의 입력 값에 이상이 없는 경우 정상적으로 객체가 생성됩니다.")
	void shouldCreateQuestionWhenInputsAreValid () {
		// when
		Question question = Question.create(validName, validDescription, validInputType, validRequired, validSortOrder, validOptions);

		// then
		assertNotNull(question);
		assertNotNull(question.getId());
		assertEquals(validName, question.getName());
		assertEquals(validDescription, question.getDescription());
		assertEquals(InputType.valueOf(validInputType), question.getInputType());
		assertEquals(validRequired, question.getRequired());
		assertEquals(validSortOrder, question.getSortOrder());
	}
}
