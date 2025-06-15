package fc.innercircle.sanghyukonboarding.formreply.domain.model

import jakarta.persistence.Column
import jakarta.persistence.Id
import java.time.LocalDateTime

open class FormReply(
    @Id
    @Column(
        name = "id",
        nullable = false,
        updatable = false,
        unique = true,
        columnDefinition = "char(26) not null comment 'ID'"
    )
    val id: String,
    formId: String,
    submittedAt: LocalDateTime? = null,
    createdBy: String,
) {

    @Column(nullable = false, columnDefinition = "bigint not null comment '설문 ID'")
    var formId: String = formId
        protected set

    @Column(nullable = false, columnDefinition = "datetime not null comment '설문 응답일자'")
    var submittedAt: LocalDateTime? = submittedAt
        protected set
}
