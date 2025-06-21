package com.innercircle.survey.api.controller;

import com.innercircle.survey.api.dto.response.SurveyResponseSearchResult;
import com.innercircle.survey.api.dto.response.SurveyStatisticsResult;
import com.innercircle.survey.api.service.SurveySearchService;
import com.innercircle.survey.api.service.SurveyStatisticsService;
import com.innercircle.survey.infrastructure.repository.SurveyResponseRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * 설문조사 고급 기능 REST API 컨트롤러
 * 
 * 응답 검색, 통계 분석 등의 고급 기능을 제공합니다.
 */
@Slf4j
@RestController
@RequestMapping("/api/surveys")
@RequiredArgsConstructor
@Tag(name = "설문조사 고급 API", description = "응답 검색, 통계 분석 등의 고급 기능을 제공합니다.")
public class SurveyAdvancedController {

    private final SurveySearchService surveySearchService;
    private final SurveyStatisticsService surveyStatisticsService;
    private final SurveyResponseRepository surveyResponseRepository;

    /**
     * 설문조사 응답 고급 검색 API
     */
    @Operation(
            summary = "설문조사 응답 고급 검색",
            description = """
                    설문조사 응답을 다양한 조건으로 검색합니다.
                    • 질문 제목 키워드로 검색
                    • 응답 값 키워드로 검색  
                    • 응답자 정보로 검색
                    • 기간별 검색
                    • 여러 조건을 조합하여 검색 가능
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "검색 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = SurveyResponseSearchResult.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "설문조사를 찾을 수 없음"
            )
    })
    @GetMapping("/{surveyId}/search-responses")
    public ResponseEntity<SurveyResponseSearchResult> searchSurveyResponses(
            @Parameter(description = "설문조사 ID", example = "01HK123ABC456DEF789GHI012J")
            @PathVariable String surveyId,
            
            @Parameter(description = "질문 제목 검색 키워드", example = "만족도")
            @RequestParam(required = false) String questionKeyword,
            
            @Parameter(description = "응답 값 검색 키워드", example = "매우 만족")
            @RequestParam(required = false) String answerKeyword,
            
            @Parameter(description = "응답자 정보 검색 키워드", example = "admin@company.com")
            @RequestParam(required = false) String respondentKeyword,
            
            @Parameter(description = "검색 시작일시", example = "2024-01-01T00:00:00")
            @RequestParam(required = false) 
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            
            @Parameter(description = "검색 종료일시", example = "2024-12-31T23:59:59")
            @RequestParam(required = false) 
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {

        log.info("설문조사 응답 고급 검색 요청 - surveyId: {}", surveyId);

        SurveyResponseSearchResult result = surveySearchService.searchResponses(
                surveyId, questionKeyword, answerKeyword, respondentKeyword, startDate, endDate);

        return ResponseEntity.ok(result);
    }

    /**
     * 설문조사 통계 조회 API
     */
    @Operation(
            summary = "설문조사 통계 분석",
            description = """
                    설문조사의 상세 통계를 조회합니다.
                    • 질문별 응답 통계
                    • 선택지별 선택 비율
                    • 응답 트렌드 분석
                    • 캐시를 활용하여 성능 최적화
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "통계 조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = SurveyStatisticsResult.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "설문조사를 찾을 수 없음"
            )
    })
    @GetMapping("/{surveyId}/statistics")
    public ResponseEntity<SurveyStatisticsResult> getSurveyStatistics(
            @Parameter(description = "설문조사 ID", example = "01HK123ABC456DEF789GHI012J")
            @PathVariable String surveyId) {

        log.info("설문조사 통계 조회 요청 - surveyId: {}", surveyId);

        SurveyStatisticsResult result = surveyStatisticsService.getSurveyStatistics(surveyId);

        return ResponseEntity.ok(result);
    }

    /**
     * 설문조사 응답 요약 정보 API
     */
    @Operation(
            summary = "설문조사 응답 요약 정보",
            description = "설문조사의 기본적인 응답 현황을 빠르게 조회합니다."
    )
    @GetMapping("/{surveyId}/summary")
    public ResponseEntity<Object> getSurveyResponseSummary(
            @Parameter(description = "설문조사 ID", example = "01HK123ABC456DEF789GHI012J")
            @PathVariable String surveyId) {

        log.info("설문조사 응답 요약 정보 조회 요청 - surveyId: {}", surveyId);

        long responseCount = surveyResponseRepository.countBySurveyId(surveyId);
        
        return ResponseEntity.ok(java.util.Map.of(
                "surveyId", surveyId,
                "totalResponseCount", responseCount,
                "timestamp", LocalDateTime.now()
        ));
    }
}
