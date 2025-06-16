package icd.onboarding.surveyproject.survey.service;

import icd.onboarding.surveyproject.survey.fixtures.SurveyFixtures;
import icd.onboarding.surveyproject.survey.repository.SurveyRepository;
import icd.onboarding.surveyproject.survey.service.domain.Survey;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class SurveyServiceTest {
	@InjectMocks
	private SurveyService sut;

	@Mock
	private SurveyRepository surveyRepository;

	@Nested
	class CreateSurvey {
		@Test
		@DisplayName("설문 조사를 생성할 수 있어야 한다.")
		void shouldCreateSurveyWithValidInput () {
			// given
			Survey survey = SurveyFixtures.basicSurvey();

			// when
			Mockito.when(surveyRepository.save(survey)).thenReturn(survey);
			Survey created = sut.createSurvey(survey);

			// then
			assertEquals(survey.getId(), created.getId());
			assertEquals(1, created.getVersion());
			assertEquals(survey.getQuestions().size(), created.getQuestions().size());
		}
	}

	@Nested
	class UpdateSurvey {

	}

	@Nested
	class SubmitResponse {

	}
}
