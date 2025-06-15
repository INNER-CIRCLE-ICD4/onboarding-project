package fc.innercircle.sanghyukonboarding.common.interfaces.rest

import fc.innercircle.sanghyukonboarding.common.domain.exception.CustomException
import fc.innercircle.sanghyukonboarding.common.domain.exception.ErrorCode
import fc.innercircle.sanghyukonboarding.common.interfaces.rest.dto.response.ApiError
import org.slf4j.LoggerFactory.getLogger
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    private val log = getLogger(GlobalExceptionHandler::class.java.name)

    @ExceptionHandler(CustomException::class)
    fun handleCustomException(e: CustomException): ResponseEntity<ApiError> {
        val errorCode: ErrorCode.FormattedErrorCode = e.errorCode
        log.error("CustomException occurred, code: ${errorCode.code}, message: ${errorCode.message}", e)
        val errorResponse = ApiError.from(errorCode)
        return ResponseEntity
            .status(errorCode.statusCode)
            .body(errorResponse)
    }

    @ExceptionHandler(RuntimeException::class)
    fun handleRuntimeException(e: RuntimeException): ResponseEntity<ApiError> {
        log.error("RuntimeException occurred", e)
        val errorResponse = ApiError.from(ErrorCode.SERVER_ERROR)
        return ResponseEntity
            .status(ErrorCode.SERVER_ERROR.status)
            .body(errorResponse)
    }
}
