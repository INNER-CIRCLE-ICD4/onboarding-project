package com.example.byeongjin_onboarding.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class SurveyCreateRequest {
    private String name;
    private String description;
    private List<FormItemDto> formItems;
}