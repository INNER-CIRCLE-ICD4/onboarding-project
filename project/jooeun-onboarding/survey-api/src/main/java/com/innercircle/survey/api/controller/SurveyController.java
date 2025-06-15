package com.innercircle.survey.api.controller;

import com.innercircle.survey.api.dto.request.CreateSurveyRequest;
import com.innercircle.survey.api.dto.request.UpdateSurveyRequest;
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
                            schema = @Schema(implementation = CreateSurveyRequest.class),
                            examples = @ExampleObject(
                                    name = "고객 만족도 조사 예시",
                                    value = """
                                    {
                                        "title": "2024년 고객 만족도 조사",
                                        "description": "고객 서비스 개선을 위한 만족도 조사입니다.",
                                        "createdBy": "admin@company.com",
                                        "questions": [
                                            {
                                                "title": "전반적인 서비스에 만족하십니까?",
                                                "description": "서비스 전반에 대한 만족도를 평가해 주세요.",
                                                "questionType": "SINGLE_CHOICE",
                                                "required": true,
                                                "options": ["매우 불만족", "불만족", "보통", "만족", "매우 만족"]
                                            },
                                            {
                                                "title": "개선사항이나 의견을 자유롭게 작성해 주세요.",
                                                "description": "더 나은 서비스를 위한 소중한 의견을 들려주세요.",
                                                "questionType": "LONG_TEXT",
                                                "required": false
                                            }
                                        ]
                                    }
                                    """
                            )
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
     * 설문조사 수정 API
     */
    @Operation(
            summary = "설문조사 수정",
            description = """
                    기존 설문조사를 수정합니다. 
                    • 제목과 설명을 수정할 수 있습니다.
                    • 질문을 추가/수정/삭제할 수 있습니다.
                    • 기존 응답 보존을 위해 기존 질문은 비활성화되고 새 질문이 추가됩니다.
                    • 생성자만 수정할 수 있습니다.
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "설문조사 수정 성공",
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
                    responseCode = "403",
                    description = "권한 없음 (생성자가 아님)",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                    {
                                        "error": "ACCESS_DENIED",
                                        "message": "설문조사 수정 권한이 없습니다.",
                                        "timestamp": "2024-01-15T10:30:00"
                                    }
                                    """
                            )
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
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "동시성 충돌 (낙관적 락)",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                    {
                                        "error": "OPTIMISTIC_LOCK_EXCEPTION",
                                        "message": "다른 사용자가 동시에 수정했습니다. 최신 버전을 다시 조회하여 수정해주세요.",
                                        "timestamp": "2024-01-15T10:30:00"
                                    }
                                    """
                            )
                    )
            )
    })
    @PutMapping("/{surveyId}")
    public ResponseEntity<SurveyResponse> updateSurvey(
            @Parameter(description = "설문조사 ID", example = "01HK123ABC456DEF789GHI012J")
            @PathVariable String surveyId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "설문조사 수정 요청 데이터",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UpdateSurveyRequest.class),
                            examples = @ExampleObject(
                                    name = "설문조사 수정 예시",
                                    value = """
                                    {
                                        "title": "2024년 상반기 고객 만족도 조사 (수정됨)",
                                        "description": "고객 서비스 개선을 위한 만족도 조사입니다. (업데이트된 설명)",
                                        "modifiedBy": "admin@company.com",
                                        "questions": [
                                            {
                                                "title": "전반적인 서비스에 만족하십니까?",
                                                "description": "서비스 전반에 대한 만족도를 평가해 주세요.",
                                                "questionType": "SINGLE_CHOICE",
                                                "required": true,
                                                "options": ["매우 불만족", "불만족", "보통", "만족", "매우 만족", "탁월함"]
                                            },
                                            {
                                                "title": "가장 개선이 필요한 부분은 무엇입니까?",
                                                "description": "개선이 필요한 영역을 모두 선택해 주세요.",
                                                "questionType": "MULTIPLE_CHOICE",
                                                "required": true,
                                                "options": ["응답 속도", "친절도", "전문성", "접근성", "기타"]
                                            },
                                            {
                                                "title": "추가 의견이나 제안사항을 자유롭게 작성해 주세요.",
                                                "description": "더 나은 서비스를 위한 소중한 의견을 들려주세요.",
                                                "questionType": "LONG_TEXT",
                                                "required": false
                                            }
                                        ]
                                    }
                                    """
                            )
                    )
            )
            @Valid @RequestBody UpdateSurveyRequest request) {
        
        log.info("설문조사 수정 요청 - ID: {}, 제목: {}, 수정자: {}", surveyId, request.getTitle(), request.getModifiedBy());
        
        SurveyResponse response = surveyService.updateSurvey(surveyId, request);
        
        return ResponseEntity.ok(response);
    }

    /**
     * 설문조사 비활성화(삭제) API
     */
    @Operation(
            summary = "설문조사 비활성화",
            description = """
                    설문조사를 비활성화(논리적 삭제)합니다.
                    • 기존 응답은 보존됩니다.
                    • 비활성화된 설문조사는 새로운 응답을 받을 수 없습니다.
                    • 생성자만 비활성화할 수 있습니다.
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "설문조사 비활성화 성공"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "권한 없음 (생성자가 아님)",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                    {
                                        "error": "ACCESS_DENIED",
                                        "message": "설문조사 비활성화 권한이 없습니다.",
                                        "timestamp": "2024-01-15T10:30:00"
                                    }
                                    """
                            )
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
    @DeleteMapping("/{surveyId}")
    public ResponseEntity<Void> deactivateSurvey(
            @Parameter(description = "설문조사 ID", example = "01HK123ABC456DEF789GHI012J")
            @PathVariable String surveyId,
            @Parameter(description = "삭제 요청자 식별자", example = "admin@company.com")
            @RequestParam String requestedBy) {
        
        log.info("설문조사 비활성화 요청 - ID: {}, 요청자: {}", surveyId, requestedBy);
        
        surveyService.deactivateSurvey(surveyId, requestedBy);
        
        return ResponseEntity.noContent().build();
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
