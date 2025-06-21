package onboardingproject.project.common.dto

import org.springframework.http.HttpStatus
import java.time.LocalDateTime

/**
 * packageName : onboardingproject.project.dto
 * fileName    : ResponseDto
 * author      : hsj
 * date        : 2025. 6. 16.
 * description :
 */
data class ResponseDto<T> (
    val status: Int = HttpStatus.OK.value(),
    val timestamp: LocalDateTime = LocalDateTime.now(),
    val path: String,
    val errorMessage: String? = null,
    val data: T? = null
)