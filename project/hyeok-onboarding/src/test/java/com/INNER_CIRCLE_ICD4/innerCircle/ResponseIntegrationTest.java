package com.INNER_CIRCLE_ICD4.innerCircle;

import com.INNER_CIRCLE_ICD4.innerCircle.domain.*;
import com.INNER_CIRCLE_ICD4.innerCircle.dto.*;
import com.INNER_CIRCLE_ICD4.innerCircle.repository.*;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ResponseIntegrationTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @Autowired private SurveyRepository surveyRepository;
    @Autowired private QuestionRepository questionRepository;
    @Autowired private ChoiceRepository choiceRepository;
    @Autowired private ResponseRepository responseRepository;

    private Survey survey;
    private Question question;
    private Choice choice;

    @BeforeEach
    void setUp() {
        responseRepository.deleteAll();
        choiceRepository.deleteAll();
        questionRepository.deleteAll();
        surveyRepository.deleteAll();

        survey = surveyRepository.save(new Survey("테스트 설문", "설명"));
        question = questionRepository.save(new Question("질문1", "설명1", QuestionType.SHORT, true));
        question.setSurvey(survey);
        survey.addQuestion(question);

        choice = choiceRepository.save(new Choice("선택지1", question));
    }

    @Test
    void 응답제출_후_단건조회_테스트() throws Exception {
        // 1. 응답 저장 요청
        ResponseRequest request = new ResponseRequest(
                survey.getId(),
                List.of(new AnswerRequest(
                        question.getId(),
                        "기입형 응답",
                        List.of(choice.getId())
                ))
        );

        String json = objectMapper.writeValueAsString(request);

        String responseId = mockMvc.perform(post("/responses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString()
                .replace("\"", "");

        // 2. 응답 단건 조회 요청
        mockMvc.perform(get("/responses/" + responseId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(responseId))
                .andExpect(jsonPath("$.answers[0].textValue").value("기입형 응답"));
    }
}
