package icd.onboarding.surveyproject.survey.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import icd.onboarding.surveyproject.common.NotImplementedTestException;
import icd.onboarding.surveyproject.survey.common.controller.GlobalExceptionHandler;
import icd.onboarding.surveyproject.survey.controller.consts.ErrorCodes;
import icd.onboarding.surveyproject.survey.controller.dto.CreateQuestionRequest;
import icd.onboarding.surveyproject.survey.controller.dto.CreateSurveyRequest;
import icd.onboarding.surveyproject.survey.controller.dto.SurveyResponse;
import icd.onboarding.surveyproject.survey.fixtures.SurveyFixtures;
import icd.onboarding.surveyproject.survey.service.SurveyService;
import icd.onboarding.surveyproject.survey.service.domain.Survey;
import icd.onboarding.surveyproject.survey.service.enums.InputType;
import icd.onboarding.surveyproject.survey.service.exception.SurveyNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({SurveyController.class, GlobalExceptionHandler.class})
class SurveyControllerTest {
	@MockitoBean
	SurveyService surveyService;

	@Autowired
	MockMvc mockMvc;

	@Autowired
	ObjectMapper objectMapper;

	@DisplayName("설문 조회")
	@Nested
	class GetSurvey {
		final UUID surveyId = UUID.randomUUID();
		final int version = 1;

		@Test
		@DisplayName("조회 대상 설문 조사가 없으면 404 status와 error를 반환한다.")
		void throwExceptionWhenSurveyNotFound () throws Exception {
			// given
			given(surveyService.findByIdAndVersion(surveyId, version))
					.willThrow(new SurveyNotFoundException());

			// when & then
			mockMvc.perform(get("/api/v1/survey/{surveyId}/version/{version}", surveyId, version))
				   .andExpect(status().isNotFound())
				   .andExpect(jsonPath("$.error.code").value(ErrorCodes.SURVEY_NOT_FOUND.code))
				   .andExpect(jsonPath("$.error.message").value(ErrorCodes.SURVEY_NOT_FOUND.message));
		}

		@Test
		@DisplayName("정보가 올바른 경우 200 status와 data를 반환한다.")
		void shouldGetSurveys () throws Exception {
			// given
			Survey survey = SurveyFixtures.basicSurvey();
			SurveyResponse response = SurveyResponse.formDomain(survey);

			given(surveyService.findByIdAndVersion(surveyId, version))
					.willReturn(survey);

			// when & then
			mockMvc.perform(get("/api/v1/survey/{surveyId}/version/{version}", surveyId, version))
				   .andExpect(status().isOk())
				   .andExpect(jsonPath("$.data.id").value(response.id().toString()))
				   .andExpect(jsonPath("$.data.title").value(response.title()))
				   .andExpect(jsonPath("$.data.questions").isNotEmpty());
		}
	}

	@DisplayName("설문 생성")
	@Nested
	class CreateSurvey {
		String url = "/api/v1/survey";

		@ParameterizedTest
		@NullAndEmptySource
		@DisplayName("설문 조사의 제목이 존재하지 않으면 400 status와 error를 반환한다.")
		void throwExceptionWhenSurveyNameIsNullAndEmpty (String title) throws Exception {
			// given
			CreateSurveyRequest request = new CreateSurveyRequest(title, "", List.of());
			String body = objectMapper.writeValueAsString(request);

			given(surveyService.createSurvey(request.toDomain()))
					.willReturn(null);

			// when & then
			mockMvc.perform(post(url)
						   .content(body)
						   .contentType(MediaType.APPLICATION_JSON))
				   .andExpect(status().isBadRequest())
				   .andReturn();

			verify(surveyService, never()).createSurvey(any());
		}

		@Test
		@DisplayName("질문이 존재하지 않으면 400 status와 error를 반환 한다.")
		void throwExceptionWhenInSufficientQuestions () throws Exception {
			// given
			CreateSurveyRequest request = new CreateSurveyRequest("설문 조사 1", "", List.of());
			String body = objectMapper.writeValueAsString(request);

			given(surveyService.createSurvey(request.toDomain()))
					.willReturn(null);

			// when & then
			mockMvc.perform(post(url)
						   .content(body)
						   .contentType(MediaType.APPLICATION_JSON))
				   .andExpect(status().isBadRequest())
				   .andReturn();

			verify(surveyService, never()).createSurvey(any());
		}

		@Test
		@DisplayName("질문이 10개 이상이면 400 status와 error를 반환한다.")
		void throwExceptionWhenMaxExceededQuestions () throws Exception {
			// given
			List<CreateQuestionRequest> questions = IntStream.range(1, 12)
															 .mapToObj(i -> new CreateQuestionRequest(
																	 "질문 " + i,
																	 "질문 설명 " + i,
																	 "LONG_TEXT",
																	 false,
																	 i,
																	 List.of())
															 ).toList();
			CreateSurveyRequest request = new CreateSurveyRequest("설문 조사 1", "", questions);
			String body = objectMapper.writeValueAsString(request);

			given(surveyService.createSurvey(request.toDomain()))
					.willReturn(null);

			// when & then
			mockMvc.perform(post(url)
						   .content(body)
						   .contentType(MediaType.APPLICATION_JSON))
				   .andExpect(status().isBadRequest())
				   .andReturn();

			verify(surveyService, never()).createSurvey(any());
		}

		@Test
		@DisplayName("질문의 유형이 올바르지 않으면 400 status와 error를 반환한다.")
		void throwExceptionWhenWrongInputType () throws Exception {
			// given
			CreateQuestionRequest question = new CreateQuestionRequest(
					"질문 1",
					"질문 설명",
					"INVALID_TYPE",
					true,
					1,
					List.of()
			);
			CreateSurveyRequest request = new CreateSurveyRequest("설문 조사 1", "", List.of(question));
			String body = objectMapper.writeValueAsString(request);

			given(surveyService.createSurvey(SurveyFixtures.basicSurvey()))
					.willReturn(null);

			// when & then
			mockMvc.perform(post(url)
						   .content(body)
						   .contentType(MediaType.APPLICATION_JSON))
				   .andExpect(status().isBadRequest())
				   .andReturn();

			verify(surveyService, never()).createSurvey(any());
		}

		@Test
		@DisplayName("질문의 유형이 선택형일 경우 옵션이 존재하지 않으면 400 status와 error를 반환한다.")
		void throwExceptionWhenOptionsNotHaveForSelectTypeQuestion () {
			throw new NotImplementedTestException();
		}

		@Test
		@DisplayName("질문의 유형이 문자형일 경우 옵션이 존재하면 400 status와 error를 반환한다.")
		void throwExceptionWhenOptionsExistsToTextTypeQuestion () {
			throw new NotImplementedTestException();
		}

		@Test
		@DisplayName("설문 조사의 정보가 올바른 경우 200 stauts와 data를 반환한다.")
		void shouldCreateSurveyWithValidInfo () {
			throw new NotImplementedTestException();
		}
	}

	@DisplayName("설문 수정")
	@Nested
	class UpdateSurvey {
		@Test
		@DisplayName("설문이 존재하지 않으면 404 status와 error를 반환한다.")
		void throwExceptionWhenSurveyNotFound () {
			throw new NotImplementedTestException();
		}

		@Test
		@DisplayName("질문이 존재하지 않으면 400 status와 error를 반환 한다.")
		void throwExceptionWhenInSufficientQuestions () {
			throw new NotImplementedTestException();
		}

		@Test
		@DisplayName("질문이 10개 이상이면 400 status와 error를 반환한다.")
		void throwExceptionWhenMaxExceededQuestions () {
			throw new NotImplementedTestException();
		}

		@Test
		@DisplayName("질문의 유형이 올바르지 않으면 400 status와 error를 반환한다.")
		void throwExceptionWhenWrongInputType () {
			throw new NotImplementedTestException();
		}

		@Test
		@DisplayName("질문의 유형이 선택형일 경우 옵션이 존재하지 않으면 400 status와 error를 반환한다.")
		void throwExceptionWhenOptionsNotHaveForSelectTypeQuestion () {
			throw new NotImplementedTestException();
		}

		@Test
		@DisplayName("질문의 유형이 문자형일 경우 옵션이 존재하면 400 status와 error를 반환한다.")
		void throwExceptionWhenOptionsExistsToTextTypeQuestion () {
			throw new NotImplementedTestException();
		}

		@Test
		@DisplayName("수정 요청에 대한 정보가 올바른 경우 200 status와 data를 반환한다.")
		void shouldUpdateSurveyWithValidInfo () {
			throw new NotImplementedTestException();
		}
	}

	@DisplayName("응답 제출")
	@Nested
	class SubmitResponse {
		@Test
		@DisplayName("설문이 존재하지 않으면 404 status와 error를 반환한다.")
		void throwExceptionWhenSurveyNotFound () {
			throw new NotImplementedTestException();
		}

		@Test
		@DisplayName("필수 질문에 대한 응답이 없으면 400 status와 error를 반환한다.")
		void throwExceptionWhenAnswerIsNullForRequiredQuestion () {
			throw new NotImplementedTestException();
		}

		@Test
		@DisplayName("설문 조사에 등록된 필수 질문이 누락된 경우 400 status와 error를 반환한다.")
		void throwExceptionWhenExactlyAnswersForRequiredQuestion () {
			throw new NotImplementedTestException();
		}

		@Test
		@DisplayName("단일 선택 리스트형 질문에 응답이 2개면 400 status와 error를 반환한다.")
		void throwExceptionWhenNotMatchedAnswersForSingleSelectQuestion () {
			throw new NotImplementedTestException();
		}

		@Test
		@DisplayName("중복 응답을 제출한 경우 400 status와 error를 반환한다.")
		void throwExceptionWhenDuplicateResponse () {
			throw new NotImplementedTestException();
		}

		@Test
		@DisplayName("응답 정보가 올바른 경우 200 status와 data를 반환한다.")
		void shouldSumbitResponse () {
			throw new NotImplementedTestException();
		}
	}

	@DisplayName("응답 조회")
	@Nested
	class GetResponse {
		@Test
		@DisplayName("요청한 설문 조사의 응답이 없으면 404 status와 error를 반환한다.")
		void throwExceptionWhenResponseNotFoundBySurveyInfo () {
			throw new NotImplementedTestException();
		}

		@Test
		@DisplayName("설문 조사에 대한 응답이 존재하면 200 status와 data를 반환한다.")
		void shouldGetResponsesBySurveyInfo () {
			throw new NotImplementedTestException();
		}
	}
}