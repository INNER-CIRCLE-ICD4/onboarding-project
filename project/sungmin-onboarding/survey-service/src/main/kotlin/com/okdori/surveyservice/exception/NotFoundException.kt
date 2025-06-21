package com.okdori.surveyservice.exception

import org.springframework.http.HttpStatus

/**
 * author       : okdori
 * date         : 2025. 6. 10.
 * description  :
 */

class NotFoundException : BootException {
    constructor() : super(HttpStatus.NOT_FOUND)
    constructor(code: HttpStatus) : super(code)
    constructor(errorCode: ErrorCode) : super(HttpStatus.NOT_FOUND, errorCode.message)
    constructor(message: String?) : super(HttpStatus.NOT_FOUND, message)
    constructor(className: Class<*>?) : super(HttpStatus.NOT_FOUND, className)
    constructor(className: Class<*>?, message: String?) : super(HttpStatus.NOT_FOUND, className, message)
    constructor(e: Exception) : super(e)

    companion object {
        private const val serialVersionUID = -4920939812423982128L
    }
}
