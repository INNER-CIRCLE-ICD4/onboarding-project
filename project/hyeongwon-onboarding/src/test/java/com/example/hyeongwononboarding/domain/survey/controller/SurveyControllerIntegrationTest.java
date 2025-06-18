package com.example.hyeongwononboarding.domain.survey.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SurveyControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private Map<String, Object> getSurveyCreateRequest() {
        return Map.of(
                "title", "고객 만족도 조사",
                "description", "서비스 개선을 위한 고객 만족도 조사입니다.",
                "questions", List.of(
                        Map.of(
                                "name", "이름",
                                "description", "응답자의 이름을 입력해주세요",
                                "inputType", "SHORT_TEXT",
                                "isRequired", true,
                                "order", 1
                        ),
                        Map.of(
                                "name", "만족도",
                                "description", "전반적인 서비스 만족도를 선택해주세요",
                                "inputType", "SINGLE_CHOICE",
                                "isRequired", true,
                                "options", List.of("매우 만족", "만족", "보통", "불만족", "매우 불만족"),
                                "order", 2
                        ),
                        Map.of(
                                "name", "개선사항",
                                "description", "개선이 필요한 부분을 자유롭게 작성해주세요",
                                "inputType", "LONG_TEXT",
                                "isRequired", false,
                                "order", 3
                        )
                )
        );
    }

    @Test
    @DisplayName("설문조사 생성 API - 정상 동작")
    void createSurvey_success() throws Exception {
        mockMvc.perform(post("/api/v1/surveys")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(getSurveyCreateRequest())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.title").value("고객 만족도 조사"))
                .andExpect(jsonPath("$.data.questions[0].name").value("이름"))
                .andExpect(jsonPath("$.data.questions[1].options[0].text").value("매우 만족"));
    }

    @Test
    @DisplayName("설문 생성 후 단일 설문 조회 API - 정상 동작")
    void createAndGetSurvey_success() throws Exception {
        // 설문 생성
        String createResponse = mockMvc.perform(post("/api/v1/surveys")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(getSurveyCreateRequest())))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.success").value(true))
            .andReturn().getResponse().getContentAsString();

        JsonNode root = objectMapper.readTree(createResponse);
        String surveyId = root.path("data").path("surveyId").asText();

        // 단일 설문 조회
        mockMvc.perform(get("/api/v1/surveys/" + surveyId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.surveyId").value(surveyId))
            .andExpect(jsonPath("$.data.title").value("고객 만족도 조사"))
            .andExpect(jsonPath("$.data.questions[0].name").value("이름"))
            .andExpect(jsonPath("$.data.questions[1].options[0].text").value("매우 만족"));
    }

    @Test
    @DisplayName("설문 생성 후 전체 설문 목록 조회 API - 정상 동작")
    void createAndListSurveys_success() throws Exception {
        // 설문 생성
        String createResponse = mockMvc.perform(post("/api/v1/surveys")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(getSurveyCreateRequest())))
            .andExpect(status().isCreated())
            .andReturn().getResponse().getContentAsString();

        JsonNode root = objectMapper.readTree(createResponse);
        String surveyId = root.path("data").path("surveyId").asText();

        // 전체 설문 목록 조회 (생성한 설문이 목록에 포함되어 있는지 확인)
        mockMvc.perform(get("/api/v1/surveys"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data[*].surveyId", org.hamcrest.Matchers.hasItem(surveyId)))
            .andExpect(jsonPath("$.data[?(@.surveyId=='" + surveyId + "')].title", org.hamcrest.Matchers.hasItem("고객 만족도 조사")));
    }

    @Test
    @DisplayName("설문조사 수정 API - 실제 응답 기반 정상 동작")
    void updateSurvey_success() throws Exception {
        // 1. 설문 생성 (POST)
        String createResponse = mockMvc.perform(post("/api/v1/surveys")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(getSurveyCreateRequest())))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        // 2. 생성 응답에서 surveyId, questionId, optionId 등 동적으로 추출
        var root = objectMapper.readTree(createResponse);
        String surveyId = root.path("data").path("surveyId").asText();
        var questions = root.path("data").path("questions");
        String idName = questions.get(0).path("id").asText(); // "이름" 문항 id
        String idSatisfaction = questions.get(1).path("id").asText(); // "만족도" 문항 id
        var options = questions.get(1).path("options");
        String idOpt1 = options.get(0).path("id").asText(); // 매우 만족
        String idOpt2 = options.get(1).path("id").asText(); // 만족
        String idOpt3 = options.get(2).path("id").asText(); // 보통
        String idOpt4 = options.get(3).path("id").asText(); // 불만족
        String idOpt5 = options.get(4).path("id").asText(); // 매우 불만족

        // 3. 실제 수정 요청값 구성 (실제 API 사용 예시와 동일하게)
        Map<String, Object> updateRequest = Map.of(
                "title", "2025년 2분기 고객 만족도 조사 (수정됨)",
                "description", "서비스 개선을 위한 고객 만족도 조사입니다. 여러분의 소중한 의견을 반영하여 더 나은 서비스를 제공하겠습니다.",
                "questions", List.of(
                        Map.of(
                                "id", idName,
                                "name", "이름 (수정됨)",
                                "description", "응답자의 이름을 입력해주세요 (필수)",
                                "inputType", "SHORT_TEXT",
                                "isRequired", true,
                                "order", 2,
                                "options", List.of()
                        ),
                        Map.of(
                                "id", idSatisfaction,
                                "name", "서비스 만족도",
                                "description", "전반적인 서비스 만족도를 선택해주세요 (필수)",
                                "inputType", "SINGLE_CHOICE",
                                "isRequired", true,
                                "order", 1,
                                "options", List.of(
                                        Map.of("id", idOpt1, "text", "⭐⭐⭐⭐⭐ 매우 만족", "order", 1),
                                        Map.of("id", idOpt2, "text", "⭐⭐⭐⭐ 만족", "order", 2),
                                        Map.of("id", idOpt3, "text", "⭐⭐⭐ 보통", "order", 3),
                                        Map.of("id", idOpt4, "text", "⭐⭐ 불만족", "order", 4),
                                        Map.of("id", idOpt5, "text", "⭐ 매우 불만족", "order", 5)
                                )
                        ),
                        Map.of(
                                // 신규 문항은 id 없이 추가
                                "name", "추가 의견",
                                "description", "서비스 개선을 위한 의견이 있으시다면 자유롭게 남겨주세요",
                                "inputType", "LONG_TEXT",
                                "isRequired", false,
                                "order", 3,
                                "options", List.of()
                        )
                )
        );

        // 4. 수정 요청 (PUT) 및 응답 검증
        mockMvc.perform(put("/api/v1/surveys/" + surveyId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                // 제목/설명 등 주요 필드 검증
                .andExpect(jsonPath("$.data.title").value("2025년 2분기 고객 만족도 조사 (수정됨)"))
                .andExpect(jsonPath("$.data.description").value("서비스 개선을 위한 고객 만족도 조사입니다. 여러분의 소중한 의견을 반영하여 더 나은 서비스를 제공하겠습니다."))
                // 문항 개수 및 이름
                .andExpect(jsonPath("$.data.questions", org.hamcrest.Matchers.hasSize(3)))
                .andExpect(jsonPath("$.data.questions[0].name").value("이름 (수정됨)"))
                .andExpect(jsonPath("$.data.questions[1].name").value("서비스 만족도"))
                .andExpect(jsonPath("$.data.questions[2].name").value("추가 의견"))
                // 옵션 텍스트 및 순서
                .andExpect(jsonPath("$.data.questions[1].options[0].text").value("⭐⭐⭐⭐⭐ 매우 만족"))
                .andExpect(jsonPath("$.data.questions[1].options[1].text").value("⭐⭐⭐⭐ 만족"))
                .andExpect(jsonPath("$.data.questions[1].options[2].text").value("⭐⭐⭐ 보통"))
                .andExpect(jsonPath("$.data.questions[1].options[3].text").value("⭐⭐ 불만족"))
                .andExpect(jsonPath("$.data.questions[1].options[4].text").value("⭐ 매우 불만족"));
    }

}
