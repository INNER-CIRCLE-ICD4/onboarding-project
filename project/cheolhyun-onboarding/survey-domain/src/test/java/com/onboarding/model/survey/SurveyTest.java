package com.onboarding.model.survey;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class SurveyTest {
    @Test
    @DisplayName("null과 빈값 유효성 검증")
    void 설문지_빈값_검증() {
        assertAll(
                () -> assertThatThrownBy(() -> new Survey("")).isInstanceOf(IllegalArgumentException.class),
                () -> assertThatThrownBy(() -> new Survey(null)).isInstanceOf(IllegalArgumentException.class)
        );
    }
}