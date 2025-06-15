package fc.innercircle.sanghyukonboarding.formreply.domain.model

import fc.innercircle.sanghyukonboarding.formreply.infrastructure.persistence.jpa.FormReplyEntity
import jakarta.persistence.ConstraintMode
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.ForeignKey
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne

open class Answer(
    formReplyEntity: FormReplyEntity,
    val questionSnapshotId: String,
    val answer: String = "",
    val selectableOptionId: String = "",
) {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
        name = "form_reply_id",
        nullable = false,
        foreignKey = ForeignKey(ConstraintMode.NO_CONSTRAINT),
        columnDefinition = "char(26) not null comment '설문 응답 ID'"
    )
    var formReplyEntity: FormReplyEntity = formReplyEntity
        protected set
}
