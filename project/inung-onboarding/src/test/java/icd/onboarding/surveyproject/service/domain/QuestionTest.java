package icd.onboarding.surveyproject.service.domain;

import icd.onboarding.surveyproject.common.NotImplementedTestException;
import icd.onboarding.surveyproject.service.enums.InputType;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Disabled
class QuestionTest {

	@Test
	@DisplayName("설문 id 및 버전이 null인 경우 예외를 반환 한다.")
	void throwWhenQuestionSurveyInfoIsNull () {
		throw new NotImplementedTestException();
	}

	@Test
	@DisplayName("질문의 이름, 설명이 null이거나 비어있는 경우 예외를 반환 한다.")
	void throwWhenQuestionNameOrDescriptionIsNullAndEmpty () {
		throw new NotImplementedTestException();
	}

	@Test
	@DisplayName("질문의 정렬 순서가 음수인 경우 예외를 반환 한다.")
	void throwWhenQuestionSortOrderIsNegativeNumber () {
		throw new NotImplementedTestException();
	}

	@Test
	@DisplayName("입력 형태가 InputType Enum에 포함되지 않은 경우 예외를 반환 한다.")
	void throwWhenQuestionInputTypeIsNotContains () {
		throw new NotImplementedTestException();
	}

	@Test
	@DisplayName("입력 형태가 SINGLE_SELECT, MULTI_SELECT 경우 옵션이 최소 1개 이상 존재해야 한다.")
	void throwWhenQuestionOptionsIsEmpty () {
		throw new NotImplementedTestException();
	}

	@Test
	@DisplayName("질문의 입력 값에 이상이 없는 경우 정상적으로 객체가 생성됩니다.")
	void shouldCreateQuestionWhenInputsAreValid () {
		// given
		UUID surveyId = UUID.randomUUID();
		Integer surveyVersion = 1;
		String name = "질문 1";
		String description = "질문 설명";
		InputType inputType = InputType.valueOf("SHORT_TEXT");
		Boolean required = true;
		Integer sortOrder = 1;

		// when
		Question question = Question.create(surveyId, surveyVersion, name, description, inputType, required, sortOrder);

		// then
		assertNotNull(question);
		assertNotNull(question.getId());
		assertEquals(surveyId, question.getSurveyId());
		assertEquals(surveyVersion, question.getSurveyVersion());
		assertEquals(name, question.getName());
		assertEquals(description, question.getDescription());
		assertEquals(inputType, question.getInputType());
		assertEquals(required, question.getRequired());
		assertEquals(sortOrder, question.getSortOrder());
	}
}
