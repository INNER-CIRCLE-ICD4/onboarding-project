package com.survey.soyoung_onboarding.controller;

import com.survey.soyoung_onboarding.common.exception.ApiError;
import com.survey.soyoung_onboarding.common.exception.ApiException;
import com.survey.soyoung_onboarding.common.response.ApiResponse;
import com.survey.soyoung_onboarding.common.response.ApiResponseDto;
import com.survey.soyoung_onboarding.dto.SurveyDto;
import com.survey.soyoung_onboarding.service.CommonSerivce;
import com.survey.soyoung_onboarding.service.SurveyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("survey")
public class SurveyController {

    @Autowired
    private SurveyService surveyService;

    @PostMapping("add")
    public ResponseEntity<ApiResponseDto<Object>> createSurvey(
            @RequestBody SurveyDto survey,
            BindingResult bindingResult
    ) {
        try {
            survey.validate_add(bindingResult);
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

}
