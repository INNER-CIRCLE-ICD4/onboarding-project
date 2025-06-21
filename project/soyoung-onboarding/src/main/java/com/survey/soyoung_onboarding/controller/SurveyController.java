package com.survey.soyoung_onboarding.controller;

import com.survey.soyoung_onboarding.common.exception.ApiError;
import com.survey.soyoung_onboarding.common.exception.ApiException;
import com.survey.soyoung_onboarding.common.response.ApiResponse;
import com.survey.soyoung_onboarding.common.response.ApiResponseDto;
import com.survey.soyoung_onboarding.dto.ReplyDto;
import com.survey.soyoung_onboarding.dto.SurveyDto;
import com.survey.soyoung_onboarding.service.SurveyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("survey")
public class SurveyController {

    @Autowired
    private SurveyService surveyService;

    /**
     * 설문조사 생성 API
     */
    @PostMapping("add")
    public ResponseEntity<ApiResponseDto<Object>> createSurvey(
            @RequestBody SurveyDto survey,
            BindingResult bindingResult) {
        try {
            survey.validate(bindingResult);
            if (bindingResult.hasErrors()) {
                throw new ApiException(ApiError.INVALID_PARAMETER);
            }

            surveyService.createSurvey(survey);
            return ApiResponse.success();

        } catch (ApiException e) {
            log.error(e.getMessage(), e);
            return ApiResponse.error(e.getError());
        }
    }

    /**
     * 설문조사 수정 API
     */
    @PutMapping("update")
    public ResponseEntity<ApiResponseDto<Object>> updateSurvey(
            @RequestBody SurveyDto survey,
            BindingResult bindingResult) {
        try {
            survey.validate(bindingResult);
            if (bindingResult.hasErrors()) {
                throw new ApiException(ApiError.INVALID_PARAMETER);
            }

            surveyService.updateSurvey(survey);
            return ApiResponse.success();

        } catch (ApiException e) {
            log.error(e.getMessage(), e);
            return ApiResponse.error(e.getError());
        }
    }

    /**
     * 설문조사 응답제출 API
     */
    @PostMapping("submit")
    public ResponseEntity<ApiResponseDto<Object>> submitSurveyReply(
            @RequestBody ReplyDto replyDto,
            BindingResult bindingResult) {
        try {
            replyDto.validate(bindingResult);
            if (bindingResult.hasErrors()) {
                throw new ApiException(ApiError.INVALID_PARAMETER);
            }

            surveyService.submitSurveyReply(replyDto);
            return ApiResponse.success();

        } catch (ApiException e) {
            log.error(e.getMessage(), e);
            return ApiResponse.error(e.getError());
        }
    }

    /**
     * 설문조사 응답 조회 API
     */
    @GetMapping("responses/{surveyId}")
    public ResponseEntity<ApiResponseDto<Object>> getSurveyResponses(@PathVariable UUID surveyId) {
        try {
            var responses = surveyService.getSurveyReplies(surveyId);
            return ApiResponse.success(responses);
        } catch (ApiException e) {
            log.error(e.getMessage(), e);
            return ApiResponse.error(e.getError());
        }
    }
}


