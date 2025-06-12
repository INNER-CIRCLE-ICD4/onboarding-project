package com.group.surveyapp.dto.response;

import com.group.surveyapp.domain.entity.QuestionType;
import lombok.Data;
import java.util.List;

/**
 * 설문조사 생성/수정 응답 DTO
 * <p>
 * - 설문조사 생성 및 수정 API의 응답값으로 사용.
 * - 설문조사 기본 정보와 문항 정보를 포함.
 * </p>
 */
@Data
public class SurveyResponseDto {
    private Long surveyId; // 설문 고유 ID
    private String title; // 설문 제목
    private String description; // 설문 설명
    private String createdAt; // 설문 생성 시각
    private List<QuestionDto> questions; // 설문 문항 리스트

    @Data
    public static class QuestionDto {
        private Long questionId;
        private String name;
        private String description;
        private QuestionType type;
        private boolean required;
        private List<String> candidates;
    }
}
