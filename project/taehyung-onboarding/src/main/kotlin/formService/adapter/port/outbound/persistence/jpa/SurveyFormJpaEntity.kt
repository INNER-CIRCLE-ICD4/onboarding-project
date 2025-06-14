package formService.adapter.port.outbound.persistence.jpa

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.PostLoad
import jakarta.persistence.PostPersist
import jakarta.persistence.Table
import jakarta.persistence.Transient
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import org.springframework.data.domain.Persistable
import java.time.LocalDateTime

@Entity
@Table(name = "survey_form")
class SurveyFormJpaEntity(
    id: String,
    @Column val surveyName: String,
    @Column val description: String,
) : Persistable<String> {
    @Id
    @Column(columnDefinition = "char(13)")
    private val id: String = id

    @OneToMany(mappedBy = "surveyForm", cascade = [CascadeType.PERSIST])
    protected val mutableQuestions: MutableList<QuestionJpaEntity> = mutableListOf()
    val questions: List<QuestionJpaEntity> get() = mutableQuestions.toList()

    @CreationTimestamp
    var createdAt: LocalDateTime? = null

    @UpdateTimestamp
    var updatedAt: LocalDateTime? = null

    @Transient
    private var isNew = (createdAt == null)

    override fun getId(): String = id

    override fun isNew(): Boolean = isNew

    @PostLoad
    @PostPersist
    fun markNotNow() {
        isNew = false
    }

    fun addQuestions(question: List<QuestionJpaEntity>) {
        question.forEach {
            mutableQuestions.add(it)
            it.surveyForm = this
        }
    }

    override fun toString(): String =
        "SurveyFormJpaEntity(surveyName='$surveyName', description='$description', id='$id', mutableQuestions=$mutableQuestions, questions=$questions, createdAt=$createdAt, updatedAt=$updatedAt, isNew=$isNew)"
}
