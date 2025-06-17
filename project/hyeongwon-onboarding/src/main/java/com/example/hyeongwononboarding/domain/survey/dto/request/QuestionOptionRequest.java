package com.example.hyeongwononboarding.domain.survey.dto.request;

/**
 * 질문 옵션 요청 인터페이스
 * - CreateQuestionRequest와 UpdateQuestionRequest에서 사용하는 옵션 요청 인터페이스
 */
public interface QuestionOptionRequest {
    /**
     * @return 옵션 ID (기존 옵션 수정 시 사용)
     */
    String getId();
    
    /**
     * @return 옵션 텍스트
     */
    String getText();
    
    /**
     * @return 옵션 순서
     */
    Integer getOrder();
}
