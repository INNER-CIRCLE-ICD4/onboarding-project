package com.example.hyeongwononboarding.domain.survey.dto.request;

import com.example.hyeongwononboarding.domain.survey.entity.QuestionInputType;

import java.util.List;

/**
 * 설문조사 질문 요청 인터페이스
 * - CreateQuestionRequest와 UpdateQuestionRequest의 공통 메서드를 정의합니다.
 */
public interface QuestionRequest {
    String getName();
    String getDescription();
    QuestionInputType getInputType();
    Boolean getIsRequired();
    Integer getOrder();
    /**
     * @return 질문 옵션 목록 (String 또는 QuestionOptionRequest)
     */
    List<?> getOptions();
}
