package com.INNER_CIRCLE_ICD4.innerCircle;

import com.INNER_CIRCLE_ICD4.innerCircle.repository.*;
import com.INNER_CIRCLE_ICD4.innerCircle.domain.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class SurveyRepositoryTest {

    @Autowired
    private SurveyRepository surveyRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private ChoiceRepository choiceRepository;

    @Test
    void surveyRepositoryTestFunc() {
        // 1. 설문 생성
        Survey survey = new Survey("테스트 설문", "설문 설명입니다.");
        surveyRepository.save(survey);

        // 2. 질문 생성
        Question question = new Question("질문 제목", "질문 설명", QuestionType.SINGLE_CHOICE, true);
        question.setSurvey(survey);
        questionRepository.save(question);

        // 3. 선택지 생성 및 추가
        Choice choice1 = new Choice("선택지1", 0);
        choice1.setQuestion(question);
        Choice choice2 = new Choice("선택지2", 1);
        choice2.setQuestion(question);

        choiceRepository.save(choice1);
        choiceRepository.save(choice2);

        // 4. 저장 후 조회
        Survey foundSurvey = surveyRepository.findById(survey.getId()).orElseThrow();
        assertThat(foundSurvey.getTitle()).isEqualTo("테스트 설문");
    }
}