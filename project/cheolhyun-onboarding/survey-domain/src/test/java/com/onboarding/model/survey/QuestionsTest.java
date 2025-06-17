package com.onboarding.model.survey;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class QuestionsTest {
    private final String QUESTION_ID = "QUESTION_ID";
    private final String QUESTION_TEXT = "Question";
    private final String DESCRIPTION_TEXT = "Description";

    @Test
    @DisplayName("설문 받을 항목의 개수 검증")
    void 설문_항목_개수_검증() {
        Questions questions = new Questions();

        for(int i = 0; i < Questions.MAX_SIZE; i++) {
            questions.add(QUESTION_ID + i, QUESTION_TEXT + i, DESCRIPTION_TEXT + i);
        }

        assertThatThrownBy(() ->
                questions.add(String.valueOf(Questions.MAX_SIZE), QUESTION_TEXT + Questions.MAX_SIZE, DESCRIPTION_TEXT + Questions.MAX_SIZE))
                    .isInstanceOf(IndexOutOfBoundsException.class);
    }

    @Test
    void 설문_항목_수정() {
        Questions questions = new Questions();

        for(int i = 0; i < Questions.MAX_SIZE; i++) {
            questions.add(QUESTION_ID + 1, QUESTION_TEXT + i, DESCRIPTION_TEXT + i);
        }

        Question question = new Question(String.valueOf(Questions.MAX_SIZE), "Question");

        questions.update(QUESTION_ID + 1, question);

        assertThat(questions.getById(QUESTION_ID + 1).isDeleted()).isTrue();
    }

    @Test
    void 수정_대상이_없을_경우_예외발생() {
        Questions questions = new Questions();

        assertThatThrownBy(() -> questions.getById(QUESTION_ID)).isInstanceOf(NoSuchElementException.class);
    }
}