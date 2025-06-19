package fc.innercircle.sanghyukonboarding.formreply.infrastructure.persistence.jpa.entity

import fc.innercircle.sanghyukonboarding.common.domain.model.IdGenerator
import fc.innercircle.sanghyukonboarding.formreply.domain.model.Answer
import fc.innercircle.sanghyukonboarding.formreply.domain.model.FormReply
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.PrePersist
import jakarta.persistence.Table
import org.springframework.data.domain.Persistable
import java.time.LocalDateTime

@Table(name = "form_reply",
    indexes = [],
    uniqueConstraints = [])
@Entity(name = "form_reply")
open class FormReplyEntity(
    @Id
    @Column(
        name = "id",
        nullable = false,
        updatable = false,
        unique = true,
        columnDefinition = "char(26) not null comment 'ID'"
    )
    private var id: String,
    @Column(nullable = false, columnDefinition = "char(26) not null comment '설문 ID'")
    val formId: String = "",
    @Column(nullable = false, columnDefinition = "datetime not null comment '설문 응답일자'")
    val submittedAt: LocalDateTime
): Persistable<String> {
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

    fun toDomain(answers: List<Answer>): FormReply {
        return FormReply(
            id = this.id,
            formId = this.formId,
            submittedAt = this.submittedAt,
            answers = answers
        )
    }

    companion object {
        fun fromDomain(formReply: FormReply): FormReplyEntity {
            return FormReplyEntity(
                id = formReply.id,
                formId = formReply.formId,
                submittedAt = formReply.submittedAt,
            )
        }
    }
}
