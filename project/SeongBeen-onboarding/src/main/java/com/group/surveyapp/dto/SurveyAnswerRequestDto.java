package com.group.surveyapp.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import java.util.List;

/**
 * 설문조사 응답 제출 요청 DTO
 * <p>
 * - 설문조사 응답 제출 API(/api/surveys/{surveyId}/responses) 요청 본문에 사용.
 * - 각 설문 항목에 대한 사용자의 응답 값을 포함.
 * </p>
 */
@Data
public class SurveyAnswerRequestDto {
    @NotEmpty
    private List<AnswerDto> answers; // 설문 항목별 응답

    @Data
    public static class AnswerDto {
        private String questionName; // 항목 이름
        private Object answer;       // 응답 값(문자열 또는 배열)
    }
}
