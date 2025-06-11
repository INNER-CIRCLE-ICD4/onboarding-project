package fc.innercircle.jinhoonboarding.survey.domain

import jakarta.persistence.*

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