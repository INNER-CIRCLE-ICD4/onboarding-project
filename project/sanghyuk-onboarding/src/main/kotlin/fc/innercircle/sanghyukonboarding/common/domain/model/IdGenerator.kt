package fc.innercircle.sanghyukonboarding.common.domain.model

import fc.innercircle.sanghyukonboarding.common.infrastructure.numbering.UlidGenerator

object IdGenerator {
    fun next(): String {
        return UlidGenerator.next()
    }
}
