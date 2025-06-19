package com.wlghsp.querybox.application

import com.wlghsp.querybox.domain.survey.Questions
import com.wlghsp.querybox.ui.dto.AnswerRequest
import org.springframework.stereotype.Service
import kotlin.collections.forEach

@Service
class ResponseValidatorService {

    fun validateAnswers(questions: Questions, answerRequests: List<AnswerRequest>) {
        val questionsById = questions.groupById()

        answerRequests.forEach { answer ->
            val question = questionsById[answer.questionId]
                ?: throw IllegalArgumentException("응답 대상 문항이 설문에 존재하지 않습니다: ${answer.questionId}")

            if (question.required) {
                val isEmptyText = answer.answerValue.isBlank()
                val isEmptyChoices = answer.selectedIds.isEmpty()
                require(!(isEmptyText && isEmptyChoices)) { "필수 문항에는 반드시 응답해야 합니다: ${answer.questionName}" }
            }
        }

        require(answerRequests.size == questions.size()) { "질문 수와 응답 수가 일치하지 않습니다." }
    }
}