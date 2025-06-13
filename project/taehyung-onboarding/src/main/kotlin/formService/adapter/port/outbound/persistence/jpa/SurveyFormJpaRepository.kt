package formService.adapter.port.outbound.persistence.jpa

import org.springframework.data.jpa.repository.JpaRepository

interface SurveyFormJpaRepository : JpaRepository<SurveyFormJpaEntity, String>
