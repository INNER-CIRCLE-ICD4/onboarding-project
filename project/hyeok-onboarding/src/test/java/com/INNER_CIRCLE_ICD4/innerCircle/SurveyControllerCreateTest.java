// src/test/java/com/INNER_CIRCLE_ICD4/innerCircle/SurveyControllerCreateTest.java
package com.INNER_CIRCLE_ICD4.innerCircle;

import com.INNER_CIRCLE_ICD4.innerCircle.domain.QuestionType;
import com.INNER_CIRCLE_ICD4.innerCircle.dto.QuestionRequest;
import com.INNER_CIRCLE_ICD4.innerCircle.dto.SurveyRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class SurveyControllerCreateTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;

    @Test
    void 설문생성_성공() throws Exception {
        SurveyRequest req = new SurveyRequest(
                "테스트 제목",
                "테스트 설명",
                List.of(
                        // SHORT_TEXT → SHORT
                        new QuestionRequest("Q1", "Desc1", QuestionType.SHORT, true, null),
                        // SINGLE_CHOICE 그대로
                        new QuestionRequest("Q2", "Desc2", QuestionType.SINGLE_CHOICE, false, List.of("A", "B"))
                )
        );

        mockMvc.perform(post("/surveys")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("테스트 제목"))
                .andExpect(jsonPath("$.questions").isArray())
                .andExpect(jsonPath("$.questions.length()").value(2));
    }
}
