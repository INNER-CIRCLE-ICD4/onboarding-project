package com.wlghsp.querybox.domain.response

import com.wlghsp.querybox.domain.survey.QuestionType
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.assertj.core.api.AssertionsForInterfaceTypes.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class AnswersTest {

    @DisplayName("중복 없는 유효한 응답 리스트로 Answers 생성 성공")
    @Test
    fun create_validAnswers() {
        val answer1 = Answer.of(
            questionId = 1L,
            questionName = "취미?",
            questionType = QuestionType.SHORT_TEXT,
            answerValue = "독서"
        )
        val answer2 = Answer.of(
            questionId = 2L,
            questionName = "선호하는 언어?",
            questionType = QuestionType.SINGLE_CHOICE,
            selectedOptionIds = listOf(1L)
        )

        val answers = Answers.of(listOf(answer1, answer2))

        assertThat(answers.values()).hasSize(2)
        assertThat(answers.values().map { it.questionId }).containsExactly(1L, 2L)
    }

    @DisplayName("응답이 비어있으면 예외 발생")
    @Test
    fun throwsException_whenEmptyAnswers() {
        assertThatThrownBy {
            Answers.of(emptyList())
        }.isInstanceOf(IllegalArgumentException::class.java)
            .hasMessageContaining("응답은 하나 이상의 답변을 포함해야 합니다.")
    }

    @DisplayName("동일한 문항 ID로 응답이 중복되면 예외 발생")
    @Test
    fun throwsException_whenDuplicateQuestionId() {
        val answer1 = Answer.of(
            questionId = 1L,
            questionName = "취미?",
            questionType = QuestionType.SHORT_TEXT,
            answerValue = "영화"
        )
        val answer2 = Answer.of(
            questionId = 1L,
            questionName = "취미?",
            questionType = QuestionType.SHORT_TEXT,
            answerValue = "여행"
        )

        assertThatThrownBy { Answers.of(listOf(answer1, answer2)) }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessageContaining("같은 문항에 중복 응답할 수 없습니다.")
    }

    @DisplayName("질문 키워드와 답변 키워드로 필터링된 Answer만 반환")
    @Test
    fun filterAnswer_byKeywords() {
        val answer1 = Answer.of(
            questionId = 1L,
            questionName = "좋아하는 음식?",
            questionType = QuestionType.SHORT_TEXT,
            answerValue = "초밥"
        )
        val answer2 = Answer.of(
            questionId = 2L,
            questionName = "좋아하는 색상?",
            questionType = QuestionType.SHORT_TEXT,
            answerValue = "파랑"
        )
        val answer3 = Answer.of(
            questionId = 3L,
            questionName = "좋아하는 운동?",
            questionType = QuestionType.MULTIPLE_CHOICE,
            selectedOptionIds = listOf(1L, 2L),
            selectedOptionTexts = listOf("축구", "농구")
        )

        val answers = Answers.of(listOf(answer1, answer2, answer3))
        val result = answers.filterAnswer("음식", "초밥")

        assertThat(result).containsExactly(answer1)
    }
}
