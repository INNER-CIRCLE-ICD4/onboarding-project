package com.innercircle.survey.response.domain

import com.innercircle.survey.common.domain.BaseEntity
import com.innercircle.survey.survey.domain.QuestionType
import jakarta.persistence.CollectionTable
import jakarta.persistence.Column
import jakarta.persistence.ElementCollection
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.util.UUID

@Entity
@Table(name = "ANSWERS")
class Answer private constructor(
    @Column(name = "question_id", nullable = false)
    val questionId: UUID,
    @Column(name = "question_title", nullable = false)
    val questionTitle: String,
    @Enumerated(EnumType.STRING)
    @Column(name = "question_type", nullable = false)
    val questionType: QuestionType,
    @Column(name = "text_value", length = 5000)
    val textValue: String? = null,
    @ElementCollection
    @CollectionTable(
        name = "ANSWER_CHOICES",
        joinColumns = [JoinColumn(name = "answer_id")],
    )
    @Column(name = "choice_id")
    val selectedChoiceIds: MutableSet<UUID> = mutableSetOf(),
) : BaseEntity() {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "response_id")
    lateinit var response: Response
        internal set

    fun validateAnswer() {
        when (questionType) {
            QuestionType.SHORT_TEXT -> {
                val text = textValue
                requireNotNull(text) { "단답형 질문에는 텍스트 응답이 필요합니다." }
                require(text.length <= 500) { "단답형 응답은 500자를 초과할 수 없습니다." }
                require(selectedChoiceIds.isEmpty()) { "텍스트형 질문에는 선택지를 선택할 수 없습니다." }
            }
            QuestionType.LONG_TEXT -> {
                val text = textValue
                requireNotNull(text) { "장문형 질문에는 텍스트 응답이 필요합니다." }
                require(text.length <= 5000) { "장문형 응답은 5000자를 초과할 수 없습니다." }
                require(selectedChoiceIds.isEmpty()) { "텍스트형 질문에는 선택지를 선택할 수 없습니다." }
            }
            QuestionType.SINGLE_CHOICE -> {
                require(selectedChoiceIds.size == 1) { "단일 선택 질문에는 정확히 하나의 선택지를 선택해야 합니다." }
                require(textValue == null) { "선택형 질문에는 텍스트 응답을 입력할 수 없습니다." }
            }
            QuestionType.MULTIPLE_CHOICE -> {
                require(selectedChoiceIds.isNotEmpty()) { "다중 선택 질문에는 최소 하나 이상의 선택지를 선택해야 합니다." }
                require(textValue == null) { "선택형 질문에는 텍스트 응답을 입력할 수 없습니다." }
            }
        }
    }

    companion object {
        fun createTextAnswer(
            questionId: UUID,
            questionTitle: String,
            questionType: QuestionType,
            textValue: String,
        ): Answer {
            require(questionType.isTextType()) { "텍스트 응답은 텍스트형 질문에만 가능합니다." }

            val answer =
                Answer(
                    questionId = questionId,
                    questionTitle = questionTitle,
                    questionType = questionType,
                    textValue = textValue,
                )
            answer.validateAnswer()
            return answer
        }

        fun createChoiceAnswer(
            questionId: UUID,
            questionTitle: String,
            questionType: QuestionType,
            selectedChoiceIds: Set<UUID>,
        ): Answer {
            require(questionType.isChoiceType()) { "선택지 응답은 선택형 질문에만 가능합니다." }

            val answer =
                Answer(
                    questionId = questionId,
                    questionTitle = questionTitle,
                    questionType = questionType,
                )
            answer.selectedChoiceIds.addAll(selectedChoiceIds)
            answer.validateAnswer()
            return answer
        }
    }
}
