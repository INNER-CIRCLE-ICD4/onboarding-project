package onboardingproject.project.common.exception

import org.springframework.http.HttpStatus

/**
 * packageName : onboardingproject.project.common.exception
 * fileName    : NotFoundException
 * author      : hsj
 * date        : 2025. 6. 16.
 * description :
 */
class NotFoundException : BootException {
    constructor() : super(HttpStatus.NOT_FOUND)
    constructor(message: String?) : super(HttpStatus.NOT_FOUND, message)
}