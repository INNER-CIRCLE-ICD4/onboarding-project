package com.INNER_CIRCLE_ICD4.innerCircle;


import com.INNER_CIRCLE_ICD4.innerCircle.domain.*;
import com.INNER_CIRCLE_ICD4.innerCircle.dto.SurveyResponse;
import com.INNER_CIRCLE_ICD4.innerCircle.mapper.SurveyMapper;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class SurveyMapperTest {

    @Test
    void survey_to_response_mapper_test() {
        // given
        Survey survey = new Survey("제목", "설명");
        Question question = new Question("질문1", "설명1", QuestionType.SINGLE_CHOICE, true);
        question.setSurvey(survey);

        Choice choice1 = new Choice("선택1", 0);
        choice1.setQuestion(question);
        Choice choice2 = new Choice("선택2", 1);
        choice2.setQuestion(question);

        question.getChoices().addAll(List.of(choice1, choice2));
        survey.getQuestions().add(question);

        // when
        SurveyResponse response = SurveyMapper.toResponse(survey);

        // then
        assertThat(response.title()).isEqualTo("제목");
        assertThat(response.questions()).hasSize(1);
        assertThat(response.questions().get(0).choices()).hasSize(2);
    }
}
