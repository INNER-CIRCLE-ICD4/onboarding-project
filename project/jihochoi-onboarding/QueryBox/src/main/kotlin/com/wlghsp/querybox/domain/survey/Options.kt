package com.wlghsp.querybox.domain.survey

import jakarta.persistence.*


class Options(
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "question_option", joinColumns = [JoinColumn(name = "question_id")])
    @Column(name = "text")
    val values: List<Option> = listOf(),
) {
    @Suppress("unused") // Hibernate용 기본 생성자
    constructor() : this(emptyList()) {

    }

    fun validate() {
        require(values.isNotEmpty()) { "옵션은 비어 있을 수 없습니다."}
        require(values.distinctBy { it.text }.size == values.size) {
            "옵션 값은 중복될 수 없습니다."
        }
    }

    fun isEmpty(): Boolean = values.isEmpty()
    fun isNotEmpty(): Boolean = values.isNotEmpty()


    companion object {
        fun of(values: List<Option>): Options {
            val options = Options(values.toMutableList())
            options.validate()
            return options
        }
    }
}