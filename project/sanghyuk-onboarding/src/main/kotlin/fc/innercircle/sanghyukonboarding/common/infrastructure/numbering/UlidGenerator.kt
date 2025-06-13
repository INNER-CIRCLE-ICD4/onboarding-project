package fc.innercircle.sanghyukonboarding.common.infrastructure.numbering

import com.github.f4b6a3.ulid.UlidCreator
import fc.innercircle.sanghyukonboarding.common.domain.service.IdGenerator
import org.springframework.stereotype.Component

@Component
class UlidGenerator : IdGenerator {
    override fun nextId(): String {
        return UlidCreator.getUlid().toString()
    }
}
