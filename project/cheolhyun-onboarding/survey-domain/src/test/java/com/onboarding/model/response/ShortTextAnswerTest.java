package com.onboarding.model.response;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class ShortTextAnswerTest {

    @Test
    @DisplayName("ID, 응답값이 null 이거나 공백이면 오류")
    void 단답형_입력값_검증() {
        assertAll(
                () -> assertThatThrownBy(() -> ShortTextAnswer.of("", "Test")).isInstanceOf(IllegalArgumentException.class),
                () -> assertThatThrownBy(() -> ShortTextAnswer.of("   ", "Test")).isInstanceOf(IllegalArgumentException.class),
                () -> assertThatThrownBy(() -> ShortTextAnswer.of(null, "Test")).isInstanceOf(IllegalArgumentException.class),
                () -> assertThatThrownBy(() -> ShortTextAnswer.of("Question1", "")).isInstanceOf(IllegalArgumentException.class),
                () -> assertThatThrownBy(() -> ShortTextAnswer.of("Question1", "   ")).isInstanceOf(IllegalArgumentException.class),
                () -> assertThatThrownBy(() -> ShortTextAnswer.of("Question1", null)).isInstanceOf(IllegalArgumentException.class)
        );
    }
}