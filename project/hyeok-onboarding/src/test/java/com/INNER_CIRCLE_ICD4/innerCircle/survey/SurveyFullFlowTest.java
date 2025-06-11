package com.INNER_CIRCLE_ICD4.innerCircle.survey;

import com.INNER_CIRCLE_ICD4.innerCircle.survey.request.domain.*;
import com.INNER_CIRCLE_ICD4.innerCircle.survey.request.repository.SurveyRepository;
import com.INNER_CIRCLE_ICD4.innerCircle.survey.request.repository.ResponseRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class SurveyFullFlowTest {

    @Autowired
    private SurveyRepository surveyRepository;

    @Autowired
    private ResponseRepository responseRepository;

    @Test
    @Transactional
    void surveyFullFolwTest() {
        // === 1. 설문 생성 ===
        Survey survey = new Survey();
        survey.setTitle("고객 피드백 조사");
        survey.setDescription("우리 서비스에 대한 피드백을 주세요");

        // === 2. 질문 생성 ===
        Question q1 = new Question();
        q1.setQuestionText("서비스는 만족스러우셨나요?");
        q1.setType(QuestionType.SHORT);
        q1.setRequired(true);

        Question q2 = new Question();
        q2.setQuestionText("어떤 기능을 가장 많이 사용하시나요?");
        q2.setType(QuestionType.SINGLE);
        q2.setRequired(true);

        QuestionOption op1 = new QuestionOption();
        op1.setOptionValue("검색");

        QuestionOption op2 = new QuestionOption();
        op2.setOptionValue("결제");

        QuestionOption op3 = new QuestionOption();
        op3.setOptionValue("추천");

        q2.addOption(op1);
        q2.addOption(op2);
        q2.addOption(op3);

        survey.addQuestion(q1);
        survey.addQuestion(q2);

        surveyRepository.save(survey);

        // === 3. 응답 생성 ===
        Response response = new Response();
        response.setSurvey(survey);

        Answer a1 = new Answer();
        a1.setQuestion(q1);
        a1.setAnswerValue("네, 아주 만족합니다.");

        Answer a2 = new Answer();
        a2.setQuestion(q2);
        a2.setAnswerValue("추천");

        response.addAnswer(a1);
        response.addAnswer(a2);

        responseRepository.save(response);

        // === 4. 검증 ===
        Response saved = responseRepository.findById(response.getId()).orElseThrow();

        assertThat(saved.getAnswers()).hasSize(2);
        assertThat(saved.getAnswers().get(0).getAnswerValue()).contains("만족");
        assertThat(saved.getSurvey().getTitle()).isEqualTo("고객 피드백 조사");
    }
}