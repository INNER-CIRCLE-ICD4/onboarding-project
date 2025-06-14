package icd.onboarding.surveyproject.survey.service.domain;

import icd.onboarding.surveyproject.survey.service.exception.InvalidOptionInfoException;
import icd.onboarding.surveyproject.survey.service.exception.NotNegativeNumberException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import static org.junit.jupiter.api.Assertions.*;

class OptionTest {
	final String validText = "옵션 1";
	final Integer validSortOrder = 1;

	@ParameterizedTest
	@NullAndEmptySource
	@DisplayName("옵션 값이 null이거나 비어 있으면 예외를 반환 합니다.")
	void throwExceptionWhenOptionTextIsNullAndEmpty (String text) {
		// when & then
		assertThrows(InvalidOptionInfoException.class, () -> Option.create(text, validSortOrder));
	}

	@Test
	@DisplayName("옵션의 sortOrder가 음수일 경우 예외를 반환 합니다.")
	void throwExceptionWhenOptionSortOrderIsNegativeNumber () {
		// when & then
		assertThrows(NotNegativeNumberException.class, () -> Option.create(validText, -1));
	}

	@Test
	@DisplayName("옵션의 값과 ID가 유효하며, sortOrder가 음수가 아니면 객체가 생성됩니다.")
	void shouldCreateOptionWhenInputsAreValid () {
		// when
		Option option = Option.create(validText, validSortOrder);

		// then
		assertNotNull(option);
		assertNotNull(option.getId());
		assertEquals(validText, option.getText());
		assertEquals(validSortOrder, option.getSortOrder());
	}
}
