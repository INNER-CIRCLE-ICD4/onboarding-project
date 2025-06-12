package com.innercircle.survey.survey.adapter.`in`.web

import com.innercircle.survey.common.dto.ApiResponse
import com.innercircle.survey.common.dto.PageRequest
import com.innercircle.survey.common.dto.PageResponse
import com.innercircle.survey.survey.adapter.`in`.web.dto.CreateSurveyRequest
import com.innercircle.survey.survey.adapter.`in`.web.dto.SurveyResponse
import com.innercircle.survey.survey.adapter.`in`.web.dto.SurveySummaryResponse
import com.innercircle.survey.survey.adapter.`in`.web.dto.UpdateSurveyRequest
import com.innercircle.survey.survey.adapter.`in`.web.dto.toCommand
import com.innercircle.survey.survey.adapter.`in`.web.dto.toResponse
import com.innercircle.survey.survey.adapter.out.persistence.dto.SurveySummaryProjection
import com.innercircle.survey.survey.application.port.`in`.SurveyUseCase
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
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

private val logger = KotlinLogging.logger {}

@Tag(name = "Survey", description = "설문조사 관리 API")
@RestController
@RequestMapping("/api/v1/surveys")
class SurveyController(
    private val surveyUseCase: SurveyUseCase,
) {
    @Operation(
        summary = "설문조사 생성",
        description = "새로운 설문조사를 생성합니다. 설문조사는 1개에서 10개의 질문을 포함할 수 있습니다.",
    )
    @ApiResponses(
        value = [
            io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "201",
                description = "설문조사 생성 성공",
            ),
            io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "400",
                description = "잘못된 요청 (유효성 검증 실패)",
            ),
        ],
    )
    @PostMapping
    fun createSurvey(
        @Valid @RequestBody request: CreateSurveyRequest,
    ): ResponseEntity<ApiResponse<SurveyResponse>> {
        logger.info { "Create survey request received: ${request.title}" }

        val command = request.toCommand()
        val survey = surveyUseCase.createSurvey(command)
        val response = survey.toResponse()

        logger.info { "Survey created successfully: ${survey.id}" }

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(ApiResponse.success(response, "설문조사가 성공적으로 생성되었습니다."))
    }

    @Operation(
        summary = "설문조사 단건 조회",
        description = "ID로 특정 설문조사를 조회합니다.",
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
    @GetMapping("/{surveyId}")
    fun getSurvey(
        @Parameter(description = "설문조사 ID", required = true)
        @PathVariable surveyId: UUID,
    ): ResponseEntity<ApiResponse<SurveyResponse>> {
        logger.info { "Get survey request received: $surveyId" }

        val survey = surveyUseCase.getSurveyById(surveyId)
        val response = survey.toResponse()

        return ResponseEntity.ok(
            ApiResponse.success(response),
        )
    }

    @Operation(
        summary = "설문조사 수정",
        description = "기존 설문조사를 수정합니다. 설문 항목이 추가/변경/삭제되더라도 기존 응답은 유지됩니다.",
    )
    @ApiResponses(
        value = [
            io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "설문조사 수정 성공",
            ),
            io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "400",
                description = "잘못된 요청 (유효성 검증 실패)",
            ),
            io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "404",
                description = "설문조사를 찾을 수 없음",
            ),
        ],
    )
    @PutMapping("/{surveyId}")
    fun updateSurvey(
        @Parameter(description = "설문조사 ID", required = true)
        @PathVariable surveyId: UUID,
        @Valid @RequestBody request: UpdateSurveyRequest,
    ): ResponseEntity<ApiResponse<SurveyResponse>> {
        logger.info { "Update survey request received: $surveyId" }

        val command = request.toCommand(surveyId)
        val survey = surveyUseCase.updateSurvey(command)
        val response = survey.toResponse()

        logger.info { "Survey updated successfully: ${survey.id}, version: ${survey.version}" }

        return ResponseEntity.ok(
            ApiResponse.success(response, "설문조사가 성공적으로 수정되었습니다."),
        )
    }

    @Operation(
        summary = "설문조사 목록 조회",
        description = "페이징을 적용하여 설문조사 목록을 조회합니다. summary=true 파라미터로 요약 정보만 조회할 수 있습니다.",
    )
    @ApiResponses(
        value = [
            io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "조회 성공",
            ),
        ],
    )
    @GetMapping
    fun getSurveys(
        @Parameter(description = "페이지 정보")
        @Valid pageRequest: PageRequest,
        @Parameter(description = "요약 정보만 조회할지 여부 (기본값: false)")
        @RequestParam(required = false, defaultValue = "false") summary: Boolean,
    ): ResponseEntity<*> {
        logger.info { "Get surveys request received: page=${pageRequest.page}, size=${pageRequest.size}, summary=$summary" }

        val pageable = pageRequest.toPageable()

        return if (summary) {
            val surveySummaries = surveyUseCase.getSurveySummaries(pageable)
            val response = PageResponse.of(surveySummaries) { it.toSummaryResponse() }
            ResponseEntity.ok(response)
        } else {
            val surveysPage = surveyUseCase.getSurveys(pageable)
            val response = PageResponse.of(surveysPage) { it.toResponse() }
            ResponseEntity.ok(response)
        }
    }
}

fun SurveySummaryProjection.toSummaryResponse(): SurveySummaryResponse =
    SurveySummaryResponse(
        id = id.toString(),
        title = title,
        description = description,
        version = version,
        questionCount = questionCount,
        createdAt = createdAt,
    )
