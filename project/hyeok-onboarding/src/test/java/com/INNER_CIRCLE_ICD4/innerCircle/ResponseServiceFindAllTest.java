package com.INNER_CIRCLE_ICD4.innerCircle;

import com.INNER_CIRCLE_ICD4.innerCircle.domain.QuestionType;
import com.INNER_CIRCLE_ICD4.innerCircle.dto.*;
import com.INNER_CIRCLE_ICD4.innerCircle.service.ResponseService;
import com.INNER_CIRCLE_ICD4.innerCircle.service.SurveyService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ResponseServiceFindAllTest {

    @Autowired
    private SurveyService surveyService;

    @Autowired
    private ResponseService responseService;

    @Test
    @DisplayName("모든 응답을 조회할 수 있다")
    void findAllResponses() {
        // given
        SurveyRequest surveyRequest = new SurveyRequest(
                "응답조회용 설문",
                "설명",
                List.of(
                        new QuestionRequest("이름?", "간단한 질문", QuestionType.SHORT, true, List.of())
                )
        );
        SurveyResponse surveyResponse = surveyService.createSurvey(surveyRequest);
        UUID surveyId = surveyResponse.id();

        // ✅ 실제 저장된 질문 ID 추출
        UUID questionId = surveyResponse.questions().get(0).id();

        ResponseRequest request = new ResponseRequest(
                surveyId,
                List.of(new AnswerRequest(
                        questionId,   // ✅ 실제 존재하는 질문 ID 사용
                        "홍길동",
                        null
                ))
        );
        responseService.saveResponse(request);

        // when
        List<ResponseDto> all = responseService.findAll();

        // then
        assertThat(all).isNotEmpty();
    }

}
