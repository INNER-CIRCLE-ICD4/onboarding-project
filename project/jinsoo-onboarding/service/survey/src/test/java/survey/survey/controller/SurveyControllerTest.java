package survey.survey.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import survey.survey.config.ApplicationException;
import survey.survey.controller.request.SurveyFormCreateRequest;
import survey.survey.controller.request.SurveyFormCreateRequest.QuestionCreateRequest;
import survey.survey.entity.surveyquestion.CheckCandidate;
import survey.survey.entity.surveyquestion.InputType;
import survey.survey.service.SurveyFormService;
import survey.survey.service.response.SurveyFormResponse;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static survey.survey.config.ErrorType.MAXIMUM_QUESTION;
import static survey.survey.config.ErrorType.MINIMUM_QUESTION;

@WebMvcTest(SurveyController.class)
@DisplayName("SurveyController 테스트")
class SurveyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private SurveyFormService surveyFormService;

    @Nested
    @DisplayName("설문지 생성 API")
    class CreateSurveyForm {

        private final String API_URL = "/api/v1/survey";

        @Test
        @DisplayName("유효한 요청으로 설문지를 성공적으로 생성한다")
        void shouldCreateSurveyFormSuccessfully() throws Exception {
            // given
            SurveyFormCreateRequest request = givenValidSurveyFormRequest();
            SurveyFormResponse response = givenSurveyFormResponse();
            given(surveyFormService.create(any(SurveyFormCreateRequest.class))).willReturn(response);

            // when & then
            mockMvc.perform(post(API_URL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.surveyFormId", is(1234567890)))
                    .andExpect(jsonPath("$.version", is(1)))
                    .andExpect(jsonPath("$.surveyId", is(12345)))
                    .andExpect(jsonPath("$.title", is("테스트 설문지")))
                    .andExpect(jsonPath("$.description", is("테스트용 설문지입니다")))
                    .andExpect(jsonPath("$.questions", hasSize(2)))
                    .andExpect(jsonPath("$.questions[0].name", is("텍스트 질문")))
                    .andExpect(jsonPath("$.questions[1].name", is("선택형 질문")))
                    .andExpect(jsonPath("$.questions[1].candidates", hasSize(2)));

            verify(surveyFormService).create(any(SurveyFormCreateRequest.class));
        }

        @Test
        @DisplayName("제목이 없는 요청은 유효성 검증에 실패한다")
        void shouldFailValidationWhenTitleIsEmpty() throws Exception {
            // given
            SurveyFormCreateRequest request = givenRequestWithEmptyTitle();

            // when & then
            mockMvc.perform(post(API_URL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message", containsString("설문조사 이름은 필수입니다")));
        }

        @Test
        @DisplayName("질문 목록이 없는 요청은 유효성 검증에 실패한다")
        void shouldFailValidationWhenQuestionListIsNull() throws Exception {
            // given
            SurveyFormCreateRequest request = givenRequestWithNullQuestionList();

            // when & then
            mockMvc.perform(post(API_URL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message", containsString("질문 목록은 필수입니다")));
        }

        @Test
        @DisplayName("질문 이름이 없는 요청은 유효성 검증에 실패한다")
        void shouldFailValidationWhenQuestionNameIsEmpty() throws Exception {
            // given
            SurveyFormCreateRequest request = givenRequestWithEmptyQuestionName();

            // when & then
            mockMvc.perform(post(API_URL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message", containsString("질문 이름은 필수입니다")));
        }

        @Test
        @DisplayName("질문 개수가 최소 개수 미만이면 서비스에서 예외가 발생한다")
        void shouldReturnErrorWhenQuestionCountBelowMinimum() throws Exception {
            // given
            SurveyFormCreateRequest request = givenValidSurveyFormRequest();
            given(surveyFormService.create(any(SurveyFormCreateRequest.class)))
                    .willThrow(new ApplicationException(MINIMUM_QUESTION));

            // when & then
            mockMvc.perform(post(API_URL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code", is(MINIMUM_QUESTION.getStatus())));
        }

        @Test
        @DisplayName("질문 개수가 최대 개수를 초과하면 서비스에서 예외가 발생한다")
        void shouldReturnErrorWhenQuestionCountAboveMaximum() throws Exception {
            // given
            SurveyFormCreateRequest request = givenValidSurveyFormRequest();
            given(surveyFormService.create(any(SurveyFormCreateRequest.class)))
                    .willThrow(new ApplicationException(MAXIMUM_QUESTION));

            // when & then
            mockMvc.perform(post(API_URL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code", is(MAXIMUM_QUESTION.getStatus())));
        }

        @Test
        @DisplayName("유효하지 않은 JSON 형식은 요청 파싱에 실패한다")
        void shouldFailWhenJsonIsInvalid() throws Exception {
            // given
            String invalidJson = "{\"title\": \"테스트 설문지\", invalid json}";

            // when & then
            mockMvc.perform(post(API_URL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(invalidJson))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("선택형 질문의 선택지가 비어있으면 유효성 검증에 실패한다")
        void shouldFailValidationWhenChoiceQuestionHasEmptyCandidateName() throws Exception {
            // given
            SurveyFormCreateRequest request = givenRequestWithEmptyCandidateName();

            // when & then
            mockMvc.perform(post(API_URL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message", containsString("선택 항목 값은 필수입니다")));
        }

        // Test Data Builders
        private SurveyFormCreateRequest givenValidSurveyFormRequest() {
            return new SurveyFormCreateRequest(
                    12345L,
                    "테스트 설문지",
                    "테스트용 설문지입니다",
                    Arrays.asList(
                            new QuestionCreateRequest(
                                    "텍스트 질문", 0, "텍스트 입력 질문입니다", InputType.SHORT_ANSWER, false, null
                            ),
                            new QuestionCreateRequest(
                                    "선택형 질문", 1, "선택지가 있는 질문입니다", InputType.MULTIPLE_CHOICE, true,
                                    Arrays.asList(
                                            new QuestionCreateRequest.CandidateCreateRequest(0, "선택지 1"),
                                            new QuestionCreateRequest.CandidateCreateRequest(1, "선택지 2")
                                    )
                            )
                    )
            );
        }

        private SurveyFormCreateRequest givenRequestWithEmptyTitle() {
            return new SurveyFormCreateRequest(
                    12345L,
                    "", // 빈 제목
                    "테스트용 설문지입니다",
                    Collections.singletonList(
                            new QuestionCreateRequest(
                                    "텍스트 질문", 0, "텍스트 입력 질문입니다", InputType.SHORT_ANSWER, false, null
                            )
                    )
            );
        }

        private SurveyFormCreateRequest givenRequestWithNullQuestionList() {
            return new SurveyFormCreateRequest(
                    12345L,
                    "테스트 설문지",
                    "테스트용 설문지입니다",
                    null // 질문 목록 없음
            );
        }

        private SurveyFormCreateRequest givenRequestWithEmptyQuestionName() {
            return new SurveyFormCreateRequest(
                    12345L,
                    "테스트 설문지",
                    "테스트용 설문지입니다",
                    Collections.singletonList(
                            new QuestionCreateRequest(
                                    "", // 빈 질문 이름
                                    0,
                                    "텍스트 입력 질문입니다",
                                    InputType.SHORT_ANSWER,
                                    false,
                                    null
                            )
                    )
            );
        }

        private SurveyFormCreateRequest givenRequestWithEmptyCandidateName() {
            return new SurveyFormCreateRequest(
                    12345L,
                    "테스트 설문지",
                    "테스트용 설문지입니다",
                    Collections.singletonList(
                            new QuestionCreateRequest(
                                    "선택형 질문",
                                    0,
                                    "선택지가 있는 질문입니다",
                                    InputType.MULTIPLE_CHOICE,
                                    true,
                                    Collections.singletonList(
                                            new QuestionCreateRequest.CandidateCreateRequest(0, "") // 빈 선택지 이름
                                    )
                            )
                    )
            );
        }

        private SurveyFormResponse givenSurveyFormResponse() {

            SurveyFormResponse response = SurveyFormResponse.of(
                    1234567890L, // surveyFormId
                    1L, // version
                    12345L, // surveyId
                    "테스트 설문지", // title
                    "테스트용 설문지입니다", // description
                    Arrays.asList( // questions
                            SurveyFormResponse.QuestionResponse.of(
                                    1111111111L, // questionId
                                    0, // questionIndex
                                    "텍스트 질문", // name
                                    "텍스트 입력 질문입니다", // description
                                    InputType.SHORT_ANSWER, // inputType
                                    false, // required
                                    Collections.emptyList() // candidates
                            ),
                            SurveyFormResponse.QuestionResponse.of(
                                    2222222222L, // questionId
                                    1, // questionIndex
                                    "선택형 질문", // name
                                    "선택지가 있는 질문입니다", // description
                                    InputType.MULTIPLE_CHOICE, // inputType
                                    true, // required
                                    Arrays.asList( // candidates
                                            CheckCandidate.of(0, "선택지 1"),
                                            CheckCandidate.of(1, "선택지 2")
                                    )
                            )
                    ),
                    LocalDateTime.now(), // createdAt
                    LocalDateTime.now() // modifiedAt
            );

            return response;
        }
    }
}