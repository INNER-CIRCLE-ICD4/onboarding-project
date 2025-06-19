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
class ResponseServiceFindBySurveyIdTest {

    @Autowired
    private SurveyService surveyService;

    @Autowired
    private ResponseService responseService;

    @Test
    @DisplayName("설문 ID로 전체 응답을 조회할 수 있다")
    void 설문_응답_전체_조회() {
        // given
        SurveyRequest request = new SurveyRequest(
                "설문 제목",
                "설문 설명",
                List.of(new QuestionRequest("질문1", "설명1", QuestionType.SHORT, true, List.of()))
        );

        SurveyResponse survey = surveyService.createSurvey(request);
        UUID questionId = survey.questions().get(0).id();

        responseService.saveResponse(new ResponseRequest(
                survey.id(),
                List.of(new AnswerRequest(questionId, "첫번째 답변", null))
        ));

        // when
        List<ResponseDetail> responses = responseService.getResponses(survey.id());

        // then
        assertThat(responses).hasSize(1);
        assertThat(responses.get(0).answers()).hasSize(1);
        assertThat(responses.get(0).answers().get(0).text()).isEqualTo("첫번째 답변");
    }

}
