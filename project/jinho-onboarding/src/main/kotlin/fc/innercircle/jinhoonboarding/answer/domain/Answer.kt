package fc.innercircle.jinhoonboarding.answer.domain

import jakarta.persistence.*
import java.util.UUID

@Entity
class Answer(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: UUID = UUID.randomUUID(),

    val answer: String,

    @Embedded
    val question: QuestionData,

    @ManyToOne()
    @JoinColumn(name = "set_id", nullable = false)
    var answerSet: AnswerSet
) {
    @Embeddable
    data class QuestionData(
        val questionID: UUID,
        val questionTitle: String,
        val questionDescription: String,
        val options: List<String>
    )
}