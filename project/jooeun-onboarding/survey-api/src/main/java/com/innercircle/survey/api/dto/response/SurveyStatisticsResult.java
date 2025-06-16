package com.innercircle.survey.api.dto.response;

import com.innercircle.survey.common.domain.QuestionType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 설문조사 통계 결과 DTO
 */
@Schema(description = "설문조사 통계 결과")
@Getter
@Builder
@AllArgsConstructor
public class SurveyStatisticsResult {

    @Schema(description = "설문조사 ID", example = "01HK123ABC456DEF789GHI012J")
    private final String surveyId;

    @Schema(description = "설문조사 제목", example = "2024년 고객 만족도 조사")
    private final String surveyTitle;

    @Schema(description = "전체 응답 수", example = "150")
    private final long totalResponseCount;

    @Schema(description = "통계 계산 시각")
    private final LocalDateTime calculatedAt;

    @Schema(description = "질문별 통계")
    private final List<QuestionStatistics> questionStatistics;

    @Schema(description = "응답 트렌드 (최근 30일)")
    private final ResponseTrend responseTrend;

    /**
     * 질문별 통계 정보
     */
    @Schema(description = "질문별 통계 정보")
    @Getter
    @Builder
    @AllArgsConstructor
    public static class QuestionStatistics {

        @Schema(description = "질문 ID", example = "01HK123ABC456DEF789GHI012J")
        private final String questionId;

        @Schema(description = "질문 제목", example = "서비스에 만족하십니까?")
        private final String questionTitle;

        @Schema(description = "질문 타입", example = "SINGLE_CHOICE")
        private final QuestionType questionType;

        @Schema(description = "이 질문에 응답한 총 응답자 수", example = "148")
        private final long responseCount;

        @Schema(description = "응답률 (전체 응답 대비)", example = "98.67")
        private final double responseRate;

        @Schema(description = "선택형 질문의 선택지별 통계 (텍스트형 질문의 경우 null)")
        private final List<ChoiceStatistics> choiceStatistics;

        @Schema(description = "텍스트형 질문의 통계 (선택형 질문의 경우 null)")
        private final TextStatistics textStatistics;
    }

    /**
     * 선택형 질문의 선택지별 통계
     */
    @Schema(description = "선택형 질문의 선택지별 통계")
    @Getter
    @Builder
    @AllArgsConstructor
    public static class ChoiceStatistics {

        @Schema(description = "선택지 텍스트", example = "매우 만족")
        private final String choiceText;

        @Schema(description = "이 선택지를 선택한 응답자 수", example = "75")
        private final long count;

        @Schema(description = "이 선택지 선택 비율 (해당 질문 응답자 대비)", example = "50.68")
        private final double percentage;

        @Schema(description = "이 선택지 선택 비율 (전체 응답자 대비)", example = "50.00")
        private final double percentageOfTotal;
    }

    /**
     * 텍스트형 질문의 통계
     */
    @Schema(description = "텍스트형 질문의 통계")
    @Getter
    @Builder
    @AllArgsConstructor
    public static class TextStatistics {

        @Schema(description = "평균 응답 길이 (문자 수)", example = "45.2")
        private final double averageLength;

        @Schema(description = "최대 응답 길이 (문자 수)", example = "250")
        private final int maxLength;

        @Schema(description = "최소 응답 길이 (문자 수)", example = "5")
        private final int minLength;

        @Schema(description = "빈 응답 수", example = "12")
        private final long emptyResponseCount;

        @Schema(description = "자주 등장하는 키워드 Top 10")
        private final List<KeywordFrequency> topKeywords;
    }

    /**
     * 키워드 빈도 정보
     */
    @Schema(description = "키워드 빈도 정보")
    @Getter
    @AllArgsConstructor
    public static class KeywordFrequency {

        @Schema(description = "키워드", example = "서비스")
        private final String keyword;

        @Schema(description = "등장 횟수", example = "42")
        private final long frequency;

        @Schema(description = "등장 비율", example = "28.38")
        private final double percentage;
    }

    /**
     * 응답 트렌드 정보
     */
    @Schema(description = "응답 트렌드 정보 (최근 30일)")
    @Getter
    @Builder
    @AllArgsConstructor
    public static class ResponseTrend {

        @Schema(description = "일별 응답 수")
        private final Map<String, Long> dailyResponseCount;

        @Schema(description = "최근 7일 평균 응답 수", example = "5.2")
        private final double averageResponsesLast7Days;

        @Schema(description = "최고 응답 수를 기록한 날", example = "2024-01-15")
        private final String peakDate;

        @Schema(description = "최고 응답 수", example = "23")
        private final long peakCount;
    }
}
