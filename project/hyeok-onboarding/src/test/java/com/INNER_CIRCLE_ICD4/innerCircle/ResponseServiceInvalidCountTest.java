package com.INNER_CIRCLE_ICD4.innerCircle;

import com.INNER_CIRCLE_ICD4.innerCircle.domain.Question;
import com.INNER_CIRCLE_ICD4.innerCircle.domain.QuestionType;
import com.INNER_CIRCLE_ICD4.innerCircle.domain.Survey;
import com.INNER_CIRCLE_ICD4.innerCircle.dto.AnswerRequest;
import com.INNER_CIRCLE_ICD4.innerCircle.dto.ResponseRequest;
import com.INNER_CIRCLE_ICD4.innerCircle.exception.BusinessException;
import com.INNER_CIRCLE_ICD4.innerCircle.repository.SurveyRepository;
import com.INNER_CIRCLE_ICD4.innerCircle.service.ResponseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
public class ResponseServiceInvalidCountTest {

    @Autowired
    private SurveyRepository surveyRepository;

    @Autowired
    private ResponseService responseService;

    private Survey savedSurvey;
    private Question q1;
    private Question q2;

    @BeforeEach
    void setup() {
        // 설문 + 질문 2개 생성
        Survey survey = new Survey("설문 제목", "설문 설명");
        q1 = new Question("질문1", "설명1", QuestionType.SHORT, true);
        q2 = new Question("질문2", "설명2", QuestionType.SHORT, true);
        survey.addQuestion(q1);
        survey.addQuestion(q2);

        savedSurvey = surveyRepository.save(survey);
    }

    @Test
    @DisplayName("응답 수가 질문 수보다 적으면 예외가 발생해야 한다")
    void 응답수_불일치_예외발생() {
        // 질문은 2개인데 응답은 1개만 보냄
        List<AnswerRequest> invalidAnswers = List.of(
                new AnswerRequest(q1.getId(), "답변1", null)
        );
        ResponseRequest request = new ResponseRequest(savedSurvey.getId(), invalidAnswers);

        // 예외 검증
        assertThatThrownBy(() -> responseService.saveResponse(request))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("응답 개수가 설문 질문 개수와 일치하지 않습니다.");
    }
}
