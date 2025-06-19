package fc.innercircle.sanghyukonboarding.common.infrastructure.clock

import fc.innercircle.sanghyukonboarding.common.domain.service.ClockHolder
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class SystemClockHolder(): ClockHolder {
    override fun localDateTime(): LocalDateTime {
        return LocalDateTime.now()
    }
}
