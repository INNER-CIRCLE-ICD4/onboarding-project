package kr.co.fastcampus.onboarding.hyeongminonboarding.scenarioTest;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import kr.co.fastcampus.onboarding.hyeongminonboarding.global.dto.request.BaseRequest;
import kr.co.fastcampus.onboarding.hyeongminonboarding.domain.api.survey.dto.request.SubmitSurveyAnswersRequest;
import kr.co.fastcampus.onboarding.hyeongminonboarding.domain.api.survey.dto.request.SubmitAnswerRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ScenarioTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void fullSurveyLifecycleScenario() throws Exception {
        // 0. 설문조사를 생성한다.
        String createJson = "{\n" +
                "  \"body\": {\n" +
                "    \"title\": \"Onboarding Survey\",\n" +
                "    \"description\": \"Project skill evaluation\",\n" +
                "    \"questions\": [\n" +
                "      {\"title\": \"Q1?\", \"detail\": \"D1\", \"type\": \"SHORT_TEXT\", \"required\": true},\n" +
                "      {\"title\": \"Q2?\", \"detail\": null, \"type\": \"SINGLE_CHOICE\", \"required\": false,\n" +
                "        \"options\": [\"A\",\"B\"]}\n" +
                "    ]\n" +
                "  }\n" +
                "}";
        MvcResult createResult = mockMvc.perform(post("/api/survey/createSurvey")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createJson))
                .andExpect(status().isOk())
                .andReturn();
        JsonNode createNode = objectMapper.readTree(createResult.getResponse().getContentAsString());
        long surveyId = createNode.at("/data/surveyId").asLong();

        JsonNode questionsNode = createNode.at("/data/questions");
        long q1Id = questionsNode.get(0).get("id").asLong();
        long q2Id = questionsNode.get(1).get("id").asLong();
        JsonNode optionsNode = questionsNode.get(1).get("options");
        long optA = optionsNode.get(0).get("id").asLong();
        long optB = optionsNode.get(1).get("id").asLong();

        // 1. User A 가 설문조사를 제출한다.
        SubmitSurveyAnswersRequest answersA = SubmitSurveyAnswersRequest.builder()
                .answers(List.of(
                        new SubmitAnswerRequest(q1Id, "Ans A1", null),
                        new SubmitAnswerRequest(q2Id, null, List.of(optA))
                )).build();
        mockMvc.perform(post("/api/survey/" + surveyId + "/submit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(BaseRequest.of(answersA))))
                .andExpect(status().isOk());

        // 2. User B 가 설문조사를 제출한다.
        SubmitSurveyAnswersRequest answersB = SubmitSurveyAnswersRequest.builder()
                .answers(List.of(
                        new SubmitAnswerRequest(q1Id, "Ans B1", null),
                        new SubmitAnswerRequest(q2Id, null, List.of(optB))
                )).build();
        mockMvc.perform(post("/api/survey/" + surveyId + "/submit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(BaseRequest.of(answersB))))
                .andExpect(status().isOk());

        // 3. 설문조사를 수정한다.
        ObjectNode bodyNode = objectMapper.createObjectNode();
        bodyNode.put("id", surveyId);
        bodyNode.put("title", "Onboarding Survey v2");
        bodyNode.put("description", "Updated description");
        ArrayNode questionsArray = objectMapper.createArrayNode();
        questionsArray.add(objectMapper.createObjectNode()
                .put("id", q1Id)
                .put("title", "Q1 updated")
                .put("detail", "D1 updated")
                .put("type", "SHORT_TEXT")
                .put("required", true)
        );
        questionsArray.add(objectMapper.createObjectNode()
                .put("title", "Q3 new")
                .put("detail", "")
                .put("type", "LONG_TEXT")
                .put("required", false)
        );
        bodyNode.set("questions", questionsArray);

        ObjectNode rootNode = objectMapper.createObjectNode();
        rootNode.set("body", bodyNode);
        String updateJson = objectMapper.writeValueAsString(rootNode);

        mockMvc.perform(put("/api/survey/" + surveyId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isOk());

        // 4. User C 설문조사를 제출한다.
        MvcResult detailResult = mockMvc.perform(get("/api/survey/" + surveyId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        JsonNode detailNode = objectMapper.readTree(detailResult.getResponse().getContentAsString());
        JsonNode updatedQuestions = detailNode.at("/data/questions");
        long q3Id = updatedQuestions.get(1).get("id").asLong();
        SubmitSurveyAnswersRequest answersC = SubmitSurveyAnswersRequest.builder()
                .answers(List.of(
                        new SubmitAnswerRequest(q1Id, "Ans C1", null),
                        new SubmitAnswerRequest(q3Id, "Ans C3", null)
                )).build();
        mockMvc.perform(post("/api/survey/" + surveyId + "/submit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(BaseRequest.of(answersC))))
                .andExpect(status().isOk());

        // 5. User D 설문조사를 제출한다.
        SubmitSurveyAnswersRequest answersD = SubmitSurveyAnswersRequest.builder()
                .answers(List.of(
                        new SubmitAnswerRequest(q1Id, "Ans D1", null),
                        new SubmitAnswerRequest(q3Id, "Ans D3", null)
                )).build();
        mockMvc.perform(post("/api/survey/" + surveyId + "/submit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(BaseRequest.of(answersD))))
                .andExpect(status().isOk());

        // 수정되기전 설문조사를 제출한 내용과 수정된 후 설문조사를 제출한 내용을 비교 할 수 있어야 한다.
        MvcResult resResult = mockMvc.perform(get("/api/survey/" + surveyId + "/responses")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        JsonNode resNode = objectMapper.readTree(resResult.getResponse().getContentAsString());
        JsonNode items = resNode.at("/data/responses");
        assertThat(items.size()).isEqualTo(4);
        assertThat(resNode.at("/data/title").asText()).isEqualTo("Onboarding Survey v2");
    }
}
