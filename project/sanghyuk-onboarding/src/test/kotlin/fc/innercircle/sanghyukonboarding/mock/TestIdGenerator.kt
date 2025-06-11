package fc.innercircle.sanghyukonboarding.mock

import fc.innercircle.sanghyukonboarding.common.domain.service.IdGenerator
import java.util.*

/**
 * 테스트용 IdGenerator 구현체
 */
class TestIdGenerator : IdGenerator {
    var nextIdToReturn: String = UUID.randomUUID().toString()
    var callCount: Int = 0

    override fun nextId(): String {
        callCount++
        return nextIdToReturn
    }
}
