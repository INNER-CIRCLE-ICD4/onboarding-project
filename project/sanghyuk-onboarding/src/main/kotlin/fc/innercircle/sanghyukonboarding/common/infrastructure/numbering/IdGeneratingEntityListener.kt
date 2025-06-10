package fc.innercircle.sanghyukonboarding.common.infrastructure.numbering

import fc.innercircle.sanghyukonboarding.common.domain.model.Identifiable
import fc.innercircle.sanghyukonboarding.common.domain.service.IdGenerator
import jakarta.persistence.PrePersist
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * @PrePersist 시점에 BaseEntity.id를 채워주는 JPA EntityListener
 */
@Component
class IdGeneratingEntityListener {

    @Autowired
    private lateinit var idGenerator: IdGenerator

    @PrePersist
    fun onPrePersist(target: Any) {
        if (!(target !is Identifiable || target.id != 0L)) {
            target.id = idGenerator.nextId()
        }
    }
}
