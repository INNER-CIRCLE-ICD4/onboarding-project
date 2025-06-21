package com.innercircle.survey.api.dto.request;

import com.innercircle.survey.common.constants.SurveyConstants;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 설문조사 응답 제출 요청 DTO
 */
@Schema(description = "설문조사 응답 제출 요청")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SubmitSurveyResponseRequest {

    @Schema(description = "응답자 정보 (선택사항)", example = "user@company.com")
    @Size(max = 100, message = "응답자 정보는 100자 이하여야 합니다.")
    private String respondentInfo;

    @Schema(description = "개별 질문 응답 목록", required = true)
    @NotEmpty(message = "응답은 최소 1개 이상이어야 합니다.")
    @Valid
    private List<SubmitAnswerRequest> answers;

    /**
     * 응답 요청 유효성 검증
     */
    public void validate() {
        if (answers == null || answers.isEmpty()) {
            throw new IllegalArgumentException("응답은 최소 1개 이상이어야 합니다.");
        }

        if (answers.size() > SurveyConstants.Survey.MAX_QUESTIONS_COUNT) {
            throw new IllegalArgumentException("응답 개수가 질문 개수 제한을 초과했습니다.");
        }

        // 개별 응답 검증
        answers.forEach(SubmitAnswerRequest::validate);
    }

    /**
     * 특정 질문의 응답 조회
     *
     * @param questionId 질문 ID
     * @return 해당 질문의 응답 (없으면 null)
     */
    public SubmitAnswerRequest getAnswerByQuestionId(String questionId) {
        return answers.stream()
                .filter(answer -> answer.getQuestionId().equals(questionId))
                .findFirst()
                .orElse(null);
    }

    /**
     * 모든 질문 ID 목록 조회
     *
     * @return 질문 ID 목록
     */
    public List<String> getQuestionIds() {
        return answers.stream()
                .map(SubmitAnswerRequest::getQuestionId)
                .toList();
    }
}
