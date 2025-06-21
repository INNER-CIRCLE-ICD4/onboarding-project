package com.wlghsp.querybox.domain.survey

import com.wlghsp.querybox.ui.dto.QuestionUpdateRequest
import jakarta.persistence.*
import support.domain.BaseEntity

@Entity
class Question(
    @Column(nullable = false)
    var name: String, // 항목 이름
    var description: String, // 항목 설명

    @Enumerated(EnumType.STRING)
    var type: QuestionType, // 항목 입력 형태

    required: Boolean = false, // 항목 필수 여부

    @Embedded
    var options: Options? = Options(),

) : BaseEntity() {

    var required: Boolean = required
        protected set

    init {
        validateInvariants()
    }

    fun updateFrom(request: QuestionUpdateRequest) {
        validateInvariants()

        this.name = request.name
        this.description = request.description
        this.type = request.type
        this.required = request.required
    }

    private fun validateInvariants() {
        require(name.isNotBlank()) { "항목 이름은 비어 있을 수 없습니다." }

        if (type == QuestionType.SINGLE_CHOICE || type == QuestionType.MULTIPLE_CHOICE) {
            require(options != null && options!!.isNotEmpty()) {
                "선택형 항목에는 옵션이 반드시 있어야 합니다."
            }
        } else {
            require(options == null || options!!.isEmpty()) { "주관식 항목은 옵션을 가질 수 없습니다." }
        }
    }
}
