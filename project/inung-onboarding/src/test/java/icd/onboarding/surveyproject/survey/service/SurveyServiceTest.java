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

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class SurveyServiceTest {
	@InjectMocks
	private SurveyService sut;

	@Mock
	private SurveyRepository surveyRepository;

	@Nested
	class CreateSurvey {
		@Test
		@DisplayName("설문 조사를 생성하고, ID를 반환 한다.")
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

			Mockito.verify(surveyRepository, Mockito.times(1)).save(survey);
		}
	}

	@Nested
	class UpdateSurvey {
		@Test
		@DisplayName("설문 조사 수정 시 버전을 1씩 증가하고, 질문들과의 관계가 유지되어야 한다.")
		void shouldUpdateSurveyThenIncreaseVersionAndRelatedQuestions () {
			// given
			Survey existingSurvey = SurveyFixtures.basicSurvey();
			Survey updatedSurvey = SurveyFixtures.updatedSurvey(existingSurvey.getId(), existingSurvey.getVersion());

			// when
			Mockito.when(surveyRepository.save(updatedSurvey)).thenReturn(updatedSurvey);
			Survey updated = sut.updateSurvey(existingSurvey);

			// then
			assertEquals(existingSurvey.getId(), updated.getId());
			assertEquals(existingSurvey.getVersion() + 1, updated.getVersion());
			assertFalse(updated.getQuestions().isEmpty());

			Mockito.verify(surveyRepository, Mockito.times(1)).save(Mockito.any());
		}
	}

	@Nested
	class SubmitResponse {

	}
}
