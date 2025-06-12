package com.survey.core.enums;

/***
 * 미리 허용된 타입만 쓰도록 강제, 잘못된 타입 입력시 컴파일/파싱 단계 오류
 */
public enum QuestionType {
    SHORT_TEXT, //단답형
    LONG_TEXT, //장문형
    SINGLE_CHOICE, //단일 선택
    MULTI_CHOICE //다중 선택
}


