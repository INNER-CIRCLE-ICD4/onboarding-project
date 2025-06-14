
package com.INNER_CIRCLE_ICD4.innerCircle.controller;

import com.INNER_CIRCLE_ICD4.innerCircle.domain.QuestionType;
import com.INNER_CIRCLE_ICD4.innerCircle.dto.*;
import com.INNER_CIRCLE_ICD4.innerCircle.service.SurveyService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SurveyController.class)
class SurveyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SurveyService surveyService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("설문 생성 성공 시 201 Created + Location 헤더 반환")
    void createSurvey_success() throws Exception {
        // given
        UUID id = UUID.randomUUID();
        // 최소 1개의 질문을 포함해야 validation 통과
        QuestionRequest qr = new QuestionRequest(
                "Q1",
                "질문 설명",
                QuestionType.SINGLE_CHOICE,
                true,
                List.of()
        );
        SurveyRequest request = new SurveyRequest(
                "제목",
                "설명",
                List.of(qr)
        );
        SurveyResponse response = new SurveyResponse(
                id,
                "제목",
                "설명",
                1,
                List.of()
        );

        when(surveyService.createSurvey(ArgumentMatchers.any(SurveyRequest.class)))
                .thenReturn(response);

        // when & then
        mockMvc.perform(post("/surveys")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/surveys/" + id))
                .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }

    @Test
    @DisplayName("설문 수정 시 validation 실패: 빈 제목")
    void updateSurvey_validationFail_emptyTitle() throws Exception {
        // 빈 제목, 빈 질문 리스트 → @NotBlank, @NotEmpty 위반
        SurveyUpdateRequest badRequest = new SurveyUpdateRequest(
                "",
                "설명",
                List.of()
        );

        mockMvc.perform(put("/surveys/" + UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(badRequest))
                )
                .andExpect(status().isBadRequest());
    }
}
