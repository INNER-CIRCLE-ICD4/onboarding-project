package com.example.byeongjin_onboarding.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class SubmitAnswerRequest {
    private Long surveyId;
    private Map<Long, Object> responses;
}