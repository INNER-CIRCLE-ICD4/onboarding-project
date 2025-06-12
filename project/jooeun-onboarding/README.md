package com.innercircle.survey.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.innercircle.survey.api.dto.request.CreateQuestionRequest;
import com.innercircle.survey.api.dto.request.CreateSurveyRequest;
import com.innercircle.survey.api.service.SurveyService;
import com.innercircle.survey.common.domain.QuestionType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * 설문조사 컨트롤러 단위 테스트
 */
@WebMvcTest(SurveyController.class)
@DisplayName("설문조사 컨트롤러 테스트")
class SurveyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SurveyService surveyService;

    @Test
    @DisplayName("올바른 설문조사 생성 요청 시 201 Created 응답")
    void 올바른_설문조사_생성_요청_성공() throws Exception {
        // Given
        CreateSurveyRequest request = new CreateSurveyRequest(
                "2024년 고객 만족도 조사",
                "고객 서비스 개선을 위한 만족도 조사입니다.",
                List.of(
                        new CreateQuestionRequest(
                                "전반적인 서비스에 만족하십니까?",
                                "서비스 전반에 대한 만족도를 평가해 주세요.",
                                QuestionType.SINGLE_CHOICE,
                                true,
                                List.of("매우 불만족", "불만족", "보통", "만족", "매우 만족")
                        ),
                        new CreateQuestionRequest(
                                "추가 의견이 있으시면 자유롭게 작성해 주세요.",
                                "더 나은 서비스를 위한 소중한 의견을 들려주세요.",
                                QuestionType.LONG_TEXT,
                                false,
                                null
                        )
                )
        );

        // When & Then
        mockMvc.perform(post("/api/surveys")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("제목이 없는 설문조사 생성 요청 시 400 Bad Request 응답")
    void 제목_없는_설문조사_생성_요청_실패() throws Exception {
        // Given
        CreateSurveyRequest request = new CreateSurveyRequest(
                "", // 빈 제목
                "설명",
                List.of(
                        new CreateQuestionRequest(
                                "질문 제목",
                                "질문 설명",
                                QuestionType.SHORT_TEXT,
                                true,
                                null
                        )
                )
        );

        // When & Then
        mockMvc.perform(post("/api/surveys")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("VALIDATION_FAILED"));
    }

    @Test
    @DisplayName("질문이 없는 설문조사 생성 요청 시 400 Bad Request 응답")
    void 질문_없는_설문조사_생성_요청_실패() throws Exception {
        // Given
        CreateSurveyRequest request = new CreateSurveyRequest(
                "설문조사 제목",
                "설문조사 설명",
                List.of() // 빈 질문 리스트
        );

        // When & Then
        mockMvc.perform(post("/api/surveys")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("VALIDATION_FAILED"));
    }

    @Test
    @DisplayName("질문 개수가 10개를 초과하는 설문조사 생성 요청 시 400 Bad Request 응답")
    void 질문_개수_초과_설문조사_생성_요청_실패() throws Exception {
        // Given - 11개의 질문 생성
        List<CreateQuestionRequest> questions = List.of(
                new CreateQuestionRequest("질문1", null, QuestionType.SHORT_TEXT, false, null),
                new CreateQuestionRequest("질문2", null, QuestionType.SHORT_TEXT, false, null),
                new CreateQuestionRequest("질문3", null, QuestionType.SHORT_TEXT, false, null),
                new CreateQuestionRequest("질문4", null, QuestionType.SHORT_TEXT, false, null),
                new CreateQuestionRequest("질문5", null, QuestionType.SHORT_TEXT, false, null),
                new CreateQuestionRequest("질문6", null, QuestionType.SHORT_TEXT, false, null),
                new CreateQuestionRequest("질문7", null, QuestionType.SHORT_TEXT, false, null),
                new CreateQuestionRequest("질문8", null, QuestionType.SHORT_TEXT, false, null),
                new CreateQuestionRequest("질문9", null, QuestionType.SHORT_TEXT, false, null),
                new CreateQuestionRequest("질문10", null, QuestionType.SHORT_TEXT, false, null),
                new CreateQuestionRequest("질문11", null, QuestionType.SHORT_TEXT, false, null)
        );

        CreateSurveyRequest request = new CreateSurveyRequest(
                "설문조사 제목",
                "설문조사 설명",
                questions
        );

        // When & Then
        mockMvc.perform(post("/api/surveys")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("VALIDATION_FAILED"));
    }

    @Test
    @DisplayName("선택형 질문에 옵션이 없는 설문조사 생성 요청 시 잘못된 요청으로 처리")
    void 선택형_질문_옵션_없는_요청_실패() throws Exception {
        // Given
        CreateSurveyRequest request = new CreateSurveyRequest(
                "설문조사 제목",
                "설문조사 설명",
                List.of(
                        new CreateQuestionRequest(
                                "선택 질문",
                                "선택해주세요",
                                QuestionType.SINGLE_CHOICE,
                                true,
                                null // 선택형 질문인데 옵션이 없음
                        )
                )
        );

        // When & Then
        mockMvc.perform(post("/api/surveys")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}
