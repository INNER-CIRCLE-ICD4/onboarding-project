package com.okdori.surveyservice.dto

import com.okdori.surveyservice.exception.BootException
import org.springframework.http.HttpStatus
import java.time.LocalDateTime

/**
 * author       : okdori
 * date         : 2025. 6. 10.
 * description  :
 */

class Payload<T> {
    var status: Int
    var message: String
    var path: String
    var timestamp: LocalDateTime? = null
    var error: String? = null
    var datas: T? = null

    init {
        timestamp = LocalDateTime.now()
    }

    constructor(
        status: HttpStatus,
        message: String,
        path: String,
    ) {
        this.status = status.value()
        this.message = message
        this.path = path
    }

    constructor(
        status: HttpStatus,
        message: String,
        path: String,
        datas: T,
    ) : this(status, message, path) {
        this.message = message
        this.datas = datas
    }

    constructor(
        message: String,
        path: String,
        be: BootException,
    ) : this(be.status, message, path) {
        this.error = be.status.reasonPhrase
    }
}
