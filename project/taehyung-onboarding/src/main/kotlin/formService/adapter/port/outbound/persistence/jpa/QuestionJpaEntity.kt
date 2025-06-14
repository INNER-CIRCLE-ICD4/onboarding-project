package formService.adapter.port.outbound.persistence.jpa

import formService.domain.Question
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.Table

@Entity
@Table(name = "question")
class QuestionJpaEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    @Column
    var name: String,
    @Column
    var description: String,
    @Enumerated(value = EnumType.STRING)
    var inputType: Question.QuestionInputType,
    @Column
    var required: Boolean,
) {
    @OneToMany(mappedBy = "question", cascade = [CascadeType.PERSIST])
    protected val mutableOptions: MutableList<QuestionOptionJpaEntity> = mutableListOf()
    val options: List<QuestionOptionJpaEntity> get() = mutableOptions.toList()

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "survey_form_id")
    var surveyForm: SurveyFormJpaEntity? = null

    fun addOptions(options: List<QuestionOptionJpaEntity>?) {
        options?.forEach {
            mutableOptions.add(it)
            it.question = this
        }
    }

    override fun toString(): String =
        "QuestionJpaEntity(id=$id, name='$name', description='$description', inputType=$inputType, required=$required, mutableOptions=$mutableOptions, options=$options)"
}
