package fc.innercircle.sanghyukonboarding.common.domain.service

import java.time.LocalDateTime

interface ClockHolder {
    fun localDateTime(): LocalDateTime
}
