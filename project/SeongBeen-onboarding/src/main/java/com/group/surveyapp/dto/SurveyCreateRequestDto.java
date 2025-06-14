package com.group.surveyapp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

/**
 * 설문조사 생성 요청 DTO
 * <p>
 * - 설문조사 생성 API(/api/surveys) 요청 본문에 사용.
 * - 설문 이름, 설명, 설문 받을 항목 목록을 포함.
 * </p>
 */
@Data
public class SurveyCreateRequestDto {
    @NotBlank
    private String title; // 설문조사 이름

    private String description; // 설문조사 설명

    @NotEmpty
    @Size(min = 1, max = 10)
    private List<QuestionDto> questions; // 설문 받을 항목

    @Data
    public static class QuestionDto {
        @NotBlank
        private String name; // 항목 이름

        private String description; // 항목 설명

        private QuestionType type; // 입력 형태(enum)

        private boolean required; // 필수 여부

        private List<String> candidates; // 선택형에만 해당
    }

    public enum QuestionType {
        SHORT,     // 단답형
        LONG,      // 장문형
        SINGLE,    // 단일 선택 리스트
        MULTI      // 다중 선택 리스트
    }
}
