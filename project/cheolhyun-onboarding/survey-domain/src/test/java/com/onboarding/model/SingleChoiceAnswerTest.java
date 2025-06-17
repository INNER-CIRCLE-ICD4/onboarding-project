package com.onboarding.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class SingleChoiceAnswerTest {

    @Test
    @DisplayName("ID, 응답값이 null 이거나 공백이면 오류")
    void 단일_선택_입력값_검증() {
        assertAll(
                () -> assertThatThrownBy(() -> SingleChoiceAnswer.of("", "Test")).isInstanceOf(IllegalArgumentException.class),
                () -> assertThatThrownBy(() -> SingleChoiceAnswer.of("   ", "Test")).isInstanceOf(IllegalArgumentException.class),
                () -> assertThatThrownBy(() -> SingleChoiceAnswer.of(null, "Test")).isInstanceOf(IllegalArgumentException.class),
                () -> assertThatThrownBy(() -> SingleChoiceAnswer.of("Question1", "")).isInstanceOf(IllegalArgumentException.class),
                () -> assertThatThrownBy(() -> SingleChoiceAnswer.of("Question1", "   ")).isInstanceOf(IllegalArgumentException.class),
                () -> assertThatThrownBy(() -> SingleChoiceAnswer.of("Question1", null)).isInstanceOf(IllegalArgumentException.class)
        );
    }
}