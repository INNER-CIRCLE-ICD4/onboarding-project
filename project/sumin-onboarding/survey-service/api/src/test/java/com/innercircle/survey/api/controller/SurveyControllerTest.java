package com.innercircle.survey.api.controller;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SurveyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void createSurveySuccess() throws Exception {
        String json = """
                {
                  "title": "테스트 설문",
                  "description": "설문 설명입니다.",
                  "questions": [
                    {
                      "title": "당신의 성별은?",
                      "description": "",
                      "type": "SINGLE_CHOICE",
                      "required": true,
                      "options": ["남자", "여자", "기타"]
                    }
                  ]
                }
                """;

        mockMvc.perform(post("/api/surveys")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.surveyId").exists());
    }
}
