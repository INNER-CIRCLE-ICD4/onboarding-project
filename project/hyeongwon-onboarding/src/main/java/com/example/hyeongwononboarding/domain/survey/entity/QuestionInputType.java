package com.example.hyeongwononboarding.domain.survey.entity;

/**
 * 설문조사 질문 입력 타입 ENUM
 * - 단답형, 장문형, 단일 선택, 다중 선택
 */
public enum QuestionInputType {
    SHORT_TEXT,      // 단답형
    LONG_TEXT,       // 장문형
    SINGLE_CHOICE,   // 단일 선택 리스트
    MULTIPLE_CHOICE  // 다중 선택 리스트
}
