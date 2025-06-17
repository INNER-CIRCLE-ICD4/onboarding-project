package com.onboarding.model.response;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class MultipleChoiceAnswerTest {
    @Test
    @DisplayName("ID, 응답값이 null 이거나 공백이면 오류")
    void 다중_선택_입력값_검증() {
        List<String> answerList = List.of("Answer1", "Answer2", "Answer3");

        assertAll(
                () -> assertThatThrownBy(() -> MultipleChoiceAnswer.of("", answerList)).isInstanceOf(IllegalArgumentException.class),
                () -> assertThatThrownBy(() -> MultipleChoiceAnswer.of("   ", answerList)).isInstanceOf(IllegalArgumentException.class),
                () -> assertThatThrownBy(() -> MultipleChoiceAnswer.of(null, answerList)).isInstanceOf(IllegalArgumentException.class),
                () -> assertThatThrownBy(() -> MultipleChoiceAnswer.of("Question1", List.of())).isInstanceOf(IllegalArgumentException.class),
                () -> assertThatThrownBy(() -> MultipleChoiceAnswer.of("Question1", null)).isInstanceOf(IllegalArgumentException.class)
        );
    }
}