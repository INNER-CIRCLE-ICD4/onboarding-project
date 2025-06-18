package fc.innercircle.jinhoonboarding.answer.domain

import fc.innercircle.jinhoonboarding.common.entity.BaseEntity
import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import java.util.UUID

@Entity
class AnswerSet(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: UUID = UUID.randomUUID(),

    val userId: String,
    val surveyId: Long,

    @OneToMany(mappedBy = "answerSet", cascade = [CascadeType.ALL])
    var answers: MutableList<Answer> = mutableListOf(),

): BaseEntity() {

}