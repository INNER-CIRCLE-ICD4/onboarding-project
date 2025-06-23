package com.innercircle.onboarding.changzune_onboarding.survey.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

// 설문조사 수정 요청 DTO
@Getter
@Setter
public class SurveyUpdateRequest {

    // 설문 제목
    private String title;

    // 설문 설명
    private String description;

    // 설문에 포함된 질문 목록
    private List<SurveyRequest.QuestionRequest> questions;
}