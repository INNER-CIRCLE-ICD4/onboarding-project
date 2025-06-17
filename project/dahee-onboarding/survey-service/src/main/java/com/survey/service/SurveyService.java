package com.survey.service;


import com.survey.common.dto.*;

import java.util.List;

public interface SurveyService {
    /**
     * 설문 생성
     * @param request 설문 생성 요청 DTO
     * @return 생성된 설문 정보 DTO
     */
    SurveyResponseDto createSurvey(SurveyRequest request);

    /**
     * 설문 응답 제출
     * @param surveyId 설문 ID
     * @param request 응답 제출 요청 DTO
     * @return 저장된 응답 정보 DTO
     */
    ResponseDto submitResponse(Long surveyId, ResponseRequest request);

    /**
     * 설문 조회
     * @param surveyId
     * @return
     */
    List<SurveyAnswerResponseDto> getSurveyResponses(Long surveyId);
}
