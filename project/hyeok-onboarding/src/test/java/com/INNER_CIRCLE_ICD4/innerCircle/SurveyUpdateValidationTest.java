package com.INNER_CIRCLE_ICD4.innerCircle;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class SurveyUpdateValidationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    private UUID createSurvey() throws Exception {
        Map<String, Object> req = Map.of(
                "title", "초기 설문",
                "description", "초기 설명",
                "questions", List.of(
                        Map.of(
                                "title", "Q1",
                                "description", "D1",
                                "type", "SHORT",
                                "required", true,
                                "choices", List.of()
                        )
                )
        );
        String body = mockMvc.perform(post("/surveys")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Map<String, Object> resp = objectMapper.readValue(
                body, new TypeReference<>() {}
        );
        return UUID.fromString((String) resp.get("id"));
    }

    @Test
    void whenUpdateSurveyWithoutTitle_thenBadRequest() throws Exception {
        UUID surveyId = createSurvey();

        Map<String, Object> invalidUpdate = Map.of(
                // title 누락
                "description", "업데이트 설명",
                "questions", List.of(
                        Map.of(
                                "title", "Q1",
                                "description", "D1",
                                "type", "SHORT",
                                "required", true,
                                "choices", List.of()
                        )
                )
        );

        mockMvc.perform(put("/surveys/" + surveyId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidUpdate)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors[?(@.field=='title')].message")
                        .value("설문 제목은 필수입니다."));
    }

    @Test
    void whenUpdateSurveyWithEmptyQuestions_thenBadRequest() throws Exception {
        UUID surveyId = createSurvey();

        Map<String, Object> invalidUpdate = Map.of(
                "title", "업데이트 설문",
                "description", "업데이트 설명",
                "questions", List.of() // 빈 리스트
        );

        mockMvc.perform(put("/surveys/" + surveyId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidUpdate)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors[?(@.field=='questions')].message")
                        .value("질문은 1개 이상, 최대 10개까지 허용됩니다."));
    }
}
