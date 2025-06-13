package fc.innercircle.sanghyukonboarding.form.domain.model.vo

import io.kotest.assertions.throwables.shouldNotThrow
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class InputTypeTest : FunSpec({
    test("fromString should convert a string to the corresponding InputType") {
        InputType.fromString("TEXT") shouldBe InputType.TEXT
        InputType.fromString("LONG_TEXT") shouldBe InputType.LONG_TEXT
        InputType.fromString("RADIO") shouldBe InputType.RADIO
        InputType.fromString("CHECKBOX") shouldBe InputType.CHECKBOX
        InputType.fromString("SELECT") shouldBe InputType.SELECT

        // Case insensitive
        InputType.fromString("text") shouldBe InputType.TEXT
        InputType.fromString("long_text") shouldBe InputType.LONG_TEXT
    }

    test("fromString should throw IllegalArgumentException for invalid input types") {
        shouldThrow<IllegalArgumentException> {
            InputType.fromString("INVALID_TYPE")
        }
    }

    test("isValidType should return true for valid input types") {
        InputType.isValidType("TEXT") shouldBe true
        InputType.isValidType("LONG_TEXT") shouldBe true
        InputType.isValidType("RADIO") shouldBe true
        InputType.isValidType("CHECKBOX") shouldBe true
        InputType.isValidType("SELECT") shouldBe true

        // Case insensitive
        InputType.isValidType("text") shouldBe true
        InputType.isValidType("long_text") shouldBe true
    }

    test("isValidType should return false for invalid input types") {
        InputType.isValidType("INVALID_TYPE") shouldBe false
        InputType.isValidType("") shouldBe false
    }
})
