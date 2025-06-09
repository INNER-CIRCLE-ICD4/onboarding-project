package fc.innercircle.sanghyukonboarding.survey.domain.model.vo

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class InputTypeTest : DescribeSpec({
    describe("InputType 열거형 테스트") {
        context("fromString 메서드") {
            it("대문자 문자열을 InputType으로 변환할 수 있어야 한다") {
                // given
                val textType = "TEXT"
                val longTextType = "LONG_TEXT"
                val radioType = "RADIO"
                val checkboxType = "CHECKBOX"
                val selectType = "SELECT"

                // when & then
                InputType.fromString(textType) shouldBe InputType.TEXT
                InputType.fromString(longTextType) shouldBe InputType.LONG_TEXT
                InputType.fromString(radioType) shouldBe InputType.RADIO
                InputType.fromString(checkboxType) shouldBe InputType.CHECKBOX
                InputType.fromString(selectType) shouldBe InputType.SELECT
            }

            it("소문자 문자열을 InputType으로 변환할 수 있어야 한다") {
                // given
                val textType = "text"
                val longTextType = "long_text"
                val radioType = "radio"
                val checkboxType = "checkbox"
                val selectType = "select"

                // when & then
                InputType.fromString(textType) shouldBe InputType.TEXT
                InputType.fromString(longTextType) shouldBe InputType.LONG_TEXT
                InputType.fromString(radioType) shouldBe InputType.RADIO
                InputType.fromString(checkboxType) shouldBe InputType.CHECKBOX
                InputType.fromString(selectType) shouldBe InputType.SELECT
            }

            it("유효하지 않은 문자열을 변환하면 예외가 발생해야 한다") {
                // given
                val invalidType = "INVALID_TYPE"

                // when & then
                shouldThrow<IllegalArgumentException> {
                    InputType.fromString(invalidType)
                }
            }
        }

        context("isValidType 메서드") {
            it("유효한 타입은 true를 반환해야 한다") {
                // given
                val validTypes = listOf("TEXT", "LONG_TEXT", "RADIO", "CHECKBOX", "SELECT")

                // when & then
                validTypes.forEach { type ->
                    InputType.isValidType(type) shouldBe true
                }
            }

            it("소문자로 된 유효한 타입은 true를 반환해야 한다") {
                // given
                val validTypes = listOf("text", "long_text", "radio", "checkbox", "select")

                // when & then
                validTypes.forEach { type ->
                    InputType.isValidType(type) shouldBe true
                }
            }

            it("유효하지 않은 타입은 false를 반환해야 한다") {
                // given
                val invalidTypes = listOf("INVALID_TYPE", "OTHER", "", "123")

                // when & then
                invalidTypes.forEach { type ->
                    InputType.isValidType(type) shouldBe false
                }
            }
        }
    }
})
