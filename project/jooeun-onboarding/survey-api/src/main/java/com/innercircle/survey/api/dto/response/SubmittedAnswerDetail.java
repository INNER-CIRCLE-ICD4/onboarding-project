package com.innercircle.survey.api.dto.response;

import com.innercircle.survey.common.domain.QuestionType;
import com.innercircle.survey.domain.response.SurveyAnswer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.List;

/**
 * 제출된 개별 응답 상세 DTO
 */
@Schema(description = "제출된 개별 응답 상세")
@Getter
public class SubmittedAnswerDetail {

    @Schema(description = "응답 ID", example = "01HK789GHI012JKLM345NOPQ678")
    private final String answerId;

    @Schema(description = "질문 ID", example = "01HK123ABC456DEF789GHI012J")
    private final String questionId;

    @Schema(description = "질문 제목", example = "서비스에 만족하십니까?")
    private final String questionTitle;

    @Schema(description = "질문 타입", example = "SINGLE_CHOICE")
    private final QuestionType questionType;

    @Schema(description = "제출된 응답 값 목록", example = "[\"매우 만족\"]")
    private final List<String> submittedValues;

    @Schema(description = "응답 요약", example = "매우 만족")
    private final String answerSummary;

    /**
     * 도메인 엔티티로부터 DTO 생성
     */
    public SubmittedAnswerDetail(SurveyAnswer surveyAnswer) {
        this.answerId = surveyAnswer.getId();
        this.questionId = surveyAnswer.getQuestionId();
        this.questionTitle = surveyAnswer.getQuestionTitle();
        this.questionType = surveyAnswer.getQuestionType();
        this.submittedValues = surveyAnswer.getAllAnswers();
        this.answerSummary = surveyAnswer.getAnswerSummary();
    }
}
