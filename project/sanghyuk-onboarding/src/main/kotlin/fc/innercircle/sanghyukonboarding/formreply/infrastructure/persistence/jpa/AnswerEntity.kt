package fc.innercircle.sanghyukonboarding.formreply.infrastructure.persistence.jpa

import fc.innercircle.sanghyukonboarding.formreply.infrastructure.persistence.jpa.FormReplyEntity
import jakarta.persistence.Column
import jakarta.persistence.ConstraintMode
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.ForeignKey
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@Table(name = "answer",
    indexes = [],
    uniqueConstraints = [])
@Entity(name = "answer")
open class AnswerEntity(
    @Id
    @Column(
        name = "id",
        nullable = false,
        updatable = false,
        unique = true,
        columnDefinition = "char(26) not null comment 'ID'"
    )
    val id: String,
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
