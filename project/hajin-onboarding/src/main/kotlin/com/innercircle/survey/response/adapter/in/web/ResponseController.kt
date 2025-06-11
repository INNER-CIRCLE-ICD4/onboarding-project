package com.innercircle.survey.response.adapter.`in`.web

import com.innercircle.survey.common.dto.ApiResponse
import com.innercircle.survey.response.adapter.`in`.web.dto.ResponseDto
import com.innercircle.survey.response.adapter.`in`.web.dto.SubmitResponseRequest
import com.innercircle.survey.response.application.port.`in`.ResponseUseCase
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

private val logger = KotlinLogging.logger {}

@Tag(name = "Response", description = "설문조사 응답 API")
@RestController
@RequestMapping("/api/v1/surveys")
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
    @PostMapping("/{surveyId}/responses")
    fun submitResponse(
        @Parameter(description = "설문조사 ID", required = true)
        @PathVariable surveyId: UUID,
        @Valid @RequestBody request: SubmitResponseRequest,
    ): ResponseEntity<ApiResponse<ResponseDto>> {
        logger.info { "Submit response request received for survey: $surveyId" }

        val command = request.toSubmitResponseCommand(surveyId)
        val response = responseUseCase.submitResponse(command)
        val responseDto = ResponseDto.from(response)

        logger.info { "Response submitted successfully: ${response.id}" }

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(ApiResponse.success(responseDto, "응답이 성공적으로 제출되었습니다."))
    }
}
