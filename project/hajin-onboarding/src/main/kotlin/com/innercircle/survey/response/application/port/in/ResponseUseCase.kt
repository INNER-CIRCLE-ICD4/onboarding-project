package com.innercircle.survey.response.application.port.`in`

import com.innercircle.survey.response.adapter.out.persistence.dto.ResponseSummaryProjection
import com.innercircle.survey.response.domain.Response
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.UUID

interface ResponseUseCase {
    fun submitResponse(command: SubmitResponseCommand): Response

    // 응답 단건 조회
    fun getResponseById(responseId: UUID): Response

    // 설문조사별 응답 목록 조회 (페이징)
    fun getResponsesBySurveyId(
        surveyId: UUID,
        pageable: Pageable,
    ): Page<Response>

    // 응답 요약 목록 조회 (최적화)
    fun getResponseSummariesBySurveyId(
        surveyId: UUID,
        pageable: Pageable,
    ): Page<ResponseSummaryProjection>

    // 응답 검색 (항목 이름과 응답 값 기반)
    fun searchResponses(
        criteria: ResponseSearchCriteria,
        pageable: Pageable,
    ): Page<Response>

    data class SubmitResponseCommand(
        val surveyId: UUID,
        val respondentId: String? = null,
        val answers: List<AnswerCommand>,
    ) {
        data class AnswerCommand(
            val questionId: UUID,
            val textValue: String? = null,
            val selectedChoiceIds: Set<UUID>? = null,
        )
    }

    data class ResponseSearchCriteria(
        val surveyId: UUID,
        val questionTitle: String? = null,
        val answerValue: String? = null,
    )
}
