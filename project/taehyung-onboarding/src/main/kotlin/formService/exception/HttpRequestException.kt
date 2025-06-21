package formService.exception

import org.springframework.http.HttpStatus

sealed class HttpRequestException(
    message: String,
    val statusCode: HttpStatus,
) : Exception(message)
