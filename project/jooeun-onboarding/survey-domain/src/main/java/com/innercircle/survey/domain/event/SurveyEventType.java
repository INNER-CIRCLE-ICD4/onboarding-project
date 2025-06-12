package com.innercircle.survey.domain.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 설문조사 이벤트 타입을 정의하는 열거형
 */
@Getter
@RequiredArgsConstructor
public enum SurveyEventType {

    /**
     * 설문조사 생성
     */
    SURVEY_CREATED("설문조사 생성", "새로운 설문조사가 생성되었습니다."),

    /**
     * 설문조사 정보 수정
     */
    SURVEY_INFO_UPDATED("설문조사 정보 수정", "설문조사 제목 또는 설명이 수정되었습니다."),

    /**
     * 설문 항목 추가
     */
    QUESTION_ADDED("설문 항목 추가", "새로운 설문 항목이 추가되었습니다."),

    /**
     * 설문 항목 수정
     */
    QUESTION_UPDATED("설문 항목 수정", "기존 설문 항목이 수정되었습니다."),

    /**
     * 설문 항목 비활성화
     */
    QUESTION_DEACTIVATED("설문 항목 비활성화", "설문 항목이 비활성화되었습니다."),

    /**
     * 설문 전체 재구성
     */
    QUESTIONS_RESTRUCTURED("설문 전체 재구성", "설문 항목들이 전체적으로 재구성되었습니다."),

    /**
     * 응답 제출
     */
    RESPONSE_SUBMITTED("응답 제출", "새로운 응답이 제출되었습니다.");

    private final String displayName;
    private final String description;

    /**
     * 설문조사 구조 변경 이벤트인지 확인
     *
     * @return 구조 변경 이벤트 여부
     */
    public boolean isStructuralChange() {
        return this == QUESTION_ADDED || 
               this == QUESTION_UPDATED || 
               this == QUESTION_DEACTIVATED || 
               this == QUESTIONS_RESTRUCTURED;
    }

    /**
     * 응답 관련 이벤트인지 확인
     *
     * @return 응답 관련 이벤트 여부
     */
    public boolean isResponseRelated() {
        return this == RESPONSE_SUBMITTED;
    }
}
