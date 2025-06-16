package fc.innercircle.jinhoonboarding.survey.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import fc.innercircle.jinhoonboarding.common.entity.BaseEntity
import jakarta.persistence.*
import java.util.UUID

// [설문 받을 항목]은 [항목 이름], [항목 설명], [항목 입력 형태], [항목 필수 여부]의 구성으로 이루어져있습니다.
// [항목 입력 형태]는 [단답형], [장문형], [단일 선택 리스트], [다중 선택 리스트]의 구성으로 이루어져있습니다.
// [단일 선택 리스트], [다중 선택 리스트]의 경우 선택 할 수 있는 후보를 요청 값에 포함하여야 합니다.
@Entity
class Question(
    @Id
    var id: UUID = UUID.randomUUID(),

    val title: String,
    val description: String,
    @Enumerated(EnumType.STRING)
    val questionType: QuestionType,
    val required: Boolean,
    val options: List<String> = mutableListOf(),
    var deprecated: Boolean = false,

    @ManyToOne()
    @JoinColumn(name = "survey_id", nullable = false)
    @JsonIgnore
    var survey: Survey
): BaseEntity() {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Question) return false

        if (required != other.required) return false
        if (title != other.title) return false
        if (description != other.description) return false
        if (questionType != other.questionType) return false
        if (options != other.options) return false

        return true
    }

    override fun hashCode(): Int {
        var result = required.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + description.hashCode()
        result = 31 * result + questionType.hashCode()
        result = 31 * result + options.hashCode()
        return result
    }

    override fun toString(): String {
        return "Question(id=$id, title='$title', description='$description', questionType=$questionType, required=$required, options=$options, deprecated=$deprecated, survey=$survey)"
    }


}