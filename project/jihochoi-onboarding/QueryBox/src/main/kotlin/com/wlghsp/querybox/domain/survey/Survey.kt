package com.wlghsp.querybox.domain.survey

import com.wlghsp.querybox.ui.dto.SurveyUpdateRequest
import jakarta.persistence.Entity
import support.domain.BaseRootEntity

@Entity
class Survey (
    var title: String, // 설문 조사 이름
    var description: String, // 설문 조사 설명

    val questions: Questions = Questions.of(emptyList()), // 설문 받을 항목
    id: Long = 0L,
) : BaseRootEntity<Survey>(id) {

    fun update(request: SurveyUpdateRequest) {
        this.title = request.title
        this.description = request.description
        this.questions.update(request.questions)
    }

    fun getQuestions(): List<Question> {
        return questions.values()
    }

    companion object {
        fun of(title: String, description: String, questions: Questions): Survey {
            require(title.isNotBlank()) { "설문 제목은 비어 있을 수 없습니다."}

            return Survey(
                title = title,
                description = description,
                questions = questions
            )
        }
    }
}