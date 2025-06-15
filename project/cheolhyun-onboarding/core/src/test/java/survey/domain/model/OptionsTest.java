package survey.domain.model;

import com.onboarding.model.Options;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class OptionsTest {

    @Test
    @DisplayName("옵션 넣기")
    void add() {
        Options result1 = new Options("Option1");
        Options result2 = new Options();

        result2.add("Option1");

        assertThat(result1).isEqualTo(result2);
    }
}