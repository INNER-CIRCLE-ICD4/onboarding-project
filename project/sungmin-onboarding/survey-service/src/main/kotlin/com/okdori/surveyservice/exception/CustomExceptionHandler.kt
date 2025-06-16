package com.okdori.surveyservice.exception

import com.okdori.surveyservice.dto.Payload
import com.okdori.surveyservice.exception.ErrorCode.*
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import kotlin.reflect.KClass
import kotlin.toString

/**
 * author       : okdori
 * date         : 2025. 6. 10.
 * description  :
 */

@RestControllerAdvice
class CustomExceptionHandler {
    @ExceptionHandler
    fun badReqExHandle(
        e: BadRequestException,
        request: HttpServletRequest,
        response: HttpServletResponse,
    ): Payload<Exception?>? {
        response.apply { this.status = e.status.value() }
        return Payload(e.message.toString(), request.servletPath, e)
    }

    @ExceptionHandler
    fun notFoundExHandle(
        e: NotFoundException,
        request: HttpServletRequest,
        response: HttpServletResponse,
    ): Payload<Exception?>? {
        response.apply { this.status = e.status.value() }
        return Payload(e.message.toString(), request.servletPath, e)
    }

    private val specificErrorMap = mapOf(
        "surveyName.NotBlank" to SURVEY_NAME_REQUIRED,
        "surveyName.Size" to SURVEY_NAME_TOO_LONG,
        "surveyDescription.Size" to SURVEY_DESCRIPTION_TOO_LONG,
        "items.Size" to SURVEY_ITEMS_INVALID,

        "itemName.NotBlank" to ITEM_NAME_REQUIRED,
        "itemName.Size" to ITEM_NAME_TOO_LONG,
        "itemDescription.Size" to ITEM_DESCRIPTION_TOO_LONG,

        "optionName.NotBlank" to OPTION_NAME_REQUIRED,
        "optionName.Size" to OPTION_NAME_TOO_LONG,
    )

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationException(
        e: MethodArgumentNotValidException,
        request: HttpServletRequest,
        response: HttpServletResponse,
    ): Payload<Exception?>? {
        val fieldError = e.bindingResult.fieldErrors.first()
        val key = "${fieldError.field}.${fieldError.code}"

        val errorCode = specificErrorMap[key] ?: ErrorCode.VALIDATION_FAILED

        val message = if (specificErrorMap.containsKey(key)) {
            errorCode.message
        } else {
            fieldError.defaultMessage ?: errorCode.message
        }

        response.apply { this.status = BadRequestException().status.value() }
        return Payload(message, request.servletPath, BadRequestException())
    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleHttpMessageNotReadable(
        e: HttpMessageNotReadableException,
        request: HttpServletRequest,
        response: HttpServletResponse,
    ): Payload<Exception?>? {
        val errorCode = INVALID_REQUEST_FORMAT
        response.apply { this.status = errorCode.status.value() }
        return Payload(errorCode.message, request.servletPath, BadRequestException())
    }
}

inline fun <R> (() -> R).multiCatch(vararg exceptions: KClass<out Throwable>, thenDo: () -> R): R {
    return try {
        this()
    } catch (ex: Exception) {
        if (ex::class in exceptions) thenDo() else throw ex
    }
}

