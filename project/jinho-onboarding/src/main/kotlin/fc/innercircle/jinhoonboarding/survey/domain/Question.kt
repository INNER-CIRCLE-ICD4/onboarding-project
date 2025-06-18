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
): BaseEntity()