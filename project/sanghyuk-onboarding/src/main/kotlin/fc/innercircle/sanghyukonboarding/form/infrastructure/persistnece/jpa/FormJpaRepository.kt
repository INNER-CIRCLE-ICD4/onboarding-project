package fc.innercircle.sanghyukonboarding.form.infrastructure.persistnece.jpa

import fc.innercircle.sanghyukonboarding.form.infrastructure.persistnece.jpa.entity.FormEntity
import org.springframework.data.jpa.repository.JpaRepository

interface FormJpaRepository : JpaRepository<FormEntity, String>
