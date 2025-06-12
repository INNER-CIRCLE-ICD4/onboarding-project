package com.innercircle.survey.api.dto.response;

import com.innercircle.survey.domain.survey.Survey;
import com.innercircle.survey.domain.survey.SurveyQuestion;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 설문조사 응답 DTO
 */
@Schema(description = "설문조사 응답")
@Getter
public class SurveyResponse {

    @Schema(description = "설문조사 ID", example = "01HK123ABC456DEF789GHI012J")
    private final String id;

    @Schema(description = "설문조사 제목", example = "2024년 고객 만족도 조사")
    private final String title;

    @Schema(description = "설문조사 설명", example = "고객 서비스 개선을 위한 만족도 조사입니다.")
    private final String description;

    @Schema(description = "설문 항목 목록")
    private final List<QuestionResponse> questions;

    @Schema(description = "생성 일시", example = "2024-01-15T10:30:00")
    private final LocalDateTime createdAt;

    @Schema(description = "수정 일시", example = "2024-01-15T14:20:00")
    private final LocalDateTime updatedAt;

    @Schema(description = "버전", example = "1")
    private final Long version;

    /**
     * 도메인 엔티티로부터 응답 DTO 생성
     */
    public SurveyResponse(Survey survey) {
        this.id = survey.getId();
        this.title = survey.getTitle();
        this.description = survey.getDescription();
        this.questions = survey.getActiveQuestions().stream()
                .map(QuestionResponse::new)
                .toList();
        this.createdAt = survey.getCreatedAt();
        this.updatedAt = survey.getUpdatedAt();
        this.version = survey.getVersion();
    }

    /**
     * 활성 질문만으로 응답 DTO 생성
     */
    public static SurveyResponse fromSurveyWithActiveQuestions(Survey survey) {
        return new SurveyResponse(survey);
    }

    /**
     * 모든 질문으로 응답 DTO 생성 (관리용)
     */
    public static SurveyResponse fromSurveyWithAllQuestions(Survey survey, List<SurveyQuestion> allQuestions) {
        SurveyResponse response = new SurveyResponse(survey);
        // 필요시 구현 - 현재는 활성 질문만 반환
        return response;
    }
}
