package fc.innercircle.jinhoonboarding.answer.domain

import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany

@Entity
class AnswerSet(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    val title: String,

    @OneToMany(mappedBy = "answerSet", cascade = [CascadeType.ALL])
    var answers: MutableList<Answer> = mutableListOf(),

) {

}