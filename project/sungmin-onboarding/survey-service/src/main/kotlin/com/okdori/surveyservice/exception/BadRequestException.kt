package com.okdori.surveyservice.exception

import org.springframework.http.HttpStatus

/**
 * author       : okdori
 * date         : 2025. 6. 10.
 * description  :
 */

class BadRequestException : BootException {
    constructor() : super(HttpStatus.BAD_REQUEST)
    constructor(code: HttpStatus) : super(code)
    constructor(errorCode: ErrorCode) : super(HttpStatus.BAD_REQUEST, errorCode.message)
    constructor(message: String?) : super(HttpStatus.BAD_REQUEST, message)
    constructor(className: Class<*>?) : super(HttpStatus.BAD_REQUEST, className)
    constructor(className: Class<*>?, message: String?) : super(HttpStatus.BAD_REQUEST, className, message)
    constructor(e: Exception) : super(e)

    companion object {
        private const val serialVersionUID = -4402223911213242323L
    }
}
