package com.onboarding.model.survey;

import com.onboarding.model.QuestionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;

class QuestionTest {
    private Question question1;

    @BeforeEach
    void init() {
        question1 = new Question("1", "Question1", "Description", QuestionType.SINGLE_CHOICE, new Options("QUESTION1 OPTION1"));
    }

    @Test
    @DisplayName("ID, 항목의 이름은 필수")
    void 유효성_검증() {
        assertAll(
                () -> assertThatThrownBy(() -> new Question("", "Question1")).isInstanceOf(IllegalArgumentException.class),
                () -> assertThatThrownBy(() -> new Question("1", "")).isInstanceOf(IllegalArgumentException.class),
                () -> assertThatThrownBy(() -> new Question("1", null)).isInstanceOf(IllegalArgumentException.class)
        );
    }

    @Test
    void 옵션_추가() {
        Question result = new Question("1", "Question1", "Description", QuestionType.SINGLE_CHOICE);
        result.addOption("QUESTION1 OPTION1");

        assertThat(result).isEqualTo(question1);
    }
}