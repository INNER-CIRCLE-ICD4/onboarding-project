package fc.innercircle.jinhoonboarding.answer.domain

import jakarta.persistence.*

@Entity
class Answer(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    val answer: String,



    @Embedded
    val question: Question,


    @ManyToOne()
    @JoinColumn(name = "set_id", nullable = false)
    var answerSet: AnswerSet
) {

    @Embeddable
    data class Question(
        val question: String,
    )
}