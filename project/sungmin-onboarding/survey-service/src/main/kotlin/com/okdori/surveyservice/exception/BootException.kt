package com.okdori.surveyservice.exception

import org.springframework.http.HttpStatus

/**
 * author       : okdori
 * date         : 2025. 6. 10.
 * description  :
 */

open class BootException : RuntimeException {
    val status: HttpStatus

    constructor(code: HttpStatus) : super(code.reasonPhrase) {
        status = code
    }

    constructor(message: String?) : super(message) {
        status = HttpStatus.INTERNAL_SERVER_ERROR
    }

    constructor(className: Class<*>) {
        status = HttpStatus.INTERNAL_SERVER_ERROR
        val trace = arrayOf(StackTraceElement(className.toString(), "", "", 0))
        stackTrace = trace
    }

    constructor(code: HttpStatus, message: String?) : super(message) {
        status = code
    }

    constructor(code: HttpStatus, className: Class<*>?) : this(code) {
        val trace = arrayOf(StackTraceElement(className.toString(), "", "", 0))
        stackTrace = trace
    }

    constructor(className: Class<*>, message: String?) : this(message) {
        val trace = arrayOf(StackTraceElement(className.toString(), "", "", 0))
        stackTrace = trace
    }

    constructor(code: HttpStatus, className: Class<*>?, message: String?) : this(code, message) {
        val trace = arrayOf(StackTraceElement(className.toString(), "", "", 0))
        stackTrace = trace
    }

    constructor(e: Exception) : this(e.message) {
        stackTrace = e.stackTrace
    }

    companion object {
        private const val serialVersionUID = -7396840248291444011L
    }
}

