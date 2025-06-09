package com.innercircle.survey.response.domain

import com.innercircle.survey.common.domain.BaseEntity
import com.innercircle.survey.survey.domain.Choice
import com.innercircle.survey.survey.domain.Question
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.JoinTable
import jakarta.persistence.ManyToMany
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@Entity
@Table(name = "ANSWERS")
class Answer private constructor(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    val question: Question,
    @Column(name = "text_value", length = 1000)
    var textValue: String? = null,
) : BaseEntity() {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "response_id")
    var response: Response? = null
        internal set

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "ANSWER_CHOICES",
        joinColumns = [JoinColumn(name = "answer_id")],
        inverseJoinColumns = [JoinColumn(name = "choice_id")],
    )
    private val _selectedChoices: MutableSet<Choice> = mutableSetOf()

    val selectedChoices: Set<Choice>
        get() = _selectedChoices.toSet()

    fun addSelectedChoice(choice: Choice) {
        validateChoice(choice)
        _selectedChoices.add(choice)
    }

    fun removeSelectedChoice(choice: Choice) {
        _selectedChoices.remove(choice)
    }

    fun updateSelectedChoices(choices: Set<Choice>) {
        _selectedChoices.clear()
        choices.forEach { addSelectedChoice(it) }
    }

    fun isValid(): Boolean {
        return when {
            question.type.isTextType() -> !textValue.isNullOrBlank()
            question.type.isChoiceType() -> _selectedChoices.isNotEmpty()
            else -> false
        }
    }

    private fun validateChoice(choice: Choice) {
        require(question.type.isChoiceType()) {
            "텍스트 타입 질문에는 선택지를 선택할 수 없습니다."
        }

        require(question.choices.any { it.id == choice.id }) {
            "해당 선택지는 이 질문의 선택지가 아닙니다."
        }

        if (question.type == com.innercircle.survey.survey.domain.QuestionType.SINGLE_CHOICE) {
            require(_selectedChoices.isEmpty()) {
                "단일 선택 질문에는 하나의 선택지만 선택할 수 있습니다."
            }
        }
    }

    companion object {
        fun createTextAnswer(
            question: Question,
            textValue: String,
        ): Answer {
            require(question.type.isTextType()) {
                "텍스트 답변은 텍스트 타입 질문에만 가능합니다."
            }
            require(textValue.isNotBlank()) {
                "텍스트 답변은 비어있을 수 없습니다."
            }

            return Answer(question, textValue = textValue)
        }

        fun createChoiceAnswer(
            question: Question,
            selectedChoices: Set<Choice>,
        ): Answer {
            require(question.type.isChoiceType()) {
                "선택지 답변은 선택형 질문에만 가능합니다."
            }
            require(selectedChoices.isNotEmpty()) {
                "최소 하나 이상의 선택지를 선택해야 합니다."
            }

            val answer = Answer(question)
            selectedChoices.forEach { answer.addSelectedChoice(it) }
            return answer
        }
    }
}
