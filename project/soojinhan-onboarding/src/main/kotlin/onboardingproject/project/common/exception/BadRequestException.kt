package onboardingproject.project.common.exception

import org.springframework.http.HttpStatus

/**
 * packageName : onboardingproject.project.common.exception
 * fileName    : BadRequestException
 * author      : hsj
 * date        : 2025. 6. 16.
 * description :
 */
class BadRequestException : BootException {
    constructor() : super(HttpStatus.BAD_REQUEST)
    constructor(message: String?) : super(HttpStatus.BAD_REQUEST, message)
}