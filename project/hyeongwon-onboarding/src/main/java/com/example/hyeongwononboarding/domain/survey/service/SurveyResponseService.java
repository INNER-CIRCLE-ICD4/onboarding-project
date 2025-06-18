package com.example.hyeongwononboarding.domain.survey.service;

import com.example.hyeongwononboarding.domain.survey.dto.request.SubmitSurveyResponseRequest;
import com.example.hyeongwononboarding.domain.survey.dto.response.SubmitSurveyResponseResponse;

/**
 * 설문 응답 서비스 인터페이스
 */
public interface SurveyResponseService {
    SubmitSurveyResponseResponse submitSurveyResponse(String surveyId, SubmitSurveyResponseRequest request);
}
