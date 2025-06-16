package com.INNER_CIRCLE_ICD4.innerCircle;

import com.INNER_CIRCLE_ICD4.innerCircle.domain.QuestionType;
import com.INNER_CIRCLE_ICD4.innerCircle.dto.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ResponseControllerIntegrationTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @Test
    void 존재하지_않는_설문에_응답하면_404_에러를_반환한다() throws Exception {
        ResponseRequest invalidReq = new ResponseRequest(
                UUID.randomUUID(), // 존재하지 않는 설문 ID
                List.of(new AnswerRequest(UUID.randomUUID(), "내용", null))
        );

        mockMvc.perform(post("/responses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidReq)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").exists());

    }

    @Test
    void 응답_개수_불일치_시_400_에러를_반환한다() throws Exception {
        // 설문 생성
        SurveyRequest createReq = new SurveyRequest(
                "테스트 설문",
                "설명",
                List.of(new QuestionRequest("Q1", "D1", QuestionType.SHORT, true, null))
        );

        String resp = mockMvc.perform(post("/surveys")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createReq)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        SurveyResponse created = objectMapper.readValue(resp, SurveyResponse.class);

        // 응답 개수 불일치
        ResponseRequest badAnswerReq = new ResponseRequest(
                created.id(),
                List.of(
                        new AnswerRequest(created.questions().get(0).id(), "응답1", null),
                        new AnswerRequest(UUID.randomUUID(), "응답2", null)
                )
        );

        mockMvc.perform(post("/responses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(badAnswerReq)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("응답 개수가 설문 질문 개수와 일치하지 않습니다."));
    }

    @Test
    void 설문_응답을_조회하면_응답_리스트를_반환한다() throws Exception {
        // 설문 생성
        SurveyRequest surveyRequest = new SurveyRequest(
                "조회 테스트 설문",
                "조회용 설명",
                List.of(new QuestionRequest("Q1", "D1", QuestionType.SHORT, true, null))
        );

        String surveyResp = mockMvc.perform(post("/surveys")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(surveyRequest)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        SurveyResponse createdSurvey = objectMapper.readValue(surveyResp, SurveyResponse.class);

        // 설문 응답 제출
        ResponseRequest responseRequest = new ResponseRequest(
                createdSurvey.id(),
                List.of(new AnswerRequest(createdSurvey.questions().get(0).id(), "응답 텍스트", null))
        );

        mockMvc.perform(post("/responses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(responseRequest)))
                .andExpect(status().isCreated());

        // 설문 응답 조회
        mockMvc.perform(get("/responses")
                        .param("surveyId", createdSurvey.id().toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].surveyId").value(createdSurvey.id().toString()))
                .andExpect(jsonPath("$[0].answers[0].questionId").value(createdSurvey.questions().get(0).id().toString()))
                .andExpect(jsonPath("$[0].answers[0].text").value("응답 텍스트"));
    }

}
