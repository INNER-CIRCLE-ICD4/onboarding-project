package com.onboarding.model.response;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class SurveyResponseTest {
    @Test
    void 설문_응답지_검증() {
        assertAll(
                () -> assertThatThrownBy(() -> new SurveyResponse(null, new OptionAnswers())).isInstanceOf(IllegalArgumentException.class),
                () -> assertThatThrownBy(() -> new SurveyResponse("", new OptionAnswers())).isInstanceOf(IllegalArgumentException.class)
        );
    }
}