package com.INNER_CIRCLE_ICD4.innerCircle;

import com.INNER_CIRCLE_ICD4.innerCircle.domain.*;
import com.INNER_CIRCLE_ICD4.innerCircle.dto.QuestionUpdateRequest;
import com.INNER_CIRCLE_ICD4.innerCircle.dto.SurveyUpdateRequest;
import com.INNER_CIRCLE_ICD4.innerCircle.repository.QuestionRepository;
import com.INNER_CIRCLE_ICD4.innerCircle.repository.SurveyRepository;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class SurveyUpdateTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @Autowired private SurveyRepository surveyRepository;
    @Autowired private QuestionRepository questionRepository;

    private Survey savedSurvey;
    private Question savedQuestion;

    @BeforeEach
    void setup() {
        questionRepository.deleteAll();
        surveyRepository.deleteAll();

        savedSurvey = surveyRepository.save(new Survey("기존 설문", "기존 설명"));
        savedQuestion = new Question("기존 질문", "설명", QuestionType.SHORT, true);
        savedQuestion.setSurvey(savedSurvey);
        questionRepository.save(savedQuestion);
    }

    @Test
    void 설문_제목_설명만_수정() throws Exception {
        SurveyUpdateRequest updateRequest = new SurveyUpdateRequest(
                "수정된 제목",
                "수정된 설명",
                List.of() // 질문 수정 없음
        );

        mockMvc.perform(put("/surveys/" + savedSurvey.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk());
    }

    @Test
    void 설문_질문_추가() throws Exception {
        QuestionUpdateRequest newQuestion = new QuestionUpdateRequest(
                null,
                "새 질문",
                "새 설명",
                QuestionType.LONG,
                true,
                null
        );

        SurveyUpdateRequest updateRequest = new SurveyUpdateRequest(
                savedSurvey.getTitle(),
                savedSurvey.getDescription(),
                List.of(newQuestion)
        );

        mockMvc.perform(put("/surveys/" + savedSurvey.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk());
    }

    @Test
    void 존재하지않는_설문ID_수정시_404반환() throws Exception {
        SurveyUpdateRequest updateRequest = new SurveyUpdateRequest(
                "아무 제목",
                "아무 설명",
                List.of()
        );

        mockMvc.perform(put("/surveys/" + UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isNotFound());
    }
}
