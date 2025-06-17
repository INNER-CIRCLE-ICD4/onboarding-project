package icd.onboarding.surveyproject.survey.service;

import icd.onboarding.surveyproject.survey.fixtures.SurveyFixtures;
import icd.onboarding.surveyproject.survey.repository.ResponseRepository;
import icd.onboarding.surveyproject.survey.repository.SurveyRepository;
import icd.onboarding.surveyproject.survey.service.domain.Answer;
import icd.onboarding.surveyproject.survey.service.domain.Question;
import icd.onboarding.surveyproject.survey.service.domain.Response;
import icd.onboarding.surveyproject.survey.service.domain.Survey;
import icd.onboarding.surveyproject.survey.service.exception.RequiredQuestionNotAnsweredException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class SurveyServiceTest {
	@InjectMocks
	private SurveyService sut;

	@Mock
	private SurveyRepository surveyRepository;
	@Mock
	private ResponseRepository responseRepository;

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
		@DisplayName("설문 조사 수정 시 버전이 1씩 증가하고, 질문을 보유해야 한다.")
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
		@Test
		@DisplayName("필수 질문에 응답하지 않으면 예외가 발생해야 한다.")
		void shouldThrowWhenRequiredQuestionIsNotAnswered () {
			// given
			Survey survey = SurveyFixtures.basicSurveyWithRequiredQuestion(); // 필수 질문 포함
			UUID surveyId = survey.getId();
			int version = survey.getVersion();
			Response response = Response.create(surveyId, version, Collections.emptyList());

			// when
			Mockito.when(surveyRepository.findByIdAndVersion(surveyId, version)).thenReturn(Optional.of(survey));

			// when & then
			assertThrows(
					RequiredQuestionNotAnsweredException.class,
					() -> sut.submitResponse(response)
			);
		}

		@Test
		@DisplayName("응답이 모든 필수 질문에 대해 포함된 경우 정상적으로 저장된다.")
		void shouldSubmitResponseWhenAllRequiredQuestionsAreAnswered () {
			// given
			Survey survey = SurveyFixtures.basicSurveyWithRequiredQuestion();
			UUID surveyId = survey.getId();
			int surveyVersion = survey.getVersion();

			List<Answer> partialAnswers = List.of(
					Answer.create("응답 1", survey.getQuestions().get(1).getId())
			);

			Response response = Response.create(surveyId, surveyVersion, partialAnswers);

			// when
			Mockito.when(surveyRepository.findByIdAndVersion(surveyId, surveyVersion)).thenReturn(Optional.of(survey));

			// then
			assertThrows(
					RequiredQuestionNotAnsweredException.class,
					() -> sut.submitResponse(response)
			);

			Mockito.verify(surveyRepository).findByIdAndVersion(surveyId, surveyVersion);
		}

		@Test
		@DisplayName("응답을 제출하고, 저장소에 저장되어야 한다.")
		void shouldSubmitResponseAndSave () {
			// given
			Survey survey = SurveyFixtures.basicSurvey();
			Answer answer1 = Answer.create("답변 1", survey.getQuestions().get(0).getId());
			Answer answer2 = Answer.create("답변 2", survey.getQuestions().get(1).getId());

			Response response = Response.create(survey.getId(), survey.getVersion(), List.of(answer1, answer2));

			// when
			Mockito.when(surveyRepository.findByIdAndVersion(survey.getId(), survey.getVersion())).thenReturn(Optional.of(survey));
			Mockito.when(responseRepository.save(response)).thenReturn(response);
			Response submitted = sut.submitResponse(response);

			// then
			assertNotNull(submitted);
			assertEquals(response.getSurveyId(), submitted.getSurveyId());
			assertEquals(2, submitted.getAnswers().size());

			Mockito.verify(responseRepository, Mockito.times(1)).save(response);
		}
	}
}
