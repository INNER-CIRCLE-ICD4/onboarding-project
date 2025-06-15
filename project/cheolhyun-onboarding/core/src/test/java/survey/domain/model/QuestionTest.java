package survey.domain.model;

import com.onboarding.model.Options;
import com.onboarding.model.Question;
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
        question1 = new Question("Question1", "Description", QuestionType.SINGLE_CHOICE, new Options("QUESTION1 OPTION1"));
    }

    @Test
    @DisplayName("null과 빈값 유효성 검증")
    void validEmpty() {
        assertAll(
                () -> assertThatThrownBy(() -> new Question("")).isInstanceOf(IllegalArgumentException.class),
                () -> assertThatThrownBy(() -> new Question(null)).isInstanceOf(IllegalArgumentException.class)
        );
    }

    @Test
    void addOption() {
        Question result = new Question("Question1", "Description", QuestionType.SINGLE_CHOICE);
        result.addOption("QUESTION1 OPTION1");

        assertThat(result).isEqualTo(question1);
    }

    @Test
    void updateOption() {
        Question result = new Question("Question1", "Description");

        result.updateOption();
    }
}