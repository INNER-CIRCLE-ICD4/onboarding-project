package com.innercircle.survey.api.dto.response;

import com.innercircle.survey.domain.response.SurveyResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 설문조사 응답 제출 결과 DTO
 */
@Schema(description = "설문조사 응답 제출 결과")
@Getter
public class SurveyResponseSubmissionResult {

    @Schema(description = "응답 ID", example = "01HK456DEF789GHI012JKLM345N")
    private final String responseId;

    @Schema(description = "설문조사 ID", example = "01HK123ABC456DEF789GHI012J")
    private final String surveyId;

    @Schema(description = "설문조사 제목", example = "2024년 고객 만족도 조사")
    private final String surveyTitle;

    @Schema(description = "응답자 정보", example = "user@company.com")
    private final String respondentInfo;

    @Schema(description = "제출된 응답 개수", example = "4")
    private final int submittedAnswerCount;

    @Schema(description = "제출 일시", example = "2024-01-15T15:30:00")
    private final LocalDateTime submittedAt;

    @Schema(description = "제출된 개별 응답 목록")
    private final List<SubmittedAnswerDetail> submittedAnswers;

    /**
     * 도메인 엔티티로부터 응답 DTO 생성
     */
    public SurveyResponseSubmissionResult(SurveyResponse surveyResponse) {
        this.responseId = surveyResponse.getId();
        this.surveyId = surveyResponse.getSurvey().getId();
        this.surveyTitle = surveyResponse.getSurvey().getTitle();
        this.respondentInfo = surveyResponse.getRespondentInfo();
        this.submittedAnswerCount = surveyResponse.getAnswers().size();
        this.submittedAt = surveyResponse.getCreatedAt();
        this.submittedAnswers = surveyResponse.getAnswers().stream()
                .map(SubmittedAnswerDetail::new)
                .toList();
    }

    /**
     * 간단한 결과 생성 (응답 상세 제외)
     */
    public static SurveyResponseSubmissionResult createSimpleResult(SurveyResponse surveyResponse) {
        return new SurveyResponseSubmissionResult(surveyResponse);
    }
}
