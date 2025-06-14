package com.innercircle.survey.api.controller;

import com.innercircle.survey.api.dto.request.CreateSurveyRequest;
import com.innercircle.survey.api.dto.response.SurveyResponse;
import com.innercircle.survey.api.service.SurveyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 설문조사 REST API 컨트롤러
 * 
 * 설문조사 생성, 조회, 수정 기능을 제공합니다.
 */
@Slf4j
@RestController
@RequestMapping("/api/surveys")
@RequiredArgsConstructor
@Tag(name = "설문조사 API", description = "설문조사 생성, 조회, 수정 기능을 제공합니다.")
public class SurveyController {

    private final SurveyService surveyService;

    /**
     * 설문조사 생성 API
     */
    @Operation(
            summary = "설문조사 생성",
            description = "새로운 설문조사를 생성합니다. 1~10개의 설문 항목을 포함할 수 있습니다."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "설문조사 생성 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = SurveyResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청 데이터",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                    {
                                        "error": "INVALID_REQUEST",
                                        "message": "설문조사 제목은 필수입니다.",
                                        "timestamp": "2024-01-15T10:30:00"
                                    }
                                    """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "서버 내부 오류",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                    {
                                        "error": "INTERNAL_SERVER_ERROR",
                                        "message": "설문조사 생성에 실패했습니다.",
                                        "timestamp": "2024-01-15T10:30:00"
                                    }
                                    """
                            )
                    )
            )
    })
    @PostMapping
    public ResponseEntity<SurveyResponse> createSurvey(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "설문조사 생성 요청 데이터",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CreateSurveyRequest.class)
                    )
            )
            @Valid @RequestBody CreateSurveyRequest request) {
        
        log.info("설문조사 생성 요청 - 제목: {}", request.getTitle());
        
        SurveyResponse response = surveyService.createSurvey(request);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * 설문조사 조회 API
     */
    @Operation(
            summary = "설문조사 조회",
            description = "설문조사 ID로 설문조사 정보를 조회합니다."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "설문조사 조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = SurveyResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "설문조사를 찾을 수 없음",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                    {
                                        "error": "SURVEY_NOT_FOUND",
                                        "message": "설문조사를 찾을 수 없습니다: 01HK123ABC456DEF789GHI012J",
                                        "timestamp": "2024-01-15T10:30:00"
                                    }
                                    """
                            )
                    )
            )
    })
    @GetMapping("/{surveyId}")
    public ResponseEntity<SurveyResponse> getSurvey(
            @Parameter(description = "설문조사 ID", example = "01HK123ABC456DEF789GHI012J")
            @PathVariable String surveyId) {
        
        log.info("설문조사 조회 요청 - ID: {}", surveyId);
        
        SurveyResponse response = surveyService.getSurvey(surveyId);
        
        return ResponseEntity.ok(response);
    }

    /**
     * 설문조사 존재 여부 확인 API
     */
    @Operation(
            summary = "설문조사 존재 확인",
            description = "설문조사 ID로 설문조사 존재 여부를 확인합니다."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "존재 확인 성공",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            name = "존재하는 경우",
                                            value = """
                                            {
                                                "exists": true,
                                                "surveyId": "01HK123ABC456DEF789GHI012J"
                                            }
                                            """
                                    ),
                                    @ExampleObject(
                                            name = "존재하지 않는 경우",
                                            value = """
                                            {
                                                "exists": false,
                                                "surveyId": "01HK123ABC456DEF789GHI012J"
                                            }
                                            """
                                    )
                            }
                    )
            )
    })
    @GetMapping("/{surveyId}/exists")
    public ResponseEntity<Object> checkSurveyExists(
            @Parameter(description = "설문조사 ID", example = "01HK123ABC456DEF789GHI012J")
            @PathVariable String surveyId) {
        
        log.info("설문조사 존재 확인 요청 - ID: {}", surveyId);
        
        boolean exists = surveyService.existsSurvey(surveyId);
        
        return ResponseEntity.ok(java.util.Map.of(
                "exists", exists,
                "surveyId", surveyId
        ));
    }
}
