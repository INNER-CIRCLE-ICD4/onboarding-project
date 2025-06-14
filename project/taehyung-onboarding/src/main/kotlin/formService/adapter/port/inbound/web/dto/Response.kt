package formService.adapter.port.inbound.web.dto

import org.springframework.http.HttpStatus

data class Success<T>(
    val message: String,
    val status: HttpStatus,
    val data: T,
)

data class Fail(
    val message: String,
    val status: HttpStatus,
    val errorCode: String,
)
