package com.innercircle.survey.api.dto.response;

import com.innercircle.survey.domain.response.SurveyResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 설문조사 응답 상세 DTO
 */
@Schema(description = "설문조사 응답 상세")
@Getter
public class SurveyResponseDetail {

    @Schema(description = "응답 ID", example = "01HK456DEF789GHI012JKLM345N")
    private final String responseId;

    @Schema(description = "응답자 정보", example = "user@company.com")
    private final String respondentInfo;

    @Schema(description = "응답한 질문 개수", example = "4")
    private final int answeredQuestionCount;

    @Schema(description = "응답 완료 여부", example = "true")
    private final boolean completed;

    @Schema(description = "응답 제출 일시", example = "2024-01-15T15:30:00")
    private final LocalDateTime submittedAt;

    @Schema(description = "개별 응답 목록")
    private final List<SubmittedAnswerDetail> answers;

    /**
     * 도메인 엔티티로부터 DTO 생성 (전체 응답 포함)
     */
    public SurveyResponseDetail(SurveyResponse surveyResponse) {
        this.responseId = surveyResponse.getId();
        this.respondentInfo = surveyResponse.getRespondentInfo();
        this.answeredQuestionCount = surveyResponse.getAnsweredQuestionCount();
        this.completed = surveyResponse.isCompleted();
        this.submittedAt = surveyResponse.getCreatedAt();
        this.answers = surveyResponse.getAnswers().stream()
                .map(SubmittedAnswerDetail::new)
                .toList();
    }

    /**
     * 요약 정보만 포함한 DTO 생성 (개별 응답 제외)
     */
    public static SurveyResponseDetail createSummaryOnly(SurveyResponse surveyResponse) {
        return new SurveyResponseDetail(
                surveyResponse.getId(),
                surveyResponse.getRespondentInfo(),
                surveyResponse.getAnsweredQuestionCount(),
                surveyResponse.isCompleted(),
                surveyResponse.getCreatedAt()
        );
    }

    /**
     * 요약 정보 전용 생성자 (내부용)
     */
    private SurveyResponseDetail(String responseId, String respondentInfo, int answeredQuestionCount, 
                               boolean completed, LocalDateTime submittedAt) {
        this.responseId = responseId;
        this.respondentInfo = respondentInfo;
        this.answeredQuestionCount = answeredQuestionCount;
        this.completed = completed;
        this.submittedAt = submittedAt;
        this.answers = List.of(); // 빈 목록
    }
}
