package fc.innercircle.sanghyukonboarding.formreply.model

import fc.innercircle.sanghyukonboarding.common.domain.model.BaseEntity
import jakarta.persistence.ConstraintMode
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.ForeignKey
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne

@Entity
open class Answer(
    formReply: FormReply,
    val questionSnapshotId: String,
    // 주관식용 답변
    val answer: String = "",
    // 선택형 답변용 선택 옵션 ID
    val selectableOptionId: String = "",
    createdBy: String,
) : BaseEntity(createdBy) {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
        name = "form_reply_id",
        nullable = false,
        foreignKey = ForeignKey(ConstraintMode.NO_CONSTRAINT),
        columnDefinition = "char(26) not null comment '설문 응답 ID'"
    )
    var formReply: FormReply = formReply
        protected set
}
