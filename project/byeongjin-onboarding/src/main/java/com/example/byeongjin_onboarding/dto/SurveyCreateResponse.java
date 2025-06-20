package com.example.byeongjin_onboarding.dto;

import lombok.AllArgsConstructor; // 모든 필드를 인수로 받는 생성자 (Lombok 자동 생성)
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SurveyCreateResponse {
    private Long id;
    private String name;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<FormItemDto> formItems;
}