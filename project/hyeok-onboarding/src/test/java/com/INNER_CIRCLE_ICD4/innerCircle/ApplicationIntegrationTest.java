package com.INNER_CIRCLE_ICD4.innerCircle;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@SuppressWarnings("unchecked")
public class ApplicationIntegrationTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @Test
    void fullFlow_SurveyAndResponseEndpoints() throws Exception {
        // 1. 설문 생성
        Map<String, Object> surveyReq = Map.of(
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
        String surveyRespBody = mockMvc.perform(post("/surveys")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(surveyReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("통합 테스트 설문"))
                .andExpect(jsonPath("$.questions.length()").value(2))
                .andReturn().getResponse().getContentAsString();

        Map<String,Object> surveyMap = objectMapper.readValue(
                surveyRespBody, new TypeReference<>() {});
        UUID surveyId = UUID.fromString((String) surveyMap.get("id"));

        // 2. 설문 단건 조회
        mockMvc.perform(get("/surveys/" + surveyId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.questions.length()").value(2));

        // 3. 설문 수정 (빈 questions → 질문 유지) 검증
        Map<String,Object> updateReq = Map.of(
                "title", "수정된 설문",
                "description", "수정된 설명",
                "questions", List.of()
        );
        String updateRespBody = mockMvc.perform(put("/surveys/" + surveyId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("수정된 설문"))
                .andExpect(jsonPath("$.description").value("수정된 설명"))
                .andExpect(jsonPath("$.questions.length()").value(2))
                .andReturn().getResponse().getContentAsString();

        Map<String,Object> updatedSurveyMap = objectMapper.readValue(
                updateRespBody, new TypeReference<>() {});

        // 4. 응답 제출: 질문 ID & choice ID 꺼내기
        List<Map<String,Object>> updatedQs =
                (List<Map<String,Object>>) updatedSurveyMap.get("questions");
        UUID q1 = UUID.fromString((String) updatedQs.get(0).get("id"));
        UUID q2 = UUID.fromString((String) updatedQs.get(1).get("id"));
        List<Map<String,Object>> choiceMaps =
                (List<Map<String,Object>>) updatedQs.get(1).get("choices");
        String choiceId = (String) choiceMaps.get(0).get("id");

        Map<String,Object> answer1 = new HashMap<>();
        answer1.put("questionId", q1);
        answer1.put("textValue", "답변1");
        answer1.put("selectedChoiceIds", null);

        Map<String,Object> answer2 = new HashMap<>();
        answer2.put("questionId", q2);
        answer2.put("textValue", null);
        answer2.put("selectedChoiceIds", List.of(choiceId));

        Map<String,Object> respReq = new HashMap<>();
        respReq.put("surveyId", surveyId);
        respReq.put("answers", List.of(answer1, answer2));

        // ↓ 여기만 변경: Map으로 파싱하지 않고, String으로 곧바로 읽습니다
        String respRespBody = mockMvc.perform(post("/responses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(respReq)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        String respId = objectMapper.readValue(respRespBody, String.class);

        // 5. 응답 단건 조회
        mockMvc.perform(get("/responses/" + respId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.answers.length()").value(2));

        // 6. 설문별 응답 목록 조회
        mockMvc.perform(get("/responses/survey/" + surveyId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }
}
