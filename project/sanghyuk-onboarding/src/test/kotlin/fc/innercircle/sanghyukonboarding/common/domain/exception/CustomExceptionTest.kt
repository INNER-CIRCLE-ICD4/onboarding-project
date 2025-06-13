package fc.innercircle.sanghyukonboarding.common.domain.exception

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import java.text.MessageFormat

class CustomExceptionTest : DescribeSpec({
    describe("CustomException 클래스 테스트") {
        context("CustomException 생성") {
            it("ErrorCode만 받는 생성자로 CustomException을 생성할 수 있어야 한다") {
                // given
                val errorCode = ErrorCode.INVALID_FORM_TITLE
                val args = arrayOf("테스트 제목")
                val formattedMessage = MessageFormat.format(errorCode.message, *args)

                // when
                val exception = CustomException(errorCode.withArgs(*args))

                // then
                exception.formattedErrorCode.status shouldBe errorCode.status
                exception.formattedErrorCode.statusCode shouldBe errorCode.status.value().toString()
                exception.formattedErrorCode.message shouldBe formattedMessage
                exception.message shouldBe formattedMessage
                exception.cause shouldBe null
            }

            it("ErrorCode만 받는 두 번째 생성자로 CustomException을 생성할 수 있어야 한다") {
                // given
                val errorCode = ErrorCode.INVALID_FORM_DESCRIPTION
                val formattedMessage = errorCode.message

                // when
                val exception = CustomException(errorCode)

                // then
                exception.formattedErrorCode.status shouldBe errorCode.status
                exception.formattedErrorCode.statusCode shouldBe errorCode.status.value().toString()
                exception.formattedErrorCode.message shouldBe formattedMessage
                exception.message shouldBe formattedMessage
                exception.cause shouldBe null
            }

            it("ErrorCode와 원인 예외를 함께 받는 생성자로 CustomException을 생성할 수 있어야 한다") {
                // given
                val errorCode = ErrorCode.INVALID_QUESTION_TITLE
                val cause = RuntimeException("원인 예외")
                val formattedMessage = errorCode.message

                // when
                val exception = CustomException(errorCode, cause)

                // then
                exception.formattedErrorCode.status shouldBe errorCode.status
                exception.formattedErrorCode.statusCode shouldBe errorCode.status.value().toString()
                exception.formattedErrorCode.message shouldBe formattedMessage
                exception.message shouldBe formattedMessage
                exception.cause shouldBe cause
            }

            it("FormattedErrorCode와 원인 예외를 함께 받는 생성자로 CustomException을 생성할 수 있어야 한다") {
                // given
                val errorCode = ErrorCode.INVALID_QUESTION_DESCRIPTION
                val args = arrayOf("테스트 설명")
                val formattedErrorCode = errorCode.withArgs(*args)
                val cause = RuntimeException("원인 예외")
                val formattedMessage = MessageFormat.format(errorCode.message, *args)

                // when
                val exception = CustomException(formattedErrorCode, cause)

                // then
                exception.formattedErrorCode shouldBe formattedErrorCode
                exception.formattedErrorCode.status shouldBe errorCode.status
                exception.formattedErrorCode.statusCode shouldBe errorCode.status.value().toString()
                exception.formattedErrorCode.message shouldBe formattedMessage
                exception.message shouldBe formattedMessage
                exception.cause shouldBe cause
            }
        }
    }
})
