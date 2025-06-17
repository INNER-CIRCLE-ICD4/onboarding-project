package formService.adapter.port.outbound.persistence.jpa

import formService.domain.SurveyForm
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EntityListeners
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.PostLoad
import jakarta.persistence.PrePersist
import jakarta.persistence.Table
import jakarta.persistence.Transient
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import org.springframework.data.domain.Persistable
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

@Entity
@Table(name = "survey_form")
@EntityListeners(AuditingEntityListener::class)
class SurveyFormJpaEntity(
    id: String,
    surveyName: String,
    description: String,
) : Persistable<String> {
    @Id
    @Column(columnDefinition = "char(13)")
    private val id: String = id

    @Column
    var surveyName: String = surveyName
        protected set

    @Column
    var description: String = description
        protected set

    @OneToMany(mappedBy = "surveyForm", cascade = [CascadeType.PERSIST])
    protected val mutableQuestions: MutableList<QuestionJpaEntity> = mutableListOf()
    val questions: List<QuestionJpaEntity> get() = mutableQuestions.toList()

    @CreationTimestamp
    var createdAt: LocalDateTime? = null

    @UpdateTimestamp
    var updatedAt: LocalDateTime? = null

    @Transient
    private var _isNew = createdAt == null

    override fun getId(): String = id

    override fun isNew(): Boolean = _isNew

    @PostLoad
    @PrePersist
    fun markNotNow() {
        _isNew = false
    }

    fun addQuestions(question: List<QuestionJpaEntity>) {
        question.forEach {
            mutableQuestions.add(it)
            it.surveyForm = this
        }
    }

    override fun toString(): String =
        "SurveyFormJpaEntity(" +
            "surveyName='$surveyName', \n" +
            "description='$description', \n" +
            "id='$id', \n" +
            "questions=$questions, \n" +
            "createdAt=$createdAt, \n" +
            "updatedAt=$updatedAt, \n" +
            "isNew=$_isNew, \n" +
            ")"

    fun update(surveyForm: SurveyForm) {
        this.surveyName = surveyForm.surveyName
        this.description = surveyForm.description

        surveyForm.questions.forEach {
            val find = this.questions.find { qje -> it.id == qje.id }

            find?.name = it.name
            find?.description = it.description
            find?.required = it.required
            find?.inputType = it.inputType

            if (it.isRemoved) {
                find?.deletedAt = LocalDateTime.now()
            }

            it.options?.forEach { qo ->
                val findOption = find?.options?.find { qoje -> qo.id == qoje.id }

                findOption?.option = qo.value
            }
        }
    }
}
