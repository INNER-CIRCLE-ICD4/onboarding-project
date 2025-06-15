package fc.innercircle.sanghyukonboarding.form.infrastructure.persistnece.jpa.entity

import fc.innercircle.sanghyukonboarding.common.domain.model.IdGenerator
import fc.innercircle.sanghyukonboarding.form.domain.model.Form
import fc.innercircle.sanghyukonboarding.form.domain.model.QuestionTemplate
import fc.innercircle.sanghyukonboarding.form.domain.validator.FormValidator
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.PrePersist
import jakarta.persistence.Table
import org.springframework.data.domain.Persistable

@Table(name = "form",
    indexes = [],
    uniqueConstraints = [])
@Entity(name = "form")
open class FormEntity(
    @Id
    @Column(
        name = "id",
        nullable = false,
        updatable = false,
        unique = true,
        columnDefinition = "char(26) not null comment 'ID'"
    )
    private var id: String? = null,
    @Column(nullable = false, length = 255, columnDefinition = "varchar(255) comment '설문 제목'")
    val title: String,
    @Column(nullable = false, length = 1000, columnDefinition = "varchar(1000) comment '설문 설명'")
    val description: String,
): Persistable<String> {

    /** 생성자에서 필수 입력값 검증 **/
    init {
        validateRequiredFields()
    }

    private fun validateRequiredFields() {
        FormValidator.validateTitle(title)
        FormValidator.validateDescription(description)
    }

    fun toDomain(questionTemplates: List<QuestionTemplate>): Form {
        return Form(
            id = id!!,
            title = title,
            description = description,
            questionTemplates = questionTemplates.filter { it.formId == id }
        )
    }

    override fun getId(): String {
        return id ?: throw IllegalStateException("cannot get ID before persist")
    }

    override fun isNew(): Boolean {
        return id == null
    }

    @PrePersist
    fun prePersist() {
        if (id == null) {
            id = IdGenerator.next()
        }
    }

    companion object {
        fun fromDomain(
            domain: Form,
        ): FormEntity {
            return FormEntity(
                title = domain.title,
                description = domain.description,
            )
        }
    }
}
