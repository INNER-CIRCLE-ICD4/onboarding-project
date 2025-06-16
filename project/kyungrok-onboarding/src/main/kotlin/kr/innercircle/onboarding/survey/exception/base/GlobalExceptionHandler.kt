package kr.innercircle.onboarding.survey.exception.base

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import kr.innercircle.onboarding.survey.dto.response.ApiErrorResponse
import kr.innercircle.onboarding.survey.dto.response.ApiResponse
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException

/**
  * packageName : kr.innercircle.onboarding.survey.exception
  * fileName    : GlobalExceptionHandler
  * author      : ckr
  * date        : 25. 6. 12.
  * description :
  */

@RestControllerAdvice
class GlobalExceptionHandler {
    private val logger = LoggerFactory.getLogger(this.javaClass)

    @ExceptionHandler
    fun defaultExceptionHandler(
        e: Exception,
        request: HttpServletRequest,
        response: HttpServletResponse,
    ): ApiResponse {
        logger.error(e.stackTraceToString())
        response.apply { this.status = HttpStatus.INTERNAL_SERVER_ERROR.value() }
        return ApiResponse(ErrorCode.INTERNAL_SERVER_ERROR)
    }

    @ExceptionHandler
    fun badRequestExceptionHandler(
        e: BadRequestException,
        request: HttpServletRequest,
        response: HttpServletResponse,
    ): ApiResponse {
        logger.error(e.stackTraceToString())
        response.apply { this.status = HttpStatus.BAD_REQUEST.value() }
        return ApiResponse(e.errorCode)
    }

    @ExceptionHandler
    fun notFoundExceptionHandler(
        e: NotFoundException,
        request: HttpServletRequest,
        response: HttpServletResponse,
    ): ApiResponse {
        logger.error(e.stackTraceToString())
        response.apply { this.status = HttpStatus.NOT_FOUND.value() }
        return ApiResponse(e.errorCode)
    }

    @ExceptionHandler
    fun httpMessageNotReadableExceptionHandler(
        ex: HttpMessageNotReadableException,
        request: HttpServletRequest,
        response: HttpServletResponse,
    ): ApiResponse {
        logger.error(ex.stackTraceToString())
        response.apply { this.status = HttpStatus.BAD_REQUEST.value() }
        return ApiResponse(ErrorCode.INVALID_HTTP_REQUEST)
    }

    @ExceptionHandler
    fun methodArgumentNotValidExceptionHandler(
        ex: MethodArgumentNotValidException,
        request: HttpServletRequest,
        response: HttpServletResponse,
    ): ApiResponse {
        logger.error(ex.stackTraceToString())
        val bindingResult = ex.bindingResult
        val builder = StringBuilder()
        for (fieldError in bindingResult.fieldErrors) {
            builder.append("[")
            builder.append(fieldError.field)
            builder.append("] ")
            builder.append(fieldError.defaultMessage)
            builder.append(" rejected value: [")
            builder.append(fieldError.rejectedValue)
            builder.append("] ")
        }
        response.apply { this.status = HttpStatus.BAD_REQUEST.value() }
        val errorResponse = ApiErrorResponse(ErrorCode.METHOD_ARGUMENT_NOT_VALID.code, builder.toString())
        return ApiResponse(errorResponse)
    }

    @ExceptionHandler
    fun methodArgumentTypeMismatchExceptionHandler(
        ex: MethodArgumentTypeMismatchException,
        request: HttpServletRequest,
        response: HttpServletResponse,
    ): ApiResponse {
        logger.error(ex.stackTraceToString())
        val builder = StringBuilder()
        builder.append("[")
        builder.append(ex.propertyName)
        builder.append("] required type: [")
        builder.append(ex.requiredType)
        builder.append("] value: [")
        builder.append(ex.value)
        builder.append("] ")
        response.apply { this.status = HttpStatus.BAD_REQUEST.value() }
        val errorResponse = ApiErrorResponse(ErrorCode.METHOD_ARGUMENT_TYPE_MISMATCH.code, builder.toString())
        return ApiResponse(errorResponse)
    }

}