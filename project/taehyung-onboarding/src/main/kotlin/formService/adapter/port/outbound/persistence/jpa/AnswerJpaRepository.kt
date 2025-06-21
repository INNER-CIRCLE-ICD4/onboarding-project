package formService.adapter.port.outbound.persistence.jpa

import org.springframework.data.jpa.repository.JpaRepository

interface AnswerJpaRepository : JpaRepository<AnswerJpaEntity, String>
