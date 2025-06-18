package com.INNER_CIRCLE_ICD4.innerCircle;

import com.INNER_CIRCLE_ICD4.innerCircle.domain.*;
import com.INNER_CIRCLE_ICD4.innerCircle.dto.AnswerRequest;
import com.INNER_CIRCLE_ICD4.innerCircle.dto.ResponseRequest;
import com.INNER_CIRCLE_ICD4.innerCircle.repository.ResponseRepository;
import com.INNER_CIRCLE_ICD4.innerCircle.repository.SurveyRepository;
import com.INNER_CIRCLE_ICD4.innerCircle.service.ResponseService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class ResponseServiceSaveTest {

    @Autowired
    private SurveyRepository surveyRepository;

    @Autowired
    private ResponseRepository responseRepository;

    @Autowired
    private ResponseService responseService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void 응답을_정상적으로_저장한다() {
        // given - 설문 + 질문 + 선택지 구성
        Survey survey = new Survey("설문 제목", "설문 설명");
        Question question = new Question("질문 제목", "질문 설명", QuestionType.SHORT, true);
        question.setSurvey(survey);
        survey.setQuestions(List.of(question));
        surveyRepository.save(survey);

        // when - 응답 요청 생성
        AnswerRequest answerRequest = new AnswerRequest(
                question.getId(),
                "응답 텍스트",
                null
        );

        ResponseRequest request = new ResponseRequest(
                survey.getId(),
                List.of(answerRequest)
        );

        UUID responseId = responseService.saveResponse(request);

        // then - 응답 저장 확인
        var saved = responseRepository.findById(responseId).orElseThrow();
        assertThat(saved.getSurvey().getId()).isEqualTo(survey.getId());
        assertThat(saved.getAnswers()).hasSize(1);
        assertThat(saved.getAnswers().get(0).getText()).isEqualTo("응답 텍스트");
    }
}
