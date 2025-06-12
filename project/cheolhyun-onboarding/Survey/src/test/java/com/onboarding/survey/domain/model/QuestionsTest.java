package com.onboarding.survey.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class QuestionsTest {
    private final String QUESTION_TEXT = "Question";
    private final String DESCRIPTION_TEXT = "Description";

    @Test
    @DisplayName("설문 받을 항목의 개수 검증")
    void validateQuestions() {
        Questions questions = new Questions();
        int index = Questions.MAX_SIZE + 1;

        for(int i = 0; i < Questions.MAX_SIZE; i++) {
            questions.add(QUESTION_TEXT + i, DESCRIPTION_TEXT + i);
        }

        assertThatThrownBy(() ->
                questions.add(QUESTION_TEXT + index, DESCRIPTION_TEXT + index))
                    .isInstanceOf(IndexOutOfBoundsException.class);
    }
}