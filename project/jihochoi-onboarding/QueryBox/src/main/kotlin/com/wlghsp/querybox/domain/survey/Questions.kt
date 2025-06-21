package com.wlghsp.querybox.domain.survey

import com.wlghsp.querybox.ui.dto.QuestionUpdateRequest
import jakarta.persistence.CascadeType
import jakarta.persistence.Embeddable
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToMany

@Embeddable
class Questions(
    @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true)
    @JoinColumn(name = "survey_id")
    private val values: MutableList<Question> = mutableListOf()
) {

    fun validate() {
        require(isQuestionCountValid()) { "항목 수는 1~10개 사이여야 합니다." }
        require(hasNoDuplicateQuestionNames()) { "항목 이름은 중복될 수 없습니다." }
    }

    fun update(questions: List<QuestionUpdateRequest>) {
        val currentQuestions = this.groupById()
        val newQuestions = questions.map { request ->
            val existing = request.id?.let { currentQuestions[it] }

            if (existing != null) { // 존재하면 기존 데이터 수정
                existing.updateFrom(request)
                existing
            } else { // 존재하지 않으면 항목 신규 생성
                Question.from(request)
            }
        }

        val retainedIds = questions.mapNotNull { it.id }.toSet()
        this.removeIfExists(currentQuestions.keys - retainedIds)
        this.addAllIfNew(newQuestions)
    }

    fun groupById(): Map<Long, Question> =
        values.associateBy { it.id }

    fun removeIfExists(removedIds: Set<Long>) {
        values.removeIf { it.id in removedIds }
    }

    fun addAllIfNew(newQuestions: List<Question>) {
        values.addAll(newQuestions.filter { it.id == 0L })
    }

    private fun isQuestionCountValid(): Boolean = values.size in 1..10

    private fun hasNoDuplicateQuestionNames(): Boolean = values.distinctBy { it.name }.size == values.size

    fun add(question: Question) {
        this.values.add(question)
    }

    fun values(): List<Question> = values.toList() // 방어적 복사

    fun size(): Int = values.size

    companion object {
        fun of(values: List<Question>): Questions {
            val questions = Questions(values.toMutableList())
            questions.validate()
            return questions
        }
    }
}