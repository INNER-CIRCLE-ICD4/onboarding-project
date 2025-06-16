package com.wlghsp.querybox.domain.survey

import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToMany
import support.domain.BaseRootEntity

@Entity
class Survey(
    var title: String, // 설문 조사 이름
    var description: String, // 설문 조사 설명

    @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true)
    @JoinColumn(name = "survey_id", nullable = false)
    val questions: MutableList<Question> = mutableListOf(), // 설문 받을 항목
    id: Long = 0L,
) : BaseRootEntity<Survey>(id) {

    init {
        require(title.isNotBlank()) { "설문 제목은 비어 있을 수 없습니다."}
        require(questions.size in 1..10) { "질문 수는 1개 이상 10개 이하여야 합니다."}
        require(questions.distinctBy { it.name }.size == questions.size) {
            "질문 이름은 중복될 수 없습니다."
        }
    }

    fun addQuestion(question: Question) {
        check(questions.size < 10) { "질문은 최대 10개까지 가능합니다." }
        check(questions.none { it.name == question.name }) { "질문 이름은 중복될 수 없습니다."}
        questions.add(question)
    }
}