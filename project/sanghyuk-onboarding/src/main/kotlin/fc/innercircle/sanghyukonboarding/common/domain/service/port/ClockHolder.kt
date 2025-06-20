package fc.innercircle.sanghyukonboarding.common.domain.service.port

import java.time.LocalDateTime

interface ClockHolder {
    fun localDateTime(): LocalDateTime
}
