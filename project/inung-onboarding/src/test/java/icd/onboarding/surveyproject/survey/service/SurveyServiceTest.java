package icd.onboarding.surveyproject.survey.service;

import icd.onboarding.surveyproject.survey.fixtures.SurveyFixtures;
import icd.onboarding.surveyproject.survey.repository.ResponseRepository;
import icd.onboarding.surveyproject.survey.repository.SurveyRepository;
import icd.onboarding.surveyproject.survey.service.domain.Answer;
import icd.onboarding.surveyproject.survey.service.domain.Question;
import icd.onboarding.surveyproject.survey.service.domain.Response;
import icd.onboarding.surveyproject.survey.service.domain.Survey;
import icd.onboarding.surveyproject.survey.service.enums.InputType;
import icd.onboarding.surveyproject.survey.service.exception.DuplicateResponseException;
import icd.onboarding.surveyproject.survey.service.exception.SurveyNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

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
			Mockito.when(surveyRepository.findByIdAndVersion(existingSurvey.getId(), existingSurvey.getVersion()))
				   .thenReturn(Optional.of(existingSurvey));
			Mockito.when(surveyRepository.save(any())).thenReturn(updatedSurvey);
			Survey updated = sut.updateSurvey(existingSurvey);

			// then
			assertEquals(existingSurvey.getId(), updated.getId());
			assertEquals(existingSurvey.getVersion() + 1, updated.getVersion());
			assertFalse(updated.getQuestions().isEmpty());

			Mockito.verify(surveyRepository, Mockito.times(1)).save(updatedSurvey);
		}
	}

	@Nested
	class SubmitResponse {
		final UUID surveyId = UUID.randomUUID();
		final int version = 1;
		final String repondentId = "test-id";

		@Test
		@DisplayName("응답으로 제출한 설문이 존재하지 않으면 예외를 발생 시킨다.")
		void throwExceptionWhenSurveyNotFound () {
			// given
			Response response = Response.create(
					surveyId,
					version,
					repondentId,
					List.of(Answer.create("응답 1", UUID.randomUUID()))
			);

			// when
			Mockito.when(surveyRepository.findByIdAndVersion(surveyId, version)).thenReturn(Optional.empty());

			// when & then
			assertThrows(
					SurveyNotFoundException.class,
					() -> sut.submitResponse(response)
			);
		}

		@Test
		@DisplayName("중복 응답을 제출하면 예외를 발생 시킨다.")
		void throwExceptionWhenDuplicateResponse () {
			// given
			Response response = Response.create(
					surveyId,
					version,
					repondentId,
					List.of()
			);

			// when
			Mockito.when(responseRepository.existsBySurveyIdAndVersionAndRespondentId(surveyId, version, repondentId)).thenReturn(true);

			// then
			assertThrows(
					DuplicateResponseException.class,
					() -> sut.submitResponse(response)
			);
		}


		@Test
		@DisplayName("응답을 제출하고, 저장소에 저장되어야 한다.")
		void shouldSubmitResponseAndSave () {
			// given
			Survey survey = SurveyFixtures.basicSurvey();
			Answer answer1 = Answer.create("답변 1", survey.getQuestions().get(0).getId());
			Answer answer2 = Answer.create("답변 2", survey.getQuestions().get(1).getId());

			Response response = Response.create(
					survey.getId(),
					survey.getVersion(),
					repondentId,
					List.of(answer1, answer2)
			);

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

	@Nested
	class FindResponses {

		@Test
		@DisplayName("설문 ID와 버전으로 응답을 조회하면 질문명과 응답값을 반환한다.")
		void shouldReturnAnswersWithQuestionNames () {
			// given
			UUID surveyId = UUID.randomUUID();
			int version = 1;
			String respondentid = "test-id";

			Question q1 = Question.create("질문 1", "설명", InputType.SHORT_TEXT, true, 1, List.of());
			Question q2 = Question.create("질문 2", "설명", InputType.SHORT_TEXT, false, 2, List.of());

			Response response = Response.create(
					surveyId,
					version,
					respondentid,
					List.of(
							Answer.create("응답 1", q1.getId()),
							Answer.create("응답 2", q2.getId())
					));
			Mockito.when(responseRepository.findBySurveyIdAndSurveyVersion(surveyId, version))
				   .thenReturn(Optional.of(List.of(response)));

			// when
			List<Response> results = sut.findResponsesBySurvey(surveyId, version);

			// then
			assertEquals(1, results.size());
			Response selectedResponse = results.get(0);
			assertEquals(2, selectedResponse.getAnswers().size());

			assertThat(selectedResponse.getAnswers()).extracting("questionId", "text").containsExactlyInAnyOrder(
					tuple(q1.getId(), response.getAnswers().get(0).getText()),
					tuple(q2.getId(), response.getAnswers().get(1).getText())
			);
		}
	}
}
