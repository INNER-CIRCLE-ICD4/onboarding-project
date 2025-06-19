package com.wlghsp.querybox.domain.response

import com.fasterxml.jackson.databind.ObjectMapper
import com.wlghsp.querybox.domain.survey.*
import org.assertj.core.api.AssertionsForInterfaceTypes.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class SnapshotFactoryTest {

    private val objectMapper = ObjectMapper()
    private val snapshotFactory = SnapshotFactory(objectMapper)

    @DisplayName("설문과 응답을 JSON 스냅샷으로 저장")
    @Test
    fun create_SnapshotReturnsValidJson() {
        val survey = Survey.of(
            title = "개발자 설문",
            description = "백엔드 기술 조사",
            questions = Questions.of(
                mutableListOf(
                    Question(
                        name = "사용 언어",
                        description = "자주 사용하는 언어를 선택하세요.",
                        type = QuestionType.SINGLE_CHOICE,
                        required = true,
                        options = Options.of(listOf(Option("Java"), Option("Kotlin")))
                    )
                )
            )
        )

        val answers = Answers.of(
            listOf(
                Answer.of(
                    questionId = 1L,
                    questionName = "사용 언어",
                    questionType = QuestionType.SINGLE_CHOICE,
                    answerValue = null,
                    selectedOptionIds = listOf(1L)
                )
            )
        )

        // when
        val snapshot = snapshotFactory.createSnapshot(survey, answers)

        // then
        assertThat(snapshot).isNotBlank()
        assertThat(snapshot).contains("개발자 설문", "사용 언어", "1")
    }
}
