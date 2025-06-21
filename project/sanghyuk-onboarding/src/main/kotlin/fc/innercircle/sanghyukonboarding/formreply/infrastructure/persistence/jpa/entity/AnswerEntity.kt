package fc.innercircle.sanghyukonboarding.formreply.infrastructure.persistence.jpa.entity

import fc.innercircle.sanghyukonboarding.common.domain.model.IdGenerator
import fc.innercircle.sanghyukonboarding.formreply.domain.model.Answer
import fc.innercircle.sanghyukonboarding.formreply.infrastructure.persistence.jpa.entity.converter.ListToJsonConverter
import jakarta.persistence.Column
import jakarta.persistence.ConstraintMode
import jakarta.persistence.Convert
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.ForeignKey
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.PrePersist
import jakarta.persistence.Table
import org.springframework.data.domain.Persistable

@Table(
    name = "answer",
    indexes = [],
    uniqueConstraints = []
)
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
    private var id: String,
    @Column(
        name = "question_snapshot_id",
        nullable = false,
        columnDefinition = "char(26) not null comment '질문 스냅샷 ID'"
    )
    val questionSnapshotId: String,
    @Column(
        name = "`values`",
        nullable = false,
        columnDefinition = "JSON not null comment '답변 내용'"
    )
    @Convert(converter = ListToJsonConverter::class)
    val values: List<String>,
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
        name = "form_reply_id",
        nullable = false,
        foreignKey = ForeignKey(ConstraintMode.NO_CONSTRAINT),
        columnDefinition = "char(26) not null comment '설문 응답 ID'"
    )
    val formReplyEntity: FormReplyEntity,
) : Persistable<String> {
    override fun getId(): String {
        return id
    }

    override fun isNew(): Boolean {
        return id.isBlank()
    }

    @PrePersist
    fun prePersist() {
        if (id.isBlank()) {
            id = IdGenerator.next()
        }
    }

    fun toDomain(): Answer {
        return Answer(
            id = id,
            questionId = questionSnapshotId,
            values = values.toList(),
            formReplyId = formReplyEntity.id
        )
    }

    companion object {
        fun fromDomain(answer: Answer, formReplyEntity: FormReplyEntity): AnswerEntity {
            return AnswerEntity(
                id = answer.id,
                questionSnapshotId = answer.questionId,
                values = answer.values.toList(),
                formReplyEntity = formReplyEntity
            )
        }
    }
}
