package com.innercircle.survey.survey.domain

import com.innercircle.survey.common.domain.BaseEntity
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.OneToMany
import jakarta.persistence.OrderColumn
import jakarta.persistence.Table

@Entity
@Table(name = "SURVEYS")
class Survey private constructor(
    @Column(name = "title", nullable = false, length = 200)
    var title: String,
    @Column(name = "description", nullable = false, length = 1000)
    var description: String,
) : BaseEntity() {
    @OneToMany(
        mappedBy = "survey",
        cascade = [CascadeType.ALL],
        orphanRemoval = true,
        fetch = FetchType.LAZY,
    )
    @OrderColumn(name = "question_order")
    private val _questions: MutableList<Question> = mutableListOf()

    val questions: List<Question>
        get() = _questions.toList()

    fun addQuestion(question: Question) {
        require(_questions.size < MAX_QUESTIONS) {
            "설문조사는 최대 ${MAX_QUESTIONS}개의 항목만 가질 수 있습니다."
        }
        _questions.add(question)
        question.survey = this
    }

    fun removeQuestion(question: Question) {
        _questions.remove(question)
        question.survey = null
    }

    fun update(
        title: String,
        description: String,
    ) {
        this.title = title
        this.description = description
    }

    companion object {
        const val MAX_QUESTIONS = 10

        fun create(
            title: String,
            description: String,
            questions: List<Question> = emptyList(),
        ): Survey {
            require(title.isNotBlank()) { "설문조사 제목은 필수입니다." }
            require(description.isNotBlank()) { "설문조사 설명은 필수입니다." }
            require(questions.size <= MAX_QUESTIONS) {
                "설문조사는 최대 ${MAX_QUESTIONS}개의 항목만 가질 수 있습니다."
            }

            val survey = Survey(title, description)
            questions.forEach { survey.addQuestion(it) }
            return survey
        }
    }
}
