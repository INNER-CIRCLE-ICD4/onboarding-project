// SurveyControllerIntegrationTest.java
package com.INNER_CIRCLE_ICD4.innerCircle;

import com.INNER_CIRCLE_ICD4.innerCircle.domain.QuestionType;
import com.INNER_CIRCLE_ICD4.innerCircle.dto.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class SurveyControllerIntegrationTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    private UUID surveyId;

    @BeforeEach
    void setup() throws Exception {
        SurveyRequest createReq = new SurveyRequest(
                "원본 제목",
                "원본 설명",
                List.of(
                        new QuestionRequest("Q1", "D1", QuestionType.SHORT, true, null),
                        new QuestionRequest("Q2", "D2", QuestionType.SINGLE_CHOICE, false, List.of("A", "B"))
                )
        );

        String respJson = mockMvc.perform(post("/surveys")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createReq)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        SurveyResponse created = objectMapper.readValue(respJson, SurveyResponse.class);
        surveyId = created.id();
    }

    @Test
    void 설문_수정_통합_테스트() throws Exception {
        SurveyUpdateRequest updateReq = new SurveyUpdateRequest(
                "수정된 제목",
                "수정된 설명",
                List.of(
                        new QuestionUpdateRequest(
                                null,
                                "새 질문",
                                "새 설명",
                                QuestionType.LONG,
                                true,
                                null
                        )
                )
        );

        mockMvc.perform(put("/surveys/" + surveyId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateReq)))
                .andExpect(status().isOk());

        mockMvc.perform(get("/surveys/" + surveyId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("수정된 제목"))
                .andExpect(jsonPath("$.description").value("수정된 설명"))
                .andExpect(jsonPath("$.questions[0].title").value("새 질문"));
    }

    @Test
    void 존재하지않는_설문ID_수정시_404반환() throws Exception {
        SurveyUpdateRequest updateReq = new SurveyUpdateRequest(
                "아무 제목",
                "아무 설명",
                List.of(
                        new QuestionUpdateRequest(
                                null,
                                "질문",
                                "설명",
                                QuestionType.SHORT,
                                false,
                                null
                        )
                )
        );

        mockMvc.perform(put("/surveys/" + UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateReq)))
                .andExpect(status().isNotFound());
    }
}
