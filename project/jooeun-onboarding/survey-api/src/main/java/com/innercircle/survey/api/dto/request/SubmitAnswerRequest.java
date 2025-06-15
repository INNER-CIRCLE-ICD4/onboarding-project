package com.innercircle.survey.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 개별 질문 응답 요청 DTO
 */
@Schema(description = "개별 질문 응답")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SubmitAnswerRequest {

    @Schema(description = "질문 ID", example = "01HK123ABC456DEF789GHI012J", required = true)
    @NotBlank(message = "질문 ID는 필수입니다.")
    private String questionId;

    @Schema(description = "응답 값 목록", 
            example = "[\"매우 만족\"]", 
            required = true)
    @NotEmpty(message = "응답 값은 최소 1개 이상이어야 합니다.")
    private List<String> answerValues;

    /**
     * 응답 요청 유효성 검증
     */
    public void validate() {
        if (questionId == null || questionId.trim().isEmpty()) {
            throw new IllegalArgumentException("질문 ID는 필수입니다.");
        }

        if (answerValues == null || answerValues.isEmpty()) {
            throw new IllegalArgumentException("응답 값은 최소 1개 이상이어야 합니다.");
        }

        // 빈 문자열 응답 검증
        boolean hasValidAnswer = answerValues.stream()
                .anyMatch(value -> value != null && !value.trim().isEmpty());
        
        if (!hasValidAnswer) {
            throw new IllegalArgumentException("유효한 응답 값이 없습니다.");
        }

        // 각 응답 값 길이 검증 (10,000자 제한)
        answerValues.forEach(value -> {
            if (value != null && value.length() > 10000) {
                throw new IllegalArgumentException("응답 값은 10,000자 이하여야 합니다.");
            }
        });
    }

    /**
     * 단일 응답 값 조회 (텍스트 질문, 단일 선택용)
     *
     * @return 첫 번째 응답 값
     */
    public String getSingleValue() {
        return answerValues != null && !answerValues.isEmpty() ? answerValues.get(0) : null;
    }

    /**
     * 빈 응답인지 확인
     *
     * @return 빈 응답 여부
     */
    public boolean isEmpty() {
        return answerValues == null || answerValues.isEmpty() ||
               answerValues.stream().allMatch(value -> value == null || value.trim().isEmpty());
    }
}
