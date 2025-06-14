package com.INNER_CIRCLE_ICD4.innerCircle;

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

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ApplicationIntegrationTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @Test
    void fullFlow_SurveyAndResponseEndpoints() throws Exception {
        // 1. 설문 생성
        var surveyReq = Map.of(
                "title", "통합 테스트 설문",
                "description", "설명",
                "questions", List.of(
                        Map.of(
                                "title", "Q1",
                                "description", "D1",
                                "type", "SHORT",
                                "required", true,
                                "choices", List.of()
                        ),
                        Map.of(
                                "title", "Q2",
                                "description", "D2",
                                "type", "SINGLE_CHOICE",
                                "required", false,
                                "choices", List.of("A","B")
                        )
                )
        );
        String surveyJson = objectMapper.writeValueAsString(surveyReq);
        String surveyResp = mockMvc.perform(post("/surveys")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(surveyJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("통합 테스트 설문"))
                .andReturn().getResponse().getContentAsString();

        Map<?,?> surveyMap = objectMapper.readValue(surveyResp, Map.class);
        UUID surveyId = UUID.fromString((String) surveyMap.get("id"));

        // 2. 설문 조회
        mockMvc.perform(get("/surveys/" + surveyId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.questions.length()").value(2));

        // 3. 설문 수정 (제목 변경)
        var updateReq = Map.of(
                "title", "수정된 설문",
                "description", "수정된 설명",
                "questions", List.of()
        );
        mockMvc.perform(put("/surveys/" + surveyId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateReq)))
                .andExpect(status().isOk());

        // 4. 응답 제출
        UUID q1 = UUID.fromString((String) ((Map<?,?>)((List<?>)surveyMap.get("questions")).get(0)).get("id"));
        UUID q2 = UUID.fromString((String) ((Map<?,?>)((List<?>)surveyMap.get("questions")).get(1)).get("id"));
        List<String> choices = (List<String>) ((Map<?,?>)((List<?>)surveyMap.get("questions")).get(1)).get("choices");

        var respReq = Map.of(
                "surveyId", surveyId,
                "answers", List.of(
                        Map.of("questionId", q1, "textValue", "답변1", "selectedChoiceIds", null),
                        Map.of("questionId", q2, "textValue", null, "selectedChoiceIds", List.of(choices.get(0)))
                )
        );
        String respId = mockMvc.perform(post("/responses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(respReq)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString().replace("\"", "");

        // 5. 응답 개별 조회
        mockMvc.perform(get("/responses/" + respId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.answers.length()").value(2));

        // 6. 설문별 응답 목록 조회
        mockMvc.perform(get("/responses/survey/" + surveyId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }
}