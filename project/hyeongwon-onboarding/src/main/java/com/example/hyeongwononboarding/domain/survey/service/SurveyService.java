package com.example.hyeongwononboarding.domain.survey.service;

import com.example.hyeongwononboarding.domain.survey.dto.request.CreateSurveyRequest;
import com.example.hyeongwononboarding.domain.survey.dto.response.SurveyResponse;

/**
 * 설문조사 서비스 인터페이스
 * 설문 생성, 조회 등 핵심 비즈니스 로직을 정의합니다.
 */
public interface SurveyService {
    /** 
     * 설문조사 생성
     * @param request 설문조사 생성 요청 DTO
     * @return 생성된 설문조사 상세 응답 DTO
     */
    SurveyResponse createSurvey(CreateSurveyRequest request);
}
