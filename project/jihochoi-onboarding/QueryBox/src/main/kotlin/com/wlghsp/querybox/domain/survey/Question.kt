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
        validate()
    }

    fun validate() {
        require(name.isNotBlank()) { "항목 이름은 비어 있을 수 없습니다." }
        type.validateQuestion(options)
    }

    fun updateFrom(request: QuestionUpdateRequest) {
        validate()

        this.name = request.name
        this.description = request.description
        this.type = request.type
        this.required = request.required
    }

    companion object {
        fun from(request: QuestionUpdateRequest): Question {
            return Question(
                name = request.name,
                description = request.description,
                type = request.type,
                required = request.required,
                options = request.options?.let { Options(it.map(::Option)) }
            )
        }
    }
}
