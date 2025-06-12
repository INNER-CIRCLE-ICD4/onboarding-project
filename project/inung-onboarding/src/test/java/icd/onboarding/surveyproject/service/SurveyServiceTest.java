package icd.onboarding.surveyproject.service;

import icd.onboarding.surveyproject.common.NotImplementedTestException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class SurveyServiceTest {

	@Nested
	class CreateSurvey {

		@Test
		@DisplayName("설문 조사의 이름, 설명, 질문의 값이 없는 경우 IllegalArgumentException을 반환 한다.")
		void test1 () {
			throw new NotImplementedTestException();
		}

		@Test
		@DisplayName("질문의 수가 없는 경우 InSufficientQuestionsException을 반환 한다.")
		void test2 () {
			throw new NotImplementedTestException();
		}

		@Test
		@DisplayName("질문의 수가 10개를 초과한 경우 MaxQuestionLimitExceededException을 반환 한다.")
		void test3 () {
			throw new NotImplementedTestException();
		}
	}
}
