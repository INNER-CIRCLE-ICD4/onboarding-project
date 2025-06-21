package formService.adapter.port.outbound.persistence.jpa

import formService.domain.InputType
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

@Entity
class QuestionAnswerJpaEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    @Enumerated(EnumType.STRING)
    var answerType: InputType,
    @Column
    var answerValue: String,
) {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "answer_id")
    var answer: AnswerJpaEntity? = null
}
