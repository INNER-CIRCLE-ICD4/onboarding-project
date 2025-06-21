package formService.domain

import formService.util.getTsid
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID

class AnswerTest {
    @Test
    @DisplayName("설문 받을 응답 생성 시 유저 이메일, 제출 일자, 응답 값이 필수여야된다.")
    fun answerCreateTest() {
        // given
        val userId = UUID.randomUUID().toString()
        val submittedAt = "2025-01-01T16:00:00"

        // when
        val answer =
            Answer(
                id = getTsid(),
                userId = userId,
                submittedAt = LocalDateTime.of(2025, 1, 1, 16, 0, 0),
                values =
                    listOf(
                        QuestionAnswer(
                            questionId = 1L,
                            answerValue = "value1",
                            answerType = InputType.SHORT_TEXT,
                        ),
                        QuestionAnswer(
                            questionId = 2L,
                            answerValue = "value2",
                            answerType = InputType.SHORT_TEXT,
                        ),
                    ),
            )

        // then
        assertThat(answer.userId).isEqualTo(userId)
        assertThat(answer.submittedAt.format(DateTimeFormatter.ISO_DATE_TIME)).isEqualTo(submittedAt)
        assertThat(answer.values).hasSize(2)
        assertThat(answer.values[0].answerValue).isEqualTo("value1")
        assertThat(answer.values[1].answerValue).isEqualTo("value2")
    }
}
