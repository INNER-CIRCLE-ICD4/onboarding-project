package com.innercircle.survey.response.adapter.`in`.web

import com.innercircle.survey.common.dto.ApiResponse
import com.innercircle.survey.common.dto.PageRequest
import com.innercircle.survey.common.dto.PageResponse
import com.innercircle.survey.response.adapter.`in`.web.dto.ResponseDto
import com.innercircle.survey.response.adapter.`in`.web.dto.ResponseSummaryDto
import com.innercircle.survey.response.adapter.`in`.web.dto.SubmitResponseRequest
import com.innercircle.survey.response.adapter.`in`.web.dto.toCommand
import com.innercircle.survey.response.adapter.`in`.web.dto.toDto
import com.innercircle.survey.response.adapter.out.persistence.dto.ResponseSummaryProjection
import com.innercircle.survey.response.application.port.`in`.ResponseUseCase
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

private val logger = KotlinLogging.logger {}

@Tag(name = "Response", description = "설문조사 응답 API")
@RestController
@RequestMapping("/api/v1")
class ResponseController(
    private val responseUseCase: ResponseUseCase,
) {
    @Operation(
        summary = "설문조사 응답 제출",
        description = "설문조사에 대한 응답을 제출합니다. 응답은 설문조사의 항목과 일치해야 합니다.",
    )
    @ApiResponses(
        value = [
            io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "201",
                description = "응답 제출 성공",
            ),
            io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "400",
                description = "잘못된 요청 (유효성 검증 실패, 필수 항목 누락 등)",
            ),
            io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "404",
                description = "설문조사를 찾을 수 없음",
            ),
        ],
    )
    @PostMapping("/surveys/{surveyId}/responses")
    fun submitResponse(
        @Parameter(description = "설문조사 ID", required = true)
        @PathVariable surveyId: UUID,
        @Valid @RequestBody request: SubmitResponseRequest,
    ): ResponseEntity<ApiResponse<ResponseDto>> {
        logger.info { "Submit response request received for survey: $surveyId" }

        val command = request.toCommand(surveyId)
        val response = responseUseCase.submitResponse(command)
        val responseDto = response.toDto()

        logger.info { "Response submitted successfully: ${response.id}" }

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(ApiResponse.success(responseDto, "응답이 성공적으로 제출되었습니다."))
    }

    @Operation(
        summary = "응답 단건 조회",
        description = "ID로 특정 응답을 조회합니다.",
    )
    @ApiResponses(
        value = [
            io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "조회 성공",
            ),
            io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "404",
                description = "응답을 찾을 수 없음",
            ),
        ],
    )
    @GetMapping("/responses/{responseId}")
    fun getResponse(
        @Parameter(description = "응답 ID", required = true)
        @PathVariable responseId: UUID,
    ): ResponseEntity<ApiResponse<ResponseDto>> {
        logger.info { "Get response request received: $responseId" }

        val response = responseUseCase.getResponseById(responseId)
        val responseDto = response.toDto()

        return ResponseEntity.ok(
            ApiResponse.success(responseDto),
        )
    }

    @Operation(
        summary = "설문조사별 응답 목록 조회",
        description = "특정 설문조사에 대한 응답 목록을 페이징하여 조회합니다. " +
                "summary=true 파라미터로 요약 정보만 조회할 수 있습니다. " +
                "questionTitle과 answerValue 파라미터로 특정 항목의 응답을 검색할 수 있습니다.",
    )
    @ApiResponses(
        value = [
            io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "조회 성공",
            ),
            io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "404",
                description = "설문조사를 찾을 수 없음",
            ),
        ],
    )
    @GetMapping("/surveys/{surveyId}/responses")
    fun getResponsesBySurvey(
        @Parameter(description = "설문조사 ID", required = true)
        @PathVariable surveyId: UUID,
        @Parameter(description = "페이지 정보")
        @Valid pageRequest: PageRequest,
        @Parameter(description = "요약 정보만 조회할지 여부 (기본값: false)")
        @RequestParam(required = false, defaultValue = "false") summary: Boolean,
        @Parameter(description = "검색할 질문 제목 (부분 일치)")
        @RequestParam(required = false) questionTitle: String?,
        @Parameter(description = "검색할 응답 값 (부분 일치)")
        @RequestParam(required = false) answerValue: String?,
    ): ResponseEntity<*> {
        logger.info { 
            "Get responses request received for survey: $surveyId, " +
            "page=${pageRequest.page}, size=${pageRequest.size}, summary=$summary, " +
            "questionTitle=$questionTitle, answerValue=$answerValue" 
        }

        val pageable = pageRequest.toPageable()

        return if (summary) {
            // 프로젝션을 사용한 요약 조회
            val responseSummaries = responseUseCase.getResponseSummariesBySurveyId(surveyId, pageable)
            val response = PageResponse.of(responseSummaries) { it.toSummaryDto() }
            ResponseEntity.ok(response)
        } else {
            // 전체 정보 조회 (검색 조건 포함)
            val responsesPage = if (questionTitle != null || answerValue != null) {
                val searchCriteria = ResponseUseCase.ResponseSearchCriteria(
                    surveyId = surveyId,
                    questionTitle = questionTitle,
                    answerValue = answerValue,
                )
                responseUseCase.searchResponses(searchCriteria, pageable)
            } else {
                responseUseCase.getResponsesBySurveyId(surveyId, pageable)
            }
            val response = PageResponse.of(responsesPage) { it.toDto() }
            ResponseEntity.ok(response)
        }
    }
}

// 확장 함수로 ResponseSummaryProjection을 응답 DTO로 변환
fun ResponseSummaryProjection.toSummaryDto(): ResponseSummaryDto =
    ResponseSummaryDto(
        id = id.toString(),
        surveyId = surveyId.toString(),
        surveyVersion = surveyVersion,
        respondentId = respondentId,
        answerCount = answerCount,
        createdAt = createdAt,
    )
