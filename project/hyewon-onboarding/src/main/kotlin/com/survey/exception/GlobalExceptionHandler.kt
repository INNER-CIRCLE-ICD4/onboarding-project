package com.survey.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.ServletWebRequest
import org.springframework.web.context.request.WebRequest
import java.time.Instant

@RestControllerAdvice
class GlobalExceptionHandler {

    /**
     * 공통 에러 응답 객체 생성, 반환
     */
    private fun buildResponse(
        ex: RuntimeException,
        status: HttpStatus,
        request: WebRequest,
        details: String? = null
    ): ResponseEntity<ApiErrorResponse> {
        val path = (request as ServletWebRequest).request.requestURI
        val body = ApiErrorResponse(
            error = ex.message ?: status.reasonPhrase,
            code = status.value(),
            timestamp = Instant.now(),
            path = path,
            details = details
        )
        return ResponseEntity(body, status)
    }

    /**
     * 400에 해당하는 예외
     */
    @ExceptionHandler(
        SurveyItemCountException::class,
        SurveyOptionsMissingException::class,
        RequiredFieldMissingException::class,
        SurveyUpdateFieldMissingException::class,
        ResponseRequiredFieldMissingException::class,
        ResponseOptionNotAllowedException::class,
        ResponseTypeMismatchException::class,
        SearchConditionInvalidException::class,
        SearchOperatorTypeMismatchException::class
    )
    fun handleBadRequest(ex: RuntimeException, request: WebRequest): ResponseEntity<ApiErrorResponse> {
        return buildResponse(ex, HttpStatus.BAD_REQUEST, request)
    }

    /**
     * 404에 해당하는 예외
     */
    @ExceptionHandler(
        SurveyNotFoundException::class,
        ResponseSurveyNotFoundException::class
    )
    fun handleNotFound(ex: RuntimeException, request: WebRequest): ResponseEntity<ApiErrorResponse> {
        return buildResponse(ex, HttpStatus.NOT_FOUND, request)
    }

    /**
     * 409에 해당하는 예외
     */
    @ExceptionHandler(SurveyTypeConflictException::class)
    fun handleConflict(ex: RuntimeException, request: WebRequest): ResponseEntity<ApiErrorResponse> {
        return buildResponse(ex, HttpStatus.CONFLICT, request)
    }

    /**
     * 기타 예외
     */
    @ExceptionHandler(Exception::class)
    fun handleAll(ex: Exception, request: WebRequest): ResponseEntity<ApiErrorResponse> {
        val generic = RuntimeException("내부 서버 오류가 발생했습니다.")
        ex.printStackTrace();
        return buildResponse(generic, HttpStatus.INTERNAL_SERVER_ERROR, request)
    }
}