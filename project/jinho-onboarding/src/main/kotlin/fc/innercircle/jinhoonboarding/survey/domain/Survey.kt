package fc.innercircle.jinhoonboarding.survey.domain

import fc.innercircle.jinhoonboarding.common.entity.BaseEntity
import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany

// 설문조사 양식은 [설문조사 이름], [설문조사 설명], [설문 받을 항목]의 구성으로 이루어져있습니다.

@Entity
class Survey(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    var title: String,
    var description: String,

    @OneToMany(mappedBy = "survey", cascade = [CascadeType.ALL])
    val questions: MutableSet<Question> = linkedSetOf(),
): BaseEntity() {


    fun updateTitle(newTitle: String) {
        this.title = newTitle
    }

    fun updateDescription(newDescription: String) {
        this.description = newDescription
    }

    fun updateQuestions(newQuestions: MutableSet<Question>) {
        //삭제된
        val remove = questions subtract newQuestions
        // 추가된
        val add = newQuestions subtract questions

        questions.removeAll(remove)
        add.forEach { it.survey = this }


    }



}
