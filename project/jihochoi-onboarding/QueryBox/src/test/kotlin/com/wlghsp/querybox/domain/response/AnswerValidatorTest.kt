package com.wlghsp.querybox.domain.response

import com.wlghsp.querybox.domain.survey.QuestionType
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class AnswerValidatorTest {

    @DisplayName("주관식 응답이 비어있으면 예외 발생")
    @Test
    fun throwsExceptionWhenShortAnswerIsBlank() {
        assertThatThrownBy {
            AnswerValidator.validate(
                questionType = QuestionType.SHORT_TEXT,
                answerValue = "",
                selectedOptionIds = null
            )
        }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessageContaining("주관식 응답은 비어 있을 수 없습니다.")
    }

    @DisplayName("선택형 응답이 비어있으면 예외 발생")
    @Test
    fun throwsExceptionWhenChoiceAnswerIsEmpty() {
        assertThatThrownBy {
            AnswerValidator.validate(
                questionType = QuestionType.MULTIPLE_CHOICE,
                answerValue = null,
                selectedOptionIds = emptyList()
            )
        }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessageContaining("선택형 응답은 하나 이상 선택해야 합니다.")
    }


}