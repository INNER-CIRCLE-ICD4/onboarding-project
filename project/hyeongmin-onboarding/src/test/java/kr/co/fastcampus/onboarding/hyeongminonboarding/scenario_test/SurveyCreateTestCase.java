package kr.co.fastcampus.onboarding.hyeongminonboarding.scenario_test;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import kr.co.fastcampus.onboarding.hyeongminonboarding.global.exception.enums.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


@SpringBootTest
@AutoConfigureMockMvc
public class SurveyCreateTestCase {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("정상 요청")
    void givenValidRequest_whenCreateSurvey_thenReturnsOkAndId() throws Exception {
        String payload = """
            {
              "body": {
                "name": "테스트 설문",
                "description": "설문 설명",
                "questionItems": [
                  {
                    "title": "질문1",
                    "detail": "상세1",
                    "type": "SHORT_TEXT",
                    "required": false,
                    "questionOptionItems": []
                  },
                  {
                    "title": "질문2",
                    "detail": "상세2",
                    "type": "SINGLE_CHOICE",
                    "required": true,
                    "questionOptionItems": [
                      { "optionValue": "옵션A" },
                      { "optionValue": "옵션B" }
                    ]
                  }
                ]
              }
            }
            """;

        mockMvc.perform(post("/api/survey/createSurvey")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.surveyId").isNumber());
    }

    @Test
    @DisplayName("이름 누락")
    void givenMissingName_whenCreateSurvey_thenValidationError() throws Exception {
        String payload = """
            {
              "body": {
                "description": "설문 설명",
                "questionItems": [
                  {
                    "title": "Q",
                    "detail": "D",
                    "type": "SHORT_TEXT",
                    "required": false,
                    "questionOptionItems": []
                  }
                ]
              }
            }
            """;

        mockMvc.perform(post("/api/survey/createSurvey")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ErrorCode.INVALID_PAYLOAD.getCode()))
                .andExpect(jsonPath("$.fieldErrors[?(@.field=='body.name')]").exists());
    }

    @Test
    @DisplayName("questionItems 빈 리스트")
    void givenEmptyQuestions_whenCreateSurvey_thenValidationError() throws Exception {
        String payload = """
            {
              "body": {
                "name": "테스트",
                "description": "설명",
                "questionItems": []
              }
            }
            """;

        mockMvc.perform(post("/api/survey/createSurvey")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ErrorCode.INVALID_PAYLOAD.getCode()))
                .andExpect(jsonPath("$.fieldErrors[?(@.field=='body.questionItems')]").exists());
    }

    @Test
    @DisplayName("선택형 질문에 옵션 누락")
    void givenChoiceWithoutOptions_whenCreateSurvey_thenBusinessError() throws Exception {
        String payload = """
            {
              "body": {
                "name": "테스트 설문",
                "description": "설문 설명",
                "questionItems": [
                  {
                    "title": "질문1",
                    "detail": "상세1",
                    "type": "SINGLE_CHOICE",
                    "required": true,
                    "questionOptionItems": []
                  }
                ]
              }
            }
            """;

        mockMvc.perform(post("/api/survey/createSurvey")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ErrorCode.SURVEY_QUESTION_OPTION_EMPTY.getCode()))
                .andExpect(jsonPath("$.message").value(ErrorCode.SURVEY_QUESTION_OPTION_EMPTY.getMessage()));
    }

}
