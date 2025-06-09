package fc.innercircle.sanghyukonboarding.surveyreply.model.vo

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class SelectedItemOptionsTest : DescribeSpec({
    describe("SelectedItemOptions 클래스 테스트") {
        context("SelectedItemOptions 생성") {
            it("기본 생성자로 빈 리스트를 가진 SelectedItemOptions를 생성할 수 있어야 한다") {
                // when
                val selectedItemOptions = SelectedItemOptions()

                // then
                selectedItemOptions.texts shouldBe emptyList()
            }

            it("텍스트 리스트를 가진 SelectedItemOptions를 생성할 수 있어야 한다") {
                // given
                val texts = listOf("옵션1", "옵션2", "옵션3")

                // when
                val selectedItemOptions = SelectedItemOptions(texts)

                // then
                selectedItemOptions.texts shouldBe texts
            }
        }

        context("SelectedItemOptionsConverter 테스트") {
            val converter = SelectedItemOptions.SelectedItemOptionsConverter()

            it("빈 리스트를 가진 SelectedItemOptions를 DB 컬럼으로 변환할 수 있어야 한다") {
                // given
                val selectedItemOptions = SelectedItemOptions()

                // when
                val dbColumn = converter.convertToDatabaseColumn(selectedItemOptions)

                // then
                dbColumn shouldBe ""
            }

            it("하나의 항목을 가진 SelectedItemOptions를 DB 컬럼으로 변환할 수 있어야 한다") {
                // given
                val selectedItemOptions = SelectedItemOptions(listOf("옵션1"))

                // when
                val dbColumn = converter.convertToDatabaseColumn(selectedItemOptions)

                // then
                dbColumn shouldBe "옵션1"
            }

            it("여러 항목을 가진 SelectedItemOptions를 DB 컬럼으로 변환할 수 있어야 한다") {
                // given
                val selectedItemOptions = SelectedItemOptions(listOf("옵션1", "옵션2", "옵션3"))

                // when
                val dbColumn = converter.convertToDatabaseColumn(selectedItemOptions)

                // then
                dbColumn shouldBe "옵션1,옵션2,옵션3"
            }

            it("빈 문자열을 SelectedItemOptions로 변환할 수 있어야 한다") {
                // given
                val dbData = ""

                // when
                val selectedItemOptions = converter.convertToEntityAttribute(dbData)

                // then
                selectedItemOptions.texts shouldBe listOf("")
            }

            it("하나의 항목을 가진 문자열을 SelectedItemOptions로 변환할 수 있어야 한다") {
                // given
                val dbData = "옵션1"

                // when
                val selectedItemOptions = converter.convertToEntityAttribute(dbData)

                // then
                selectedItemOptions.texts shouldBe listOf("옵션1")
            }

            it("여러 항목을 가진 문자열을 SelectedItemOptions로 변환할 수 있어야 한다") {
                // given
                val dbData = "옵션1,옵션2,옵션3"

                // when
                val selectedItemOptions = converter.convertToEntityAttribute(dbData)

                // then
                selectedItemOptions.texts shouldBe listOf("옵션1", "옵션2", "옵션3")
            }

            it("공백이 포함된 항목을 가진 문자열을 SelectedItemOptions로 변환할 수 있어야 한다") {
                // given
                val dbData = "옵션1, 옵션2 , 옵션3"

                // when
                val selectedItemOptions = converter.convertToEntityAttribute(dbData)

                // then
                selectedItemOptions.texts shouldBe listOf("옵션1", "옵션2", "옵션3")
            }
        }
    }
})
