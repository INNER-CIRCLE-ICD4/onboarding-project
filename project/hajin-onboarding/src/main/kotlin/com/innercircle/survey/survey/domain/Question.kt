package com.innercircle.survey.survey.domain

import com.innercircle.survey.common.domain.BaseEntity
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.OrderColumn
import jakarta.persistence.Table

@Entity
@Table(name = "QUESTIONS")
class Question private constructor(
    @Column(name = "title", nullable = false, length = 200)
    var title: String,
    @Column(name = "description", nullable = false, length = 500)
    var description: String,
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 20)
    var type: QuestionType,
    @Column(name = "required", nullable = false)
    var required: Boolean = false,
) : BaseEntity() {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "survey_id")
    var survey: Survey? = null
        internal set

    @OneToMany(
        mappedBy = "question",
        cascade = [CascadeType.ALL],
        orphanRemoval = true,
        fetch = FetchType.LAZY,
    )
    @OrderColumn(name = "choice_order")
    private val _choices: MutableList<Choice> = mutableListOf()

    val choices: List<Choice>
        get() = _choices.toList()

    fun addChoice(choice: Choice) {
        validateChoiceAddition()
        _choices.add(choice)
        choice.question = this
    }

    fun removeChoice(choice: Choice) {
        _choices.remove(choice)
        choice.question = null
    }

    fun clearChoices() {
        _choices.clear()
    }

    fun update(
        title: String,
        description: String,
        type: QuestionType,
        required: Boolean,
        newChoices: List<String> = emptyList(),
    ) {
        this.title = title
        this.description = description
        this.required = required

        if (this.type != type) {
            this.type = type
            if (type.isTextType()) {
                clearChoices()
            }
        }
        if (type.isChoiceType()) {
            updateChoices(newChoices)
        }
    }

    private fun updateChoices(newChoiceTexts: List<String>) {
        clearChoices()
        newChoiceTexts.forEach { text ->
            addChoice(Choice.create(text))
        }
    }

    private fun validateChoiceAddition() {
        require(type.isChoiceType()) {
            "${type.description} 타입은 선택지를 가질 수 없습니다."
        }
        require(_choices.size < MAX_CHOICES) {
            "선택지는 최대 ${MAX_CHOICES}개까지만 추가할 수 있습니다."
        }
    }

    companion object {
        const val MAX_CHOICES = 20

        fun create(
            title: String,
            description: String,
            type: QuestionType,
            required: Boolean = false,
            choices: List<String> = emptyList(),
        ): Question {
            require(title.isNotBlank()) { "항목 제목은 필수입니다." }
            require(description.isNotBlank()) { "항목 설명은 필수입니다." }

            val question = Question(title, description, type, required)

            if (type.isChoiceType()) {
                require(choices.isNotEmpty()) {
                    "${type.description} 타입은 최소 1개 이상의 선택지가 필요합니다."
                }
                require(choices.size <= MAX_CHOICES) {
                    "선택지는 최대 ${MAX_CHOICES}개까지만 추가할 수 있습니다."
                }
                choices.forEach { choiceText ->
                    question.addChoice(Choice.create(choiceText))
                }
            }

            return question
        }
    }
}
