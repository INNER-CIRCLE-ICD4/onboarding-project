package com.innercircle.survey.common.adapter

import com.innercircle.survey.common.dto.ErrorResponse
import com.innercircle.survey.common.exception.BusinessException
import com.innercircle.survey.common.exception.ErrorCode
import jakarta.servlet.http.HttpServletRequest
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.validation.BindException
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException

private val logger = KotlinLogging.logger {}

@RestControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(BusinessException::class)
    fun handleBusinessException(
        e: BusinessException,
        request: HttpServletRequest,
    ): ResponseEntity<ErrorResponse> {
        logger.warn { "Business exception occurred: ${e.errorCode.code} - ${e.message}" }

        val response =
            ErrorResponse.of(
                errorCode = e.errorCode,
                message = e.message,
                path = request.requestURI,
            )

        return ResponseEntity
            .status(e.errorCode.status)
            .body(response)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValidException(
        e: MethodArgumentNotValidException,
        request: HttpServletRequest,
    ): ResponseEntity<ErrorResponse> {
        logger.warn { "Validation failed: ${e.bindingResult}" }

        val fieldErrors =
            e.bindingResult.fieldErrors.map { fieldError ->
                ErrorResponse.FieldError(
                    field = fieldError.field,
                    value = fieldError.rejectedValue,
                    reason = fieldError.defaultMessage ?: "유효하지 않은 값입니다.",
                )
            }

        val response =
            ErrorResponse.of(
                errorCode = ErrorCode.INVALID_INPUT_VALUE,
                fieldErrors = fieldErrors,
                path = request.requestURI,
            )

        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(response)
    }

    @ExceptionHandler(BindException::class)
    fun handleBindException(
        e: BindException,
        request: HttpServletRequest,
    ): ResponseEntity<ErrorResponse> {
        logger.warn { "Binding failed: ${e.bindingResult}" }

        val fieldErrors =
            e.bindingResult.fieldErrors.map { fieldError ->
                ErrorResponse.FieldError(
                    field = fieldError.field,
                    value = fieldError.rejectedValue,
                    reason = fieldError.defaultMessage ?: "유효하지 않은 값입니다.",
                )
            }

        val response =
            ErrorResponse.of(
                errorCode = ErrorCode.INVALID_INPUT_VALUE,
                fieldErrors = fieldErrors,
                path = request.requestURI,
            )

        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(response)
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException::class)
    fun handleMethodArgumentTypeMismatchException(
        e: MethodArgumentTypeMismatchException,
        request: HttpServletRequest,
    ): ResponseEntity<ErrorResponse> {
        logger.warn { "Type mismatch: ${e.name} = ${e.value}" }

        val response =
            ErrorResponse.of(
                errorCode = ErrorCode.INVALID_TYPE_VALUE,
                message = "${e.name}의 값이 올바르지 않습니다. (입력값: ${e.value})",
                path = request.requestURI,
            )

        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(response)
    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleHttpMessageNotReadableException(
        e: HttpMessageNotReadableException,
        request: HttpServletRequest,
    ): ResponseEntity<ErrorResponse> {
        logger.warn { "Message not readable: ${e.message}" }

        val response =
            ErrorResponse.of(
                errorCode = ErrorCode.INVALID_INPUT_VALUE,
                message = "요청 본문을 읽을 수 없습니다. JSON 형식을 확인해주세요.",
                path = request.requestURI,
            )

        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(response)
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException::class)
    fun handleHttpRequestMethodNotSupportedException(
        e: HttpRequestMethodNotSupportedException,
        request: HttpServletRequest,
    ): ResponseEntity<ErrorResponse> {
        logger.warn { "Method not supported: ${e.method}" }

        val response =
            ErrorResponse.of(
                errorCode = ErrorCode.METHOD_NOT_ALLOWED,
                message = "${e.method} 메소드는 지원하지 않습니다.",
                path = request.requestURI,
            )

        return ResponseEntity
            .status(HttpStatus.METHOD_NOT_ALLOWED)
            .body(response)
    }

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgumentException(
        e: IllegalArgumentException,
        request: HttpServletRequest,
    ): ResponseEntity<ErrorResponse> {
        logger.warn { "Illegal argument: ${e.message}" }

        val response =
            ErrorResponse.of(
                errorCode = ErrorCode.INVALID_INPUT_VALUE,
                message = e.message ?: "유효하지 않은 입력값입니다.",
                path = request.requestURI,
            )

        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(response)
    }

    @ExceptionHandler(Exception::class)
    fun handleException(
        e: Exception,
        request: HttpServletRequest,
    ): ResponseEntity<ErrorResponse> {
        logger.error(e) { "Unexpected error occurred" }

        val response =
            ErrorResponse.of(
                errorCode = ErrorCode.INTERNAL_SERVER_ERROR,
                path = request.requestURI,
            )

        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(response)
    }
}
