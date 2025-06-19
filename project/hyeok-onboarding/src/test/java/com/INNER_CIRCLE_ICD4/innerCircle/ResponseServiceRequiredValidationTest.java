package com.INNER_CIRCLE_ICD4.innerCircle;

import com.INNER_CIRCLE_ICD4.innerCircle.domain.QuestionType;
import com.INNER_CIRCLE_ICD4.innerCircle.dto.*;
import com.INNER_CIRCLE_ICD4.innerCircle.exception.BusinessException;
import com.INNER_CIRCLE_ICD4.innerCircle.service.ResponseService;
import com.INNER_CIRCLE_ICD4.innerCircle.service.SurveyService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class ResponseServiceRequiredValidationTest {

    @Autowired
    private SurveyService surveyService;

    @Autowired
    private ResponseService responseService;

    @Test
    @DisplayName("필수 질문인데 응답이 비어있으면 예외가 발생해야 한다")
    void 필수_질문_응답_누락_예외() {
        // given
        SurveyRequest surveyRequest = new SurveyRequest(
                "설문 제목",
                "설문 설명",
                List.of(
                        new QuestionRequest("질문 제목", "질문 설명", QuestionType.SHORT, true, List.of())
                )
        );

        SurveyResponse createdSurvey = surveyService.createSurvey(surveyRequest);
        UUID surveyId = createdSurvey.id();
        UUID questionId = createdSurvey.questions().get(0).id(); // ← 여기서 실제 질문 ID 추출

        // when
        ResponseRequest responseRequest = new ResponseRequest(
                surveyId,
                List.of(new AnswerRequest(
                        questionId, // 실제 질문 ID 사용
                        null,       // 필수인데 응답 없음
                        null
                ))
        );

        // then
        assertThatThrownBy(() -> responseService.saveResponse(responseRequest))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("필수 질문에 응답이 없습니다");
    }

}
