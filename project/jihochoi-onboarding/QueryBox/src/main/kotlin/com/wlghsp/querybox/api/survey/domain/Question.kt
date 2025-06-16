package com.wlghsp.querybox.api.survey.domain

import jakarta.persistence.*
import support.domain.BaseRootEntity

@Entity
class Question(
    var name: String,
    var description: String,

    @Enumerated(EnumType.STRING)
    var type: QuestionType,

    var required: Boolean,

    @ElementCollection
    @CollectionTable(name = "question_option", joinColumns = [JoinColumn(name = "question_id")])
    val options: List<Option>? = null,

    id: Long = 0L,
) : BaseRootEntity<Question>(id) {

    init {
        require(name.isNotBlank()) { "문항 이름은 비어 있을 수 없습니다." }

        if (type == QuestionType.SINGLE_CHOICE || type == QuestionType.MULTIPLE_CHOICE) {
            require(!options.isNullOrEmpty()) { "선택형 문항에는 옵션이 반드시 있어야 합니다." }

            val distinctCheckTarget = options!!
            require(distinctCheckTarget.distinctBy { it.value }.size == distinctCheckTarget.size) {
                "옵션 값은 중복될 수 없습니다."
            }
        } else {
            require(options.isNullOrEmpty()) { "주관식 문항은 옵션을 가질 수 없습니다." }
        }
    }
}
