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
     *  GET http://localhost:8080/surveys/1/responses?version=1&itemId=2&answer=%EB%A7%88%EC%9A%B4%EB%A7%9E%EC%A7%80
     *  GET http://localhost:8080/surveys/1/responses?itemId=3&answer=%EB%A7%9E%EC%B9%AD
     *
     */
//    @GetMapping("/{surveyId}/responses")
//    public Page<SurveyAnswerResponseDto> getSurveyResponses(
//            @PathVariable Long surveyId,
//            @RequestParam(required = false) Integer version,
//            @RequestParam(required = false) Long itemId,
//            @RequestParam(required = false) String answer,
//            @ParameterObject
//            @PageableDefault(page = 0, size = 20)
//            @SortDefault.SortDefaults({
//                    @SortDefault(sort = "submittedAt", direction = Sort.Direction.DESC),
//                    @SortDefault(sort = "id", direction = Sort.Direction.ASC)
//            }) Pageable pageable
//    ) {
//        return surveyService.getSurveyResponses(surveyId, version, itemId, answer, pageable);
//    }

    @GetMapping("/{surveyId}/responses")
    public Page<SurveyAnswerResponseDto> getSurveyResponses(
            @PathVariable Long surveyId,

            // DTO의 각 필드를 개별 쿼리 파라미터로 ‘explode’ 시킵니다.
            @ParameterObject
            @ModelAttribute
            ResponseSearchCondition cond,

            // 페이징·정렬도 같은 방식으로 풀어 주고 싶다면 여기도 @ParameterObject
            @ParameterObject
            @PageableDefault(page = 0, size = 20)
            @SortDefault.SortDefaults({
                    @SortDefault(sort = "submittedAt", direction = Sort.Direction.DESC),
                    @SortDefault(sort = "id",           direction = Sort.Direction.ASC)
            })
            Pageable pageable
    ) {
        cond.setSurveyId(surveyId);
        return surveyService.getSurveyResponses(cond, pageable);
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
