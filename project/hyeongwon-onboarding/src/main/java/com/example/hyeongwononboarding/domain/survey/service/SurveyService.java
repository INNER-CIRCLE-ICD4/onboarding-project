package com.example.hyeongwononboarding.domain.survey.service;

import com.example.hyeongwononboarding.domain.survey.dto.request.CreateSurveyRequest;
import com.example.hyeongwononboarding.domain.survey.dto.request.UpdateSurveyRequest;
import com.example.hyeongwononboarding.domain.survey.dto.response.SurveyResponse;
import java.util.List;

/**
 * 설문조사 서비스 인터페이스
 * 설문 생성, 조회, 수정 등 핵심 비즈니스 로직을 정의합니다.
 */
public interface SurveyService {
    /** 
     * 설문조사 생성
     * @param request 설문조사 생성 요청 DTO
     * @return 생성된 설문조사 상세 응답 DTO
     */
    SurveyResponse createSurvey(CreateSurveyRequest request);

    /**
     * 모든 설문조사 목록 조회
     * @return 설문조사 응답 DTO 목록
     */
    List<SurveyResponse> getAllSurveys();

    /**
     * ID로 설문조사 단건 조회
     * @param surveyId 조회할 설문조사 ID
     * @return 설문조사 응답 DTO
     */
    SurveyResponse getSurveyById(String surveyId);
    
    /**
     * 설문조사 수정
     * @param surveyId 수정할 설문조사 ID
     * @param request 설문조사 수정 요청 DTO
     * @return 수정된 설문조사 응답 DTO
     */
    SurveyResponse updateSurvey(String surveyId, UpdateSurveyRequest request);
}
