package fc.innercircle.sanghyukonboarding.formreply.model

import fc.innercircle.sanghyukonboarding.common.domain.model.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import java.time.LocalDateTime

@Entity
open class FormReply(
    formId: String,
    submittedAt: LocalDateTime? = null,
    createdBy: String,
) : BaseEntity(createdBy) {

    @Column(nullable = false, columnDefinition = "bigint not null comment '설문 ID'")
    var formId: String = formId
        protected set

    @Column(nullable = false, columnDefinition = "datetime not null comment '설문 응답일자'")
    var submittedAt: LocalDateTime? = submittedAt
        protected set
}
