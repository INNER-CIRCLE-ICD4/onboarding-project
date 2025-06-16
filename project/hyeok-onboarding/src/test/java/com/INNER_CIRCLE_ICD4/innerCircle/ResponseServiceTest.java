// src/test/java/com/INNER_CIRCLE_ICD4/innerCircle/ResponseServiceTest.java
package com.INNER_CIRCLE_ICD4.innerCircle;

import com.INNER_CIRCLE_ICD4.innerCircle.domain.*;
import com.INNER_CIRCLE_ICD4.innerCircle.dto.*;
import com.INNER_CIRCLE_ICD4.innerCircle.exception.BusinessException;
import com.INNER_CIRCLE_ICD4.innerCircle.exception.ResourceNotFoundException;
import com.INNER_CIRCLE_ICD4.innerCircle.repository.ResponseRepository;
import com.INNER_CIRCLE_ICD4.innerCircle.repository.SurveyRepository;
import com.INNER_CIRCLE_ICD4.innerCircle.service.ResponseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

public class ResponseServiceTest {

    private ResponseService responseService;
    private SurveyRepository surveyRepository;
    private ResponseRepository responseRepository;

    @BeforeEach
    void setUp() {
        surveyRepository = mock(SurveyRepository.class);
        responseRepository = mock(ResponseRepository.class);
        responseService = new ResponseService(surveyRepository, responseRepository);
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
}
