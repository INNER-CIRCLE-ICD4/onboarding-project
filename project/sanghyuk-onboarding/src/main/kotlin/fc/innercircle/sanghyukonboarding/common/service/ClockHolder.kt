package fc.innercircle.sanghyukonboarding.common.service

import java.time.LocalDateTime

interface ClockHolder {
    fun localDateTime(): LocalDateTime
}
