package icd.onboarding.surveyproject.survey.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import icd.onboarding.surveyproject.common.NotImplementedTestException;
import icd.onboarding.surveyproject.survey.common.controller.GlobalExceptionHandler;
import icd.onboarding.surveyproject.survey.controller.consts.ErrorCodes;
import icd.onboarding.surveyproject.survey.controller.dto.*;
import icd.onboarding.surveyproject.survey.controller.exception.CommonSurveyHttpException;
import icd.onboarding.surveyproject.survey.fixtures.ResponseFixtures;
import icd.onboarding.surveyproject.survey.fixtures.SurveyFixtures;
import icd.onboarding.surveyproject.survey.service.SurveyService;
import icd.onboarding.surveyproject.survey.service.domain.Survey;
import icd.onboarding.surveyproject.survey.service.exception.InSufficientOptionException;
import icd.onboarding.surveyproject.survey.service.exception.SurveyNotFoundException;
import icd.onboarding.surveyproject.survey.service.exception.UnsupportedOptionException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
				   .andExpect(jsonPath("$.error.message").value(ErrorCodes.SURVEY_NOT_FOUND.message))
				   .andExpect(jsonPath("$.error.code").value(ErrorCodes.SURVEY_NOT_FOUND.name()));
		}

		@Test
		@DisplayName("정보가 올바른 경우 200 status와 data를 반환한다.")
		void shouldGetSurveys () throws Exception {
			// given
			Survey survey = SurveyFixtures.basicSurvey();
			SurveyResponse response = SurveyResponse.fromDomain(survey);

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
		final String url = "/api/v1/survey";

		@ParameterizedTest
		@NullAndEmptySource
		@DisplayName("설문 조사의 제목이 존재하지 않으면 400 status와 error를 반환한다.")
		void throwExceptionWhenSurveyNameIsNullAndEmpty (String title) throws Exception {
			// given
			CreateSurveyRequest request = new CreateSurveyRequest(title, "", List.of());

			given(surveyService.createSurvey(request.toDomain()))
					.willReturn(null);

			// when & then
			mockMvc.perform(post(url)
						   .content(toJson(request))
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

			given(surveyService.createSurvey(request.toDomain()))
					.willReturn(null);

			// when & then
			mockMvc.perform(post(url)
						   .content(toJson(request))
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

			given(surveyService.createSurvey(request.toDomain()))
					.willReturn(null);

			// when & then
			mockMvc.perform(post(url)
						   .content(toJson(request))
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

			given(surveyService.createSurvey(any()))
					.willReturn(null);

			// when & then
			mockMvc.perform(post(url)
						   .content(toJson(request))
						   .contentType(MediaType.APPLICATION_JSON))
				   .andExpect(status().isBadRequest())
				   .andReturn();

			verify(surveyService, never()).createSurvey(any());
		}

		@Test
		@DisplayName("질문의 유형이 선택형일 경우 옵션이 존재하지 않으면 400 status와 error를 반환한다.")
		void throwExceptionWhenOptionsNotHaveForSelectTypeQuestion () throws Exception {
			// given
			CreateQuestionRequest question = new CreateQuestionRequest(
					"질문 1",
					"질문 설명",
					"SINGLE_SELECT",
					true,
					1,
					List.of()
			);

			CreateSurveyRequest request = new CreateSurveyRequest(
					"설문 조사 1",
					"",
					List.of(question)
			);

			given(surveyService.createSurvey(any()))
					.willThrow(new InSufficientOptionException());

			// when & then
			mockMvc.perform(post(url)
						   .content(toJson(request))
						   .contentType(MediaType.APPLICATION_JSON))
				   .andExpect(status().isBadRequest())
				   .andExpect(jsonPath("$.error.message").value(ErrorCodes.INSUFFICIENT_OPTION.message))
				   .andExpect(jsonPath("$.error.code").value(ErrorCodes.INSUFFICIENT_OPTION.name()))
				   .andReturn();

			verify(surveyService, never()).createSurvey(any());
		}

		@Test
		@DisplayName("질문의 유형이 문자형일 경우 옵션이 존재하면 400 status와 error를 반환한다.")
		void throwExceptionWhenOptionsExistsToTextTypeQuestion () throws Exception {
			// given
			CreateQuestionRequest questionRequest = new CreateQuestionRequest(
					"질문 1",
					"질문 설명",
					"SHORT_TEXT",
					false,
					1,
					List.of(new CreateOptionRequest("옵션 1", 1))
			);
			CreateSurveyRequest request = new CreateSurveyRequest("설문 조사1", "설문 조사 설명", List.of(questionRequest));

			given(surveyService.createSurvey(any()))
					.willThrow(new UnsupportedOptionException());

			// when & then
			mockMvc.perform(post(url)
						   .content(toJson(request))
						   .contentType(MediaType.APPLICATION_JSON))
				   .andExpect(status().isBadRequest())
				   .andExpect(jsonPath("$.error.message").value(ErrorCodes.UNSUPPORTED_OPTION.message))
				   .andExpect(jsonPath("$.error.code").value(ErrorCodes.UNSUPPORTED_OPTION.name()))
				   .andReturn();

			verify(surveyService, never()).createSurvey(any());
		}

		@Test
		@DisplayName("설문 조사의 정보가 올바른 경우 200 stauts와 data를 반환한다.")
		void shouldCreateSurveyWithValidInfo () throws Exception {
			// given
			CreateQuestionRequest questionRequest = new CreateQuestionRequest(
					"질문 1",
					"질문 설명",
					"SHORT_TEXT",
					false,
					1,
					List.of()
			);
			CreateSurveyRequest request = new CreateSurveyRequest("설문 조사1", "설문 조사 설명", List.of(questionRequest));
			Survey survey = SurveyFixtures.basicSurvey();

			given(surveyService.createSurvey(any())).willReturn(survey);

			// when & then
			mockMvc.perform(post(url)
						   .content(toJson(request))
						   .contentType(MediaType.APPLICATION_JSON))
				   .andExpect(status().isCreated())
				   .andExpect(jsonPath("$.data.id").value(survey.getId().toString()));

			verify(surveyService, times(1)).createSurvey(any());
		}
	}

	@DisplayName("설문 수정")
	@Nested
	class UpdateSurvey {
		final String url = "/api/v1/survey";
		final UUID surveyId = UUID.randomUUID();

		@Test
		@DisplayName("설문이 존재하지 않으면 404 status와 error를 반환한다.")
		void throwExceptionWhenSurveyNotFound () throws Exception {
			UpdateQuestionRequest questionRequest = new UpdateQuestionRequest(
					"수정 질문 1",
					"",
					"SHORT_TEXT",
					1,
					false,
					List.of()
			);
			UpdateSurveyRequest request = new UpdateSurveyRequest(surveyId, 1, "수정된 설문", "수정된 설명", List.of(questionRequest));

			given(surveyService.updateSurvey(any()))
					.willThrow(new SurveyNotFoundException());

			mockMvc.perform(put(url)
						   .content(toJson(request))
						   .contentType(MediaType.APPLICATION_JSON))
				   .andExpect(status().isNotFound())
				   .andExpect(jsonPath("$.error.code").value(ErrorCodes.SURVEY_NOT_FOUND.name()))
				   .andExpect(jsonPath("$.error.message").value(ErrorCodes.SURVEY_NOT_FOUND.message));

			verify(surveyService, times(1)).updateSurvey(any());
		}

		@Test
		@DisplayName("질문이 존재하지 않으면 400 status와 error를 반환 한다.")
		void throwExceptionWhenInSufficientQuestions () throws Exception {
			UpdateSurveyRequest request = new UpdateSurveyRequest(surveyId, 1, "수정된 설문", "수정된 설명", List.of());

			mockMvc.perform(put(url)
						   .content(toJson(request))
						   .contentType(MediaType.APPLICATION_JSON))
				   .andExpect(status().isBadRequest());

			verify(surveyService, never()).updateSurvey(any());
		}

		@Test
		@DisplayName("질문이 10개 이상이면 400 status와 error를 반환한다.")
		void throwExceptionWhenMaxExceededQuestions () throws Exception {
			List<UpdateQuestionRequest> questions = IntStream.rangeClosed(1, 11)
															 .mapToObj(i -> new UpdateQuestionRequest(
																	 "질문 " + i,
																	 "설명 " + i,
																	 "SHORT_TEXT",
																	 i,
																	 false,
																	 List.of()
															 )).toList();

			UpdateSurveyRequest request = new UpdateSurveyRequest(surveyId, 1, "설문 수정", "내용", questions);

			mockMvc.perform(put(url)
						   .content(toJson(request))
						   .contentType(MediaType.APPLICATION_JSON))
				   .andExpect(status().isBadRequest());

			verify(surveyService, never()).updateSurvey(any());
		}

		@Test
		@DisplayName("질문의 유형이 올바르지 않으면 400 status와 error를 반환한다.")
		void throwExceptionWhenWrongInputType () throws Exception {
			UpdateQuestionRequest question = new UpdateQuestionRequest(
					"질문", "설명", "INVALID_TYPE", 1, true, List.of()
			);
			UpdateSurveyRequest request = new UpdateSurveyRequest(surveyId, 1, "설문 수정", "내용", List.of(question));

			mockMvc.perform(put(url)
						   .content(toJson(request))
						   .contentType(MediaType.APPLICATION_JSON))
				   .andExpect(status().isBadRequest());

			verify(surveyService, never()).updateSurvey(any());
		}

		@Test
		@DisplayName("질문의 유형이 선택형일 경우 옵션이 존재하지 않으면 400 status와 error를 반환한다.")
		void throwExceptionWhenOptionsNotHaveForSelectTypeQuestion () throws Exception {
			UpdateQuestionRequest question = new UpdateQuestionRequest(
					"질문", "설명", "SINGLE_SELECT", 1, true, List.of()
			);
			UpdateSurveyRequest request = new UpdateSurveyRequest(surveyId, 1, "설문 수정", "내용", List.of(question));

			given(surveyService.updateSurvey(any()))
					.willThrow(new InSufficientOptionException());

			mockMvc.perform(put(url)
						   .content(toJson(request))
						   .contentType(MediaType.APPLICATION_JSON))
				   .andExpect(status().isBadRequest())
				   .andExpect(jsonPath("$.error.code").value(ErrorCodes.INSUFFICIENT_OPTION.name()))
				   .andExpect(jsonPath("$.error.message").value(ErrorCodes.INSUFFICIENT_OPTION.message));

			verify(surveyService, never()).updateSurvey(any());
		}

		@Test
		@DisplayName("질문의 유형이 문자형일 경우 옵션이 존재하면 400 status와 error를 반환한다.")
		void throwExceptionWhenOptionsExistsToTextTypeQuestion () throws Exception {
			UpdateQuestionRequest question = new UpdateQuestionRequest(
					"질문",
					"설명",
					"SHORT_TEXT",
					1,
					false,
					List.of(new UpdateOptionRequest("옵션 1", 1))
			);
			UpdateSurveyRequest request = new UpdateSurveyRequest(surveyId, 1, "설문 수정", "내용", List.of(question));

			given(surveyService.updateSurvey(any()))
					.willThrow(new UnsupportedOptionException());

			mockMvc.perform(put(url)
						   .content(toJson(request))
						   .contentType(MediaType.APPLICATION_JSON))
				   .andExpect(status().isBadRequest())
				   .andExpect(jsonPath("$.error.code").value(ErrorCodes.UNSUPPORTED_OPTION.name()))
				   .andExpect(jsonPath("$.error.message").value(ErrorCodes.UNSUPPORTED_OPTION.message));

			verify(surveyService, never()).updateSurvey(any());
		}

		@Test
		@DisplayName("수정 요청에 대한 정보가 올바른 경우 200 status와 data를 반환한다.")
		void shouldUpdateSurveyWithValidInfo () throws Exception {
			UpdateQuestionRequest question = new UpdateQuestionRequest(
					"질문", "설명", "SHORT_TEXT", 1, false, List.of()
			);
			UpdateSurveyRequest request = new UpdateSurveyRequest(surveyId, 1, "설문 수정", "내용", List.of(question));
			Survey updatedSurvey = SurveyFixtures.updatedSurvey(request.id(), request.version());

			given(surveyService.updateSurvey(any())).willReturn(updatedSurvey);

			mockMvc.perform(put(url)
						   .content(toJson(request))
						   .contentType(MediaType.APPLICATION_JSON))
				   .andExpect(status().isOk())
				   .andExpect(jsonPath("$.data.id").value(request.id().toString()))
				   .andExpect(jsonPath("$.data.version").value(request.version() + 1));

			verify(surveyService, times(1)).updateSurvey(any());
		}
	}

	@DisplayName("응답 제출")
	@Nested
	class SubmitResponse {
		final String url = "/api/v1/survey";
		final UUID surveyId = UUID.randomUUID();
		final int version = 1;

		@Test
		@DisplayName("설문이 존재하지 않으면 404 status와 error를 반환한다.")
		void throwExceptionWhenSurveyNotFound () throws Exception {
			SubmitResponseRequest request = ResponseFixtures.validSubmitRequest();

			given(surveyService.submitResponse(any()))
					.willThrow(new SurveyNotFoundException());

			mockMvc.perform(post(url + "/{surveyId}/version/{version}/response", surveyId, version)
						   .content(toJson(request))
						   .contentType(MediaType.APPLICATION_JSON))
				   .andExpect(status().isNotFound())
				   .andExpect(jsonPath("$.error.code").value(ErrorCodes.SURVEY_NOT_FOUND.name()))
				   .andExpect(jsonPath("$.error.message").value(ErrorCodes.SURVEY_NOT_FOUND.message));

			verify(surveyService, times(1)).submitResponse(any());
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

	private String toJson (Object value) throws JsonProcessingException {
		return objectMapper.writeValueAsString(value);
	}
}