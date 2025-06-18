package com.onboarding.model.response;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OptionAnswersTest {

    @Test
    void 응답_리스트_검증() {
        assertThatThrownBy(() -> new OptionAnswers(null)).isInstanceOf(IllegalArgumentException.class);
    }
}