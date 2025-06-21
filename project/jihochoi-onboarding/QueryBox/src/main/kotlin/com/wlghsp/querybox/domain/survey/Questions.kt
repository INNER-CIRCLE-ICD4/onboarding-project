package com.wlghsp.querybox.domain.survey

import com.wlghsp.querybox.ui.dto.QuestionUpdateRequest
import jakarta.persistence.CascadeType
import jakarta.persistence.Embeddable
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToMany

@Embeddable
class Questions (
    @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true)
    @JoinColumn(name = "survey_id")
    private val values: MutableList<Question> = mutableListOf()
) {

    fun validate() {
        require(values.size in 1..10) { "항목 수는 1~10개 사이여야 합니다." }
        require(values.distinctBy { it.name }.size == values.size) {
            "항목 이름은 중복될 수 없습니다."
        }
    }

    fun values(): List<Question> = values.toList() // 방어적 복사

    fun groupById(): Map<Long, Question> =
        values.associateBy { it.id }

    fun removeIfExists(removedIds: Set<Long>) {
        values.removeIf { it.id in removedIds }
    }

    fun addAllIfNew(newQuestions: MutableList<Question>) {
        values.addAll(newQuestions.filter { it.id == 0L })
    }

    fun update(questions: List<QuestionUpdateRequest>) {
        val currentQuestions = this.groupById()
        val newQuestions = mutableListOf<Question>()

        for (question in questions) {
            val existingQuestion = question.id?.let { currentQuestions[it] }

            if (existingQuestion != null) {
                existingQuestion.updateFrom(question)
                newQuestions.add(existingQuestion)
            } else {
                // id가 없으면 새로 생성
                val created = Question(
                    name = question.name,
                    description = question.description,
                    type = question.type,
                    required = question.required,
                    options = question.options?.let { Options(it.map(::Option)) }
                )
                newQuestions.add(created)
            }
        }

        val removed = currentQuestions.keys - questions.mapNotNull { it.id }.toSet()
        this.removeIfExists(removed)
        this.addAllIfNew(newQuestions)
    }

    companion object {
        fun of(values: List<Question>): Questions {
            val q = Questions(values.toMutableList())
            q.validate()
            return q
        }
    }
}