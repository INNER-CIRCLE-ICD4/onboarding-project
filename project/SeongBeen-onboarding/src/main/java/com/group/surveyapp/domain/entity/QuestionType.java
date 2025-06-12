package com.group.surveyapp.domain.entity;

/**
 * 설문 항목 타입
 * SHORT: 단답형, LONG: 장문형, SINGLE: 단일선택, MULTI: 다중선택
 */
public enum QuestionType {
    SHORT,   // 단답형
    LONG,    // 장문형
    SINGLE,  // 단일 선택 리스트
    MULTI    // 다중 선택 리스트
}
