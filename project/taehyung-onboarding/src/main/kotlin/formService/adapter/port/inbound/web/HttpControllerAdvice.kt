package formService.adapter.port.inbound.web

import formService.adapter.port.inbound.web.dto.Fail
import formService.exception.HttpRequestException
import formService.exception.message.ExceptionMessage
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class HttpControllerAdvice {
    private val logger = LoggerFactory.getLogger(HttpControllerAdvice::class.java)

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun validRequestDto(e: MethodArgumentNotValidException): Fail =
        Fail(
            message = e.bindingResult.allErrors[0].defaultMessage ?: ExceptionMessage.BAD_REQUEST_DEFAULT.message,
            status = HttpStatus.BAD_REQUEST,
            errorCode = ExceptionMessage.BAD_REQUEST_DEFAULT.code.code,
        )

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun validReadMessage(e: HttpMessageNotReadableException): Fail {
        logger.error(e.message)

        return Fail(
            message = ExceptionMessage.BAD_REQUEST_DEFAULT.message,
            status = HttpStatus.INTERNAL_SERVER_ERROR,
            errorCode = ExceptionMessage.BAD_REQUEST_DEFAULT.code.code,
        )
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpRequestException::class)
    fun validRequest(e: HttpRequestException): Fail {
        logger.error(e.message)

        return Fail(
            message = e.message ?: ExceptionMessage.INTERNAL_ERROR_MESSAGE.message,
            status = e.statusCode,
            errorCode = ExceptionMessage.BAD_REQUEST_DEFAULT.code.code,
        )
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Error::class)
    fun invokeInternalServerError(e: Error): Fail {
        logger.error(e.message)

        return Fail(
            message = ExceptionMessage.INTERNAL_ERROR_MESSAGE.message,
            status = HttpStatus.INTERNAL_SERVER_ERROR,
            errorCode = ExceptionMessage.INTERNAL_ERROR_MESSAGE.code.code,
        )
    }
}
