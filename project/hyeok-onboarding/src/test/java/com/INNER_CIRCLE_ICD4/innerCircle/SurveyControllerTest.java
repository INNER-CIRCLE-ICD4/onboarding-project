package com.INNER_CIRCLE_ICD4.innerCircle;


import com.INNER_CIRCLE_ICD4.innerCircle.dto.ChoiceResponse;
import com.INNER_CIRCLE_ICD4.innerCircle.dto.QuestionResponse;
import com.INNER_CIRCLE_ICD4.innerCircle.dto.SurveyResponse;
import com.INNER_CIRCLE_ICD4.innerCircle.service.SurveyService;
import com.INNER_CIRCLE_ICD4.innerCircle.controller.SurveyController;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.BDDMockito.given;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SurveyController.class)
public class SurveyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SurveyService surveyService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void GET_All_Request() throws Exception {
        // Given
        UUID surveyId = UUID.randomUUID();
        SurveyResponse mockResponse = new SurveyResponse(
                surveyId,
                "설문 제목",
                "설문 설명",
                1,
                List.of(
                        new QuestionResponse(
                                UUID.randomUUID(),
                                "질문 제목",
                                "질문 설명",
                                com.INNER_CIRCLE_ICD4.innerCircle.domain.QuestionType.SINGLE_CHOICE,
                                true,
                                List.of(
                                        new ChoiceResponse(UUID.randomUUID(), "선택지1", 0),
                                        new ChoiceResponse(UUID.randomUUID(), "선택지2", 1)
                                )
                        )
                )
        );

        given(surveyService.findAll()).willReturn(List.of(mockResponse));

        // When & Then
        mockMvc.perform(get("/surveys"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("설문 제목"))
                .andExpect(jsonPath("$[0].questions[0].choices.length()").value(2));
    }

    @Test
    void GET_단일_설문_반환() throws Exception {
        // Given
        UUID surveyId = UUID.randomUUID();
        SurveyResponse mockResponse = new SurveyResponse(
                surveyId,
                "단일 설문 제목",
                "단일 설문 설명",
                1,
                List.of()
        );

        given(surveyService.findById(surveyId)).willReturn(mockResponse);

        // When & Then
        mockMvc.perform(get("/surveys/" + surveyId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("단일 설문 제목"));
    }
}
