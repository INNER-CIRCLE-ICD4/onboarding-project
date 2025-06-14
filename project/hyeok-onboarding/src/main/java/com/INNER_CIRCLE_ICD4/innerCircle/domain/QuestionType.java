package com.INNER_CIRCLE_ICD4.innerCircle.domain;

public enum QuestionType {
    SHORT,             // 단답형
    LONG,              // 장문형
    SINGLE_CHOICE,     // 단일 선택 리스트

    // 기존 MULTI_CHOICE 대신, 테스트에서 쓰는 MULTIPLE_CHOICE 로 변경
    MULTIPLE_CHOICE    // 다중 선택 리스트
}
