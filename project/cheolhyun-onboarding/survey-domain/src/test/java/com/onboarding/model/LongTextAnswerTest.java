package com.onboarding.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class LongTextAnswerTest {

    @Test
    @DisplayName("ID, 응답값이 null 이거나 공백이면 오류")
    void 장문형_입력값_검증() {
        assertAll(
                () -> assertThatThrownBy(() -> LongTextAnswer.of("", "Test")).isInstanceOf(IllegalArgumentException.class),
                () -> assertThatThrownBy(() -> LongTextAnswer.of("   ", "Test")).isInstanceOf(IllegalArgumentException.class),
                () -> assertThatThrownBy(() -> LongTextAnswer.of(null, "Test")).isInstanceOf(IllegalArgumentException.class),
                () -> assertThatThrownBy(() -> LongTextAnswer.of("Question1", "")).isInstanceOf(IllegalArgumentException.class),
                () -> assertThatThrownBy(() -> LongTextAnswer.of("Question1", "   ")).isInstanceOf(IllegalArgumentException.class),
                () -> assertThatThrownBy(() -> LongTextAnswer.of("Question1", null)).isInstanceOf(IllegalArgumentException.class)
        );
    }
}