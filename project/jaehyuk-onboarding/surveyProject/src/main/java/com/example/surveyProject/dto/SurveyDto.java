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
public class SurveyDto {
    private Long Id;
    private String title;
    private String description;
    private List<SurveyItemDto> items;
    private String status;     //설문 삭제 유무   0: 일반, 1: 삭제
}
