package com.survey.daheeonboarding.api.controller;


import com.survey.common.dto.*;
import com.survey.service.SurveyService;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@RestController
@RequestMapping("/api/surveys")
@RequiredArgsConstructor
public class SurveyController {
    private final SurveyService surveyService;

    /**
     * 설문 생성 API
     */
    @PostMapping
    public ResponseEntity<SurveyResponseDto> createSurvey(
            @RequestBody SurveyRequest request) {
        SurveyResponseDto result = surveyService.createSurvey(request);
        return ResponseEntity.ok(result);
    }

    /**
     * 설문 응답 제출 API
     */
    @PostMapping("/{surveyId}/sumbit")
    public ResponseEntity<ResponseDto> submitResponse(
            @PathVariable Long surveyId,
            @RequestBody ResponseRequest request) {
        ResponseDto dto = surveyService.submitResponse(surveyId, request);
        return ResponseEntity.ok(dto);
    }

    /**
     * 설문 문항 조회 API
     *  /api/surveys/{surveyId}/items?version=2
     */
    @GetMapping("/{surveyId}/items")
    public SurveyRequest getSurveyItems(@PathVariable Long surveyId,
                                        @RequestParam(required = false) Integer version) {
        return surveyService.getSurveyItems(surveyId, version);
    }

    /**
     * 설문 조회 API - 응답
     *  /api/surveys/1/responses?version=2&page=0&size=20&itemId=5&answer=Yes
     */
    @GetMapping("/{surveyId}/responses")
    public Page<SurveyAnswerResponseDto> getSurveyResponses(
            @PathVariable Long surveyId,
            @RequestParam(required = false) Integer version,
            @RequestParam(required = false) Long itemId,
            @RequestParam(required = false) String answer,

            // ■ 페이징 기본값 0,20 + 기본 정렬 submittedAt(desc), itemId(asc)
            @ParameterObject
            @PageableDefault(page = 0, size = 20)
            @SortDefault.SortDefaults({
                    @SortDefault(sort = "submittedAt", direction = Sort.Direction.DESC),
                    @SortDefault(sort = "itemId",       direction = Sort.Direction.ASC)
            })
            Pageable pageable
    ) {
        return surveyService.getSurveyResponses(surveyId, version, itemId, answer, pageable);
    }

    /**
     * 설문 수정 API
     * @param surveyId
     * @param request
     * @return
     */
    @PatchMapping("/{surveyId}/update")
    public SurveyResponseDto updateSurvey(
            @PathVariable Long surveyId,
            @RequestBody SurveyRequest request) {
        return surveyService.updateSurvey(surveyId, request);
    }
}
