package icd.onboarding.surveyproject.service.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class OptionTest {
	final UUID validQuestionId = UUID.randomUUID();
	final String validText = "옵션 1";
	final Integer validSortOrder = 1;

	@ParameterizedTest
	@NullAndEmptySource
	@DisplayName("옵션 값이 null이거나 비어 있으면 예외를 반환 합니다.")
<<<<<<< HEAD
	void throwExceptionWhenOptionTextIsNullAndEmpty (String text) {
=======
	void test1 (String text) {
>>>>>>> f24b238 (feat: Option 도메인 단위 테스트 성공)
		// when
		Exception ex = assertThrows(IllegalArgumentException.class, () -> Option.create(validQuestionId, text, validSortOrder));

		// then
		String expected = "옵션 텍스트는 필수입니다.";
		assertEquals(expected, ex.getMessage());
	}

	@Test
	@DisplayName("옵션의 sortOrder가 음수일 경우 예외를 반환 합니다.")
<<<<<<< HEAD
	void throwExceptionWhenOptionSortOrderIsNegativeNumber () {
=======
	void test2 () {
>>>>>>> f24b238 (feat: Option 도메인 단위 테스트 성공)
		// when
		Exception ex = assertThrows(IllegalArgumentException.class, () -> Option.create(validQuestionId, validText, -1));

		// then
		String expected = "정렬 순서는 음수일 수 없습니다.";
		assertEquals(expected, ex.getMessage());
	}

	@Test
	@DisplayName("옵션의 questionId가 비어 있으면 예외를 반환 합니다.")
<<<<<<< HEAD
	void throwExceptionWhenOptionQuestionIdIsNull () {
=======
	void test3 () {
>>>>>>> f24b238 (feat: Option 도메인 단위 테스트 성공)
		// when
		Exception ex = assertThrows(IllegalArgumentException.class, () -> Option.create(null, validText, validSortOrder));

		// then
		String expected = "질문의 Id는 비어 있을 수 없습니다.";
		assertEquals(expected, ex.getMessage());
	}

	@Test
	@DisplayName("옵션의 값과 ID가 유효하며, sortOrder가 음수가 아니면 객체가 생성됩니다.")
<<<<<<< HEAD
	void shouldCreateOptionWhenInputsAreValid () {
=======
	void test4 () {
>>>>>>> f24b238 (feat: Option 도메인 단위 테스트 성공)
		// when
		Option option = Option.create(validQuestionId, validText, validSortOrder);

		// then
		assertNotNull(option);
		assertNotNull(option.getId());
		assertEquals(validQuestionId, option.getQuestionId());
		assertEquals(validText, option.getText());
		assertEquals(validSortOrder, option.getSortOrder());
	}
}
