package com.wlghsp.querybox.domain.survey

import com.wlghsp.querybox.ui.dto.QuestionUpdateRequest
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.assertj.core.api.AssertionsForInterfaceTypes.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class QuestionTest {

    @DisplayName("단답형/장문형 항목은 옵션 없이 생성 성공")
    @Test
    fun questionCreate_withText_success() {
        val shortTextQuestion = Question(
            name = "나의 별명",
            description = "별명을 적어주세요.",
            type = QuestionType.SHORT_TEXT,
            required = true,
            options = null,
        )
        val longTextQuestion = Question(
            name = "자기소개",
            description = "자유롭게 작성해주세요.",
            type = QuestionType.LONG_TEXT,
            required = true,
            options = null,
        )

        assertThat(shortTextQuestion.name).isEqualTo("나의 별명")
        assertThat(shortTextQuestion.description).isEqualTo("별명을 적어주세요.")
        assertThat(shortTextQuestion.type).isEqualTo(QuestionType.SHORT_TEXT)
        assertThat(shortTextQuestion.options).isNull()

        assertThat(longTextQuestion.name).isEqualTo("자기소개")
        assertThat(longTextQuestion.description).isEqualTo("자유롭게 작성해주세요.")
        assertThat(longTextQuestion.type).isEqualTo(QuestionType.LONG_TEXT)
        assertThat(longTextQuestion.options).isNull()
    }


    @DisplayName("단일 선택/다중 선택 항목은 옵션과 함께 생성 성공")
    @Test
    fun questionCreate_withChoice_success() {
        val options = Options(listOf(Option("A"), Option("B")))

        val singleChoiceQuestion = Question(
            name = "당신의 성별은?",
            description = "해당되는 성별을 선택해주세요.",
            type = QuestionType.SINGLE_CHOICE,
            required = true,
            options = options,
        )

        val multipleChoiceQuestion = Question(
            name = "좋아하는 언어를 모두 선택하세요",
            description = "복수 선택 가능합니다.",
            type = QuestionType.MULTIPLE_CHOICE,
            required = true,
            options = options,
        )

        assertThat(singleChoiceQuestion.name).isEqualTo("당신의 성별은?")
        assertThat(singleChoiceQuestion.type).isEqualTo(QuestionType.SINGLE_CHOICE)
        assertThat(singleChoiceQuestion.options?.options).hasSize(2)

        assertThat(multipleChoiceQuestion.name).isEqualTo("좋아하는 언어를 모두 선택하세요")
        assertThat(multipleChoiceQuestion.type).isEqualTo(QuestionType.MULTIPLE_CHOICE)
        assertThat(multipleChoiceQuestion.options?.options).hasSize(2)
    }

    @DisplayName("주관식 항목은 옵션이 있으면 예외 발생")
    @Test
    fun longTextQuestionCreateExceptionTest() {
        assertThatThrownBy {
            Question(
                name = "",
                description = "자유롭게 작성해주세요.",
                type = QuestionType.LONG_TEXT,
                required = true,
                options = Options(listOf(Option("A"))),
            ) }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessageContaining("항목 이름은 비어 있을 수 없습니다.")
    }

    @DisplayName("항목의 이름이 없으면 예외 발생")
    @Test
    fun questionWithoutNameCreateExceptionTest() {
        assertThatThrownBy {
            Question(
                name = "",
                description = "자유롭게 작성해주세요.",
                type = QuestionType.LONG_TEXT,
                required = true,
                options = null,
            )
        }.isInstanceOf(IllegalArgumentException::class.java)
            .hasMessageContaining("항목 이름은 비어 있을 수 없습니다.")
    }

    @DisplayName("updateFrom 호출 시 필드가 정상적으로 수정")
    @Test
    fun questionUpdateFromSuccess() {
        val original = Question(
            name = "원래 이름",
            description = "원래 설명",
            type = QuestionType.SHORT_TEXT,
            required = false,
            options = null
        )

        val updateRequest = QuestionUpdateRequest(
            id = original.id,
            name = "변경된 이름",
            description = "변경된 설명",
            type = QuestionType.LONG_TEXT,
            required = true,
            options = null
        )

        // when
        original.updateFrom(updateRequest)

        // then
        assertThat(original.name).isEqualTo("변경된 이름")
        assertThat(original.description).isEqualTo("변경된 설명")
        assertThat(original.type).isEqualTo(QuestionType.LONG_TEXT)
        assertThat(original.required).isTrue()
    }

}
