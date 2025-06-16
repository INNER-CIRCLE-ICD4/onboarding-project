package survey.domain.model;

import com.onboarding.model.Option;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class OptionTest {
    @Test
    @DisplayName("null과 빈값 유효성 검증")
    void 옵션_빈값_검증() {
        assertAll(
                () -> assertThatThrownBy(() -> new Option("")).isInstanceOf(IllegalArgumentException.class),
                () -> assertThatThrownBy(() -> new Option(null)).isInstanceOf(IllegalArgumentException.class)
        );
    }
}