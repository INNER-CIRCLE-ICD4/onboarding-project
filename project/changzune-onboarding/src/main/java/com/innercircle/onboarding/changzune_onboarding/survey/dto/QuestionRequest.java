package com.innercircle.onboarding.changzune_onboarding.survey.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class QuestionRequest {

    private Long id;  // 🔥 여기가 핵심! 수정 시 기존 질문 구분용

    private String name;
    private String description;
    private String type;
    private boolean required;
    private List<String> options;
}