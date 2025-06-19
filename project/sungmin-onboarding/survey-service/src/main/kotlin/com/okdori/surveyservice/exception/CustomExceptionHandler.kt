package com.okdori.surveyservice.exception

import com.okdori.surveyservice.dto.Payload
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import kotlin.reflect.KClass

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
}

inline fun <R> (() -> R).multiCatch(vararg exceptions: KClass<out Throwable>, thenDo: () -> R): R {
    return try {
        this()
    } catch (ex: Exception) {
        if (ex::class in exceptions) thenDo() else throw ex
    }
}

