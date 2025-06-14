package formService.exception

import org.springframework.http.HttpStatus

class BadRequestException(
    message: String,
) : HttpRequestException(message, HttpStatus.BAD_REQUEST)
