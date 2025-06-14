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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ResponseValidationTest {

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

        survey = surveyRepository.save(new Survey("설문 제목", "설문 설명"));
        question = new Question("필수 질문", "내용", QuestionType.SHORT, true);
        question.setSurvey(survey);
        questionRepository.save(question);

        choice = new Choice("선택지1", 0);
        choice.setQuestion(question);
        choiceRepository.save(choice);
    }

    @Test
    void 필수질문응답없을때_400반환() throws Exception {
        ResponseRequest request = new ResponseRequest(
                survey.getId(),
                List.of(new AnswerRequest(question.getId(), null, null)) // ❌ 필수 질문인데 응답 없음
        );

        String json = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/responses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void 잘못된선택지전달시_400반환() throws Exception {
        // 다른 질문에 속한 선택지 생성
        Question 다른질문 = questionRepository.save(new Question("다른질문", "내용", QuestionType.MULTIPLE_CHOICE, false));
        Choice 잘못된선택지 = new Choice("잘못된 선택지", 0);
        잘못된선택지.setQuestion(다른질문);
        choiceRepository.save(잘못된선택지);

        ResponseRequest request = new ResponseRequest(
                survey.getId(),
                List.of(new AnswerRequest(question.getId(), null, List.of(잘못된선택지.getId())))
        );

        String json = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/responses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }
}
