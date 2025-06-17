package com.example.surveyProject.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * SurveyCreateRequest 생성 DTO
 *
 * @author Jpark
 * @date 2025-06-16
 * @Calss SurveyCreateRequest
 */

@Getter
@Setter
public class SurveyCreateRequestDto {

    private String title;
    private String description;
    private List<SurveyItemCreateRequestDto> items;
}
