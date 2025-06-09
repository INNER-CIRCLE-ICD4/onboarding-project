package fc.innercircle.jinhoonboarding.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

// [설문 받을 항목]은 [항목 이름], [항목 설명], [항목 입력 형태], [항목 필수 여부]의 구성으로 이루어져있습니다.
// [항목 입력 형태]는 [단답형], [장문형], [단일 선택 리스트], [다중 선택 리스트]의 구성으로 이루어져있습니다.
// [단일 선택 리스트], [다중 선택 리스트]의 경우 선택 할 수 있는 후보를 요청 값에 포함하여야 합니다.
@Entity
class Question(
    val title: String,
    val description: String,
    val questionType: QuestionType,
    val required: Boolean,
    val options: List<String>? = null,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
}

enum class QuestionType {
    SHORT_TEXT,
    LONG_TEXT,
    SINGLE_SELECT,
    MULTI_SELECT;
}

