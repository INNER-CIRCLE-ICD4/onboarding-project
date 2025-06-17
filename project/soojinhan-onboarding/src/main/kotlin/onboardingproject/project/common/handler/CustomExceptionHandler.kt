package onboardingproject.project.common.handler

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import onboardingproject.project.common.exception.BadRequestException
import onboardingproject.project.common.exception.NotFoundException
import onboardingproject.project.common.domain.ErrorMessage
import onboardingproject.project.common.dto.ResponseDto
import org.hibernate.query.sqm.tree.SqmNode.log
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

/**
 * packageName : onboardingproject.project.common.handler
 * fileName    : CustomExceptionHandler
 * author      : hsj
 * date        : 2025. 6. 14.
 * description :
 */

@RestControllerAdvice
class CustomExceptionHandler {

    @ExceptionHandler(Exception::class)
    fun defaultExceptionHandler(
        request: HttpServletRequest,
        response: HttpServletResponse,
        e: Exception
    ): ResponseDto<String> {
        val message = "\n=*=*=*=    Exception     =*=*=*=\n"
        log.error(message + e.message, e)
        response.status = HttpStatus.INTERNAL_SERVER_ERROR.value()
        return ResponseDto(
            path = request.servletPath,
            status = HttpStatus.INTERNAL_SERVER_ERROR.value(),
            errorMessage = ErrorMessage.INTERNAL_SERVER_ERROR.message
        )
    }

    @ExceptionHandler(NullPointerException::class)
    fun nullExceptionHandler(
        request: HttpServletRequest,
        response: HttpServletResponse,
        e: NullPointerException
    ): ResponseDto<String> {
        val message = "\n=*=*=*=     NullPointerException     =*=*=*=\n"
        log.error(message + e.message, e)
        response.status = HttpStatus.INTERNAL_SERVER_ERROR.value()
        return ResponseDto(
            path = request.servletPath,
            status = HttpStatus.INTERNAL_SERVER_ERROR.value(),
            errorMessage = ErrorMessage.INTERNAL_SERVER_ERROR.message
        )
    }

    @ExceptionHandler(BadRequestException::class)
    fun badRequestExceptionHandler(
        request: HttpServletRequest,
        response: HttpServletResponse,
        e: BadRequestException
    ): ResponseDto<String> {
        val message = "\n=*=*=*=    BadRequestException     =*=*=*=\n"
        log.error(message + e.message, e)
        response.status = HttpStatus.BAD_REQUEST.value()
        return ResponseDto(
            path = request.servletPath,
            status = HttpStatus.BAD_REQUEST.value(),
            errorMessage = e.message
        )
    }

    @ExceptionHandler(NotFoundException::class)
    fun notFoundExceptionHandler(
        request: HttpServletRequest,
        response: HttpServletResponse,
        e: NotFoundException
    ): ResponseDto<String> {
        val message = "\n=*=*=*=     NotFoundException     =*=*=*=\n"
        log.error(message + e.message, e)
        response.status = HttpStatus.NOT_FOUND.value()
        return ResponseDto(
            path = request.servletPath,
            status = HttpStatus.NOT_FOUND.value(),
            errorMessage = e.message
        )
    }
}