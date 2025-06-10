package fc.innercircle.sanghyukonboarding.mock

import fc.innercircle.sanghyukonboarding.common.domain.service.IdGenerator

/**
 * 테스트용 IdGenerator 구현체
 */
class TestIdGenerator : IdGenerator {
    var nextIdToReturn: Long = 0L
    var callCount: Int = 0

    override fun nextId(): Long {
        callCount++
        return nextIdToReturn
    }
}
