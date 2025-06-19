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
class ResponseServiceFindByIdTest {

    @Autowired
    private SurveyService surveyService;

    @Autowired
    private ResponseService responseService;

    @Test
    @DisplayName("응답 ID로 단일 응답을 조회할 수 있다")
    void 응답_단일_조회() {
        // given
        SurveyRequest surveyRequest = new SurveyRequest(
                "설문 제목",
                "설문 설명",
                List.of(
                        new QuestionRequest("질문1", "설명1", QuestionType.SHORT, true, List.of())
                )
        );
        SurveyResponse survey = surveyService.createSurvey(surveyRequest);

        UUID questionId = survey.questions().get(0).id(); // ✅ 질문 ID 추출

        ResponseRequest responseRequest = new ResponseRequest(
                survey.id(),
                List.of(
                        new AnswerRequest(questionId, "답변입니다", null) // ✅ 올바른 타입 전달
                )
        );
        UUID savedResponseId = responseService.saveResponse(responseRequest);

        // when
        ResponseDto found = responseService.findById(savedResponseId);

        // then
        assertThat(found).isNotNull();
        assertThat(found.answers()).hasSize(1);
        assertThat(found.answers().get(0).text()).isEqualTo("답변입니다");
    }
}
