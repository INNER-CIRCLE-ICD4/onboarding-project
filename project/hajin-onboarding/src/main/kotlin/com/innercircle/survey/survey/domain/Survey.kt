package com.innercircle.survey.survey.domain

import com.innercircle.survey.common.domain.BaseEntity
import com.innercircle.survey.survey.domain.exception.SurveyItemLimitExceededException
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
    @Column(name = "version", nullable = false)
    var version: Int = 1,
    @Column(name = "is_active", nullable = false)
    var isActive: Boolean = true,
) : BaseEntity() {
    @OneToMany(
        mappedBy = "survey",
        cascade = [CascadeType.ALL],
        orphanRemoval = false,
        fetch = FetchType.LAZY,
    )
    @OrderColumn(name = "question_order")
    private val _questions: MutableList<Question> = mutableListOf()

    val questions: List<Question>
        get() = _questions.filter { it.isActive }.toList()

    val allQuestions: List<Question> // 테스트용
        get() = _questions.toList()

    fun addQuestion(question: Question) {
        if (!canAddMoreQuestions()) {
            throw SurveyItemLimitExceededException(questions.size + 1, MAX_QUESTIONS)
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
        this.version++
    }

    fun updateQuestions(newQuestions: List<Question>) {
        if (newQuestions.size > MAX_QUESTIONS) {
            throw SurveyItemLimitExceededException(newQuestions.size, MAX_QUESTIONS)
        }

        // 기존 질문들을 비활성화 처리 (응답 보존을 위해)
        _questions.forEach { it.isActive = false }

        // 새로운 질문들 추가 (clear 하지 않고 추가만)
        newQuestions.forEach { addQuestion(it) }

        // 버전 증가
        this.version++
    }

    fun canAddMoreQuestions(): Boolean = questions.size < MAX_QUESTIONS

    companion object {
        const val MAX_QUESTIONS = 10

        fun create(
            title: String,
            description: String,
            questions: List<Question> = emptyList(),
        ): Survey {
            require(title.isNotBlank()) {
                "설문조사 제목은 필수입니다."
            }
            require(description.isNotBlank()) {
                "설문조사 설명은 필수입니다."
            }

            if (questions.size > MAX_QUESTIONS) {
                throw SurveyItemLimitExceededException(questions.size, MAX_QUESTIONS)
            }

            val survey = Survey(title, description)
            questions.forEach { survey.addQuestion(it) }
            return survey
        }
    }
}
