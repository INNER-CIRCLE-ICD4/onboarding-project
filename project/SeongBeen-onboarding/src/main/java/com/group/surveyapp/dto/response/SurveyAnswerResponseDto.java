package com.group.surveyapp.dto.response;

import com.group.surveyapp.domain.entity.QuestionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 설문조사 응답 조회 응답 DTO
 * <p>
 * - 설문조사 응답 조회 API(/api/surveys/{surveyId}/responses) 응답값에 사용.
 * - 설문 항목별 응답 데이터 및 응답 메타 정보를 포함.
 * </p>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SurveyAnswerResponseDto {
    private Long id; // 설문 ID
    private String title; // 설문 제목
    private String description; // 설문 설명
    private List<QuestionAnswerDto> questions;// 질문 및 응답 정보

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class QuestionAnswerDto {
        private Long questionId;
        private String name;            // 질문 이름
        private String description;     // 질문 설명
        private QuestionType type;      // 질문 타입
        private boolean required;       // 필수 여부
        private List<String> candidates;// 후보값(선택형)
        private Object answer;          // 실제 응답값 (문자열/배열 등)
    }
}
