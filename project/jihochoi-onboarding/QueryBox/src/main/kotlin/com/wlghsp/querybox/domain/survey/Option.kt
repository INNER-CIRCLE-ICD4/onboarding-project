package com.wlghsp.querybox.domain.survey

import jakarta.persistence.*

@Embeddable
class Option(
    val text: String,
)  {

    init {
        require(isNotBlank()) { "옵션 값은 비어 있을 수 없습니다." }
    }

    private fun isNotBlank(): Boolean = text.isNotBlank()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Option
        return text == other.text
    }

    override fun hashCode(): Int {
        return text.hashCode()
    }

}