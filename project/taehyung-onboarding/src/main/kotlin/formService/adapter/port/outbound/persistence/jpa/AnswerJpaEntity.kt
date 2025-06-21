package formService.adapter.port.outbound.persistence.jpa

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.OneToMany
import java.time.LocalDateTime

@Entity
class AnswerJpaEntity(
    id: String,
    @Column
    val userId: String,
    @Column
    val submittedAt: LocalDateTime,
) : AbstractEntity(id) {
    @OneToMany(mappedBy = "answer", fetch = FetchType.LAZY, cascade = [CascadeType.PERSIST])
    protected val mutableQuestionAnswers: MutableList<QuestionAnswerJpaEntity> = mutableListOf()
    val questionAnswers: List<QuestionAnswerJpaEntity> get() = mutableQuestionAnswers.toList()

    fun addQuestionAnswers(questionAnswers: List<QuestionAnswerJpaEntity>) {
        questionAnswers.forEach {
            this.mutableQuestionAnswers.add(it)
            it.answer = this
        }
    }
}
