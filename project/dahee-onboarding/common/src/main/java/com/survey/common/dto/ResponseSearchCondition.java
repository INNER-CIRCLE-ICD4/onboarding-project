package com.survey.common.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseSearchCondition {
    private Long surveyId;
    private Integer version;
    private Long itemId;        // 특정 문항으로 검색
//    private String answer;      // 특정 답변값(주관식, 객관식 모두 가능)
//    private String question;    // 문항명 검색(옵션)
//    private String uuid;        // 특정 응답자 검색
//    private String keyword;     // 전체 문항/응답에서 단어 포함 여부(옵션)
    // 추후 추가: 기간, 상태, 선택지 번호, etc.
}
