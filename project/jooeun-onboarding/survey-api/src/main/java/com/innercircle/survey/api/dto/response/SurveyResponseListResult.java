package com.innercircle.survey.api.dto.response;

import com.innercircle.survey.domain.response.SurveyResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 설문조사 응답 목록 조회 결과 DTO
 */
@Schema(description = "설문조사 응답 목록 조회 결과")
@Getter
public class SurveyResponseListResult {

    @Schema(description = "설문조사 ID", example = "01HK123ABC456DEF789GHI012J")
    private final String surveyId;

    @Schema(description = "설문조사 제목", example = "2024년 고객 만족도 조사")
    private final String surveyTitle;

    @Schema(description = "총 응답 개수", example = "150")
    private final long totalResponseCount;

    @Schema(description = "응답 목록")
    private final List<SurveyResponseDetail> responses;

    @Schema(description = "조회 일시", example = "2024-01-15T16:30:00")
    private final LocalDateTime retrievedAt;

    /**
     * 응답 목록 결과 생성
     */
    public SurveyResponseListResult(String surveyId, String surveyTitle, List<SurveyResponse> responses) {
        this.surveyId = surveyId;
        this.surveyTitle = surveyTitle;
        this.totalResponseCount = responses.size();
        this.responses = responses.stream()
                .map(SurveyResponseDetail::new)
                .toList();
        this.retrievedAt = LocalDateTime.now();
    }

    /**
     * 응답 개수만 포함한 간단한 결과 생성
     */
    public static SurveyResponseListResult createSimpleResult(String surveyId, String surveyTitle, long totalCount) {
        return new SurveyResponseListResult(surveyId, surveyTitle, totalCount);
    }

    /**
     * 응답 개수만 포함한 생성자 (내부용)
     */
    private SurveyResponseListResult(String surveyId, String surveyTitle, long totalCount) {
        this.surveyId = surveyId;
        this.surveyTitle = surveyTitle;
        this.totalResponseCount = totalCount;
        this.responses = List.of(); // 빈 목록
        this.retrievedAt = LocalDateTime.now();
    }
}
