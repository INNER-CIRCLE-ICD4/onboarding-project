package com.example.surveyProject.dto;

import com.example.surveyProject.common.SurveyInputType;
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
public class SurveyItemDto {
    private Long Id;
    private String itemName;
    private String description;
    private SurveyInputType inputType;
    private boolean required;
    private List<String> options; // 선택형인 경우만 값이 있음
    private String status;       //설문 삭제 유무
}
