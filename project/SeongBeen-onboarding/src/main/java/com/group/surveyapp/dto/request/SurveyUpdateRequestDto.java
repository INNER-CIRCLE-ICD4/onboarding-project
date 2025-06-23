package com.group.surveyapp.dto.request;

import com.group.surveyapp.domain.entity.QuestionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 설문조사 수정 요청 DTO
 * <p>
 * - 설문조사 수정 API(/api/surveys/{surveyId}) 요청 본문에 사용.
 * - 설문 이름, 설명, 설문 받을 항목 목록을 포함.
 * </p>
 */
@Data
public class SurveyUpdateRequestDto {

    @NotBlank
    private String title; // 설문조사 이름

    private String description; // 설문조사 설명

    @NotEmpty
    @Size(min = 1, max = 10)
    private List<QuestionDto> questions; // 설문 받을 항목

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class QuestionDto {

        private Long questionId; // 기존 질문 수정용 ID 필드

        @NotBlank
        private String name; // 항목 이름

        private String description; // 항목 설명

        @NotNull
        private QuestionType type; // 항목 타입

        private boolean required; // 필수 여부

        private List<String> candidates; // 선택형에만 해당
    }
}
