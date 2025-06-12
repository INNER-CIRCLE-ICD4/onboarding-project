package fc.innercircle.jinhoonboarding.answer.domain

import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToMany

@Entity
class Response (
    val surveyId: Long,
    val questionId: Long,
    val question: String,

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "response_id")
    val answer: List<Answer>,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
}