package com.example.hyeongwononboarding.domain.survey.dto.response;

import com.example.hyeongwononboarding.domain.survey.entity.QuestionInputType;
import lombok.Builder;
import lombok.Getter;
import java.util.List;

/**
 * 설문조사 질문 응답 DTO
 * - 설문조사 조회 시 각 질문의 정보를 반환하는 데이터 구조입니다.
 */
@Getter
@Builder
public class QuestionResponse {
    private String id;
    private String name;
    private String description;
    private QuestionInputType inputType;
    private Boolean isRequired;
    private Integer order;
    private List<OptionResponse> options;

    @Getter
    @Builder
    public static class OptionResponse {
        private String id;
        private String text;
        private Integer order;
    }
}
