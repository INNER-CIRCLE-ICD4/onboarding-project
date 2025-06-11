package fc.innercircle.jinhoonboarding.survey.domain

import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany

// 설문조사 양식은 [설문조사 이름], [설문조사 설명], [설문 받을 항목]의 구성으로 이루어져있습니다.

@Entity
class Survey(
    val title: String,
    val description: String,
    @OneToMany(mappedBy = "survey", cascade = [CascadeType.ALL])
    val questions: MutableList<Question>
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
}
