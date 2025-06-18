package com.INNER_CIRCLE_ICD4.innerCircle;

import com.INNER_CIRCLE_ICD4.innerCircle.domain.*;
import com.INNER_CIRCLE_ICD4.innerCircle.dto.*;
import com.INNER_CIRCLE_ICD4.innerCircle.exception.BusinessException;
import com.INNER_CIRCLE_ICD4.innerCircle.exception.ResourceNotFoundException;
import com.INNER_CIRCLE_ICD4.innerCircle.repository.ResponseRepository;
import com.INNER_CIRCLE_ICD4.innerCircle.repository.SurveyRepository;
import com.INNER_CIRCLE_ICD4.innerCircle.service.ResponseService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

public class ResponseServiceTest {

    private ResponseService responseService;
    private SurveyRepository surveyRepository;
    private ResponseRepository responseRepository;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        surveyRepository = mock(SurveyRepository.class);
        responseRepository = mock(ResponseRepository.class);
        objectMapper = new ObjectMapper();
        responseService = new ResponseService(surveyRepository, responseRepository, objectMapper);
    }

    @Test
    void 설문_없으면_ResourceNotFoundException_발생() {
        // given
        UUID surveyId = UUID.randomUUID();
        ResponseRequest req = new ResponseRequest(surveyId, List.of());

        when(surveyRepository.findById(surveyId)).thenReturn(Optional.empty());

        // expect
        assertThatThrownBy(() -> responseService.saveResponse(req))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("설문이 존재하지 않습니다");
    }

    @Test
    void 질문수_불일치시_BusinessException_발생() {
        // given
        UUID surveyId = UUID.randomUUID();
        Survey survey = new Survey("title", "desc");
        Question question = new Question("q1", "desc", QuestionType.SHORT, true);
        survey.addQuestion(question);

        when(surveyRepository.findById(surveyId)).thenReturn(Optional.of(survey));

        ResponseRequest req = new ResponseRequest(surveyId, List.of()); // 질문 없음

        // expect
        assertThatThrownBy(() -> responseService.saveResponse(req))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("응답 개수가 설문 질문 개수와 일치하지 않습니다");
    }

    @Test
    void 스냅샷이_정상적으로_저장된다() {
        // given
        UUID surveyId = UUID.randomUUID();
        UUID questionId = UUID.randomUUID();

        Survey survey = new Survey("title", "desc");
        Question question = new Question("q1", "desc", QuestionType.SHORT, true);

        // 👇 직접 ID 세팅
        ReflectionTestUtils.setField(survey, "id", surveyId);
        ReflectionTestUtils.setField(question, "id", questionId);

        survey.addQuestion(question);

        when(surveyRepository.findById(surveyId)).thenReturn(Optional.of(survey));

        AnswerRequest answerReq = new AnswerRequest(questionId, "답변입니다", null);
        ResponseRequest request = new ResponseRequest(surveyId, List.of(answerReq));

        when(responseRepository.save(any())).thenAnswer(invocation -> {
            Response saved = invocation.getArgument(0);
            String snapshot = saved.getSurveySnapshot();

            assertThat(snapshot).contains("\"title\":\"title\"");
            assertThat(snapshot).contains("\"description\":\"desc\"");
            assertThat(snapshot).contains("q1");

            return saved;
        });

        // when
        responseService.saveResponse(request);

        // then - 예외 없이 통과되면 성공
    }


}
