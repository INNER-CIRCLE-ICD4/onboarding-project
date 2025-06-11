package com.innercircle.survey.survey.domain

import com.innercircle.survey.common.domain.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@Entity
@Table(name = "CHOICES")
class Choice private constructor(
    @Column(name = "text", nullable = false, length = 200)
    var text: String,
) : BaseEntity() {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    var question: Question? = null
        internal set

    fun updateText(text: String) {
        require(text.isNotBlank()) { "선택지 텍스트는 필수입니다." }
        this.text = text
    }

    companion object {
        fun create(text: String): Choice {
            require(text.isNotBlank()) { "선택지 텍스트는 필수입니다." }
            return Choice(text)
        }
    }
}
