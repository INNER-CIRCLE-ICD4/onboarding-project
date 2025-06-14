package com.INNER_CIRCLE_ICD4.innerCircle;

import com.INNER_CIRCLE_ICD4.innerCircle.domain.*;
import com.INNER_CIRCLE_ICD4.innerCircle.dto.*;
import com.INNER_CIRCLE_ICD4.innerCircle.repository.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class SurveyDtoTest {

    @Autowired
    SurveyRepository surveyRepository;

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    ChoiceRepository choiceRepository;

    @Test
    void SurveyDtoTestFunc() {
        // 1. 설문 생성 및 저장
        Survey survey = new Survey("설문 제목", "설문 설명");
        surveyRepository.save(survey);

        // 2. 질문 생성 및 추가
        Question question = new Question("질문 제목", "질문 설명", QuestionType.SINGLE_CHOICE, true);
        question.setSurvey(survey);
        questionRepository.save(question);

        // 3. 선택지 추가
        Choice choice1 = new Choice("선택지1", 0);
        choice1.setQuestion(question);
        Choice choice2 = new Choice("선택지2", 1);
        choice2.setQuestion(question);
        choiceRepository.saveAll(List.of(choice1, choice2));

        // 4. DTO 변환
        List<ChoiceResponse> choiceResponses = List.of(
                new ChoiceResponse(choice1.getId(), choice1.getText(), choice1.getChoiceIndex()),
                new ChoiceResponse(choice2.getId(), choice2.getText(), choice2.getChoiceIndex())
        );

        QuestionResponse questionResponse = new QuestionResponse(
                question.getId(),
                question.getTitle(),
                question.getDescription(),
                question.getType(),
                question.isRequired(),
                choiceResponses
        );

        List<QuestionResponse> questionResponses = List.of(questionResponse);

        SurveyResponse surveyResponse = new SurveyResponse(
                survey.getId(),
                survey.getTitle(),
                survey.getDescription(),
                survey.getVersion(),
                questionResponses
        );

        // 5. 검증
        assertThat(surveyResponse.title()).isEqualTo("설문 제목");
        assertThat(surveyResponse.questions()).hasSize(1);
        assertThat(surveyResponse.questions().get(0).choices()).hasSize(2);
    }
}
