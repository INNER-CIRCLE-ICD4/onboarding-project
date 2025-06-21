package com.innercircle.survey.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 설문조사 응답 검색 결과 DTO
 */
@Schema(description = "설문조사 응답 검색 결과")
@Getter
@AllArgsConstructor
public class SurveyResponseSearchResult {

    @Schema(description = "검색된 응답의 총 개수", example = "25")
    private final long totalCount;

    @Schema(description = "검색 조건")
    private final SearchCondition searchCondition;

    @Schema(description = "검색 결과 응답 목록")
    private final List<SurveyResponseSummary> responses;

    @Schema(description = "검색 실행 시각")
    private final LocalDateTime searchExecutedAt;

    /**
     * 검색 조건 정보
     */
    @Schema(description = "검색 조건")
    @Getter
    @AllArgsConstructor
    public static class SearchCondition {

        @Schema(description = "설문조사 ID", example = "01HK123ABC456DEF789GHI012J")
        private final String surveyId;

        @Schema(description = "질문 제목 키워드", example = "만족도")
        private final String questionKeyword;

        @Schema(description = "응답 값 키워드", example = "매우 만족")
        private final String answerKeyword;

        @Schema(description = "응답자 정보 키워드", example = "admin@company.com")
        private final String respondentKeyword;

        @Schema(description = "검색 시작일", example = "2024-01-01")
        private final LocalDateTime startDate;

        @Schema(description = "검색 종료일", example = "2024-12-31")
        private final LocalDateTime endDate;
    }

    /**
     * 응답 요약 정보
     */
    @Schema(description = "응답 요약 정보")
    @Getter
    @AllArgsConstructor
    public static class SurveyResponseSummary {

        @Schema(description = "응답 ID", example = "01HK456DEF789GHI012JKLM345N")
        private final String responseId;

        @Schema(description = "응답자 정보", example = "user@company.com")
        private final String respondentInfo;

        @Schema(description = "응답 제출 시각")
        private final LocalDateTime submittedAt;

        @Schema(description = "총 응답 문항 수", example = "5")
        private final int answeredQuestionCount;

        @Schema(description = "매칭된 응답 항목들 (검색 키워드가 포함된 응답들)")
        private final List<MatchedAnswer> matchedAnswers;
    }

    /**
     * 매칭된 응답 정보
     */
    @Schema(description = "검색 조건에 매칭된 응답 정보")
    @Getter
    @AllArgsConstructor
    public static class MatchedAnswer {

        @Schema(description = "질문 ID", example = "01HK123ABC456DEF789GHI012J")
        private final String questionId;

        @Schema(description = "질문 제목", example = "서비스에 만족하십니까?")
        private final String questionTitle;

        @Schema(description = "응답 값들", example = "[\"매우 만족\"]")
        private final List<String> answerValues;

        @Schema(description = "매칭 이유", example = "QUESTION_TITLE_MATCH, ANSWER_VALUE_MATCH")
        private final List<String> matchReasons;
    }
}
