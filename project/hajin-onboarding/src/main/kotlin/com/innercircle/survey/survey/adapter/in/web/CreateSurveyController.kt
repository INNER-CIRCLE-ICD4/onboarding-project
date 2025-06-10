package com.innercircle.survey.survey.adapter.`in`.web

import com.innercircle.survey.common.dto.ApiResponse
import com.innercircle.survey.survey.adapter.`in`.web.dto.CreateSurveyRequest
import com.innercircle.survey.survey.adapter.`in`.web.dto.SurveyResponse
import com.innercircle.survey.survey.application.port.`in`.CreateSurveyUseCase
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

private val logger = KotlinLogging.logger {}

@Tag(name = "Survey", description = "설문조사 관리 API")
@RestController
@RequestMapping("/api/v1/surveys")
class CreateSurveyController(
    private val createSurveyUseCase: CreateSurveyUseCase,
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
        val survey = createSurveyUseCase.createSurvey(command)
        val response = SurveyResponse.from(survey)

        logger.info { "Survey created successfully: ${survey.id}" }

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(ApiResponse.success(response, "설문조사가 성공적으로 생성되었습니다."))
    }
}
