package onboardingproject.project.common.exception

import org.springframework.http.HttpStatus

/**
 * packageName : onboardingproject.project.common.exception
 * fileName    : BootException
 * author      : hsj
 * date        : 2025. 6. 16.
 * description :
 */
open class BootException : RuntimeException {
    private val status: HttpStatus

    constructor(code: HttpStatus) : super(code.reasonPhrase) {
        status = code
    }

    constructor(message: String?) : super(message) {
        status = HttpStatus.INTERNAL_SERVER_ERROR
    }

    constructor(code: HttpStatus, message: String?) : super(message) {
        status = code
    }
}