package com.wlghsp.querybox.api.survey.domain

import jakarta.persistence.Embeddable

@Embeddable
class Option(
    val value: String,
)  {

    init {
        require(value.isNotBlank()) { "옵션 값은 비어 있을 수 없습니다." }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Option

        return value == other.value
    }

    override fun hashCode(): Int {
        return value.hashCode()
    }

}