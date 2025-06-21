package com.innercircle.survey.api.dto.request;

import com.innercircle.survey.common.constants.SurveyConstants;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 설문조사 수정 요청 DTO
 * 
 * 설문조사 제목, 설명, 질문 목록을 수정할 수 있습니다.
 * 기존 응답 보존을 위해 질문 수정 시 기존 질문은 비활성화되고 새 질문이 추가됩니다.
 */
@Schema(description = "설문조사 수정 요청")
@Getter
@NoArgsConstructor
public class UpdateSurveyRequest {

    @Schema(description = "설문조사 제목", example = "2024년 상반기 고객 만족도 조사 (수정됨)")
    @NotBlank(message = "설문조사 제목은 필수입니다.")
    @Size(min = SurveyConstants.Survey.MIN_TITLE_LENGTH, 
          max = SurveyConstants.Survey.MAX_TITLE_LENGTH,
          message = "설문조사 제목은 {min}자 이상 {max}자 이하여야 합니다.")
    private String title;

    @Schema(description = "설문조사 설명", example = "고객 서비스 개선을 위한 만족도 조사입니다. (업데이트된 설명)")
    @Size(max = SurveyConstants.Survey.MAX_DESCRIPTION_LENGTH,
          message = "설문조사 설명은 {max}자 이하여야 합니다.")
    private String description;

    @Schema(description = "수정할 설문 항목 목록 (기존 항목은 비활성화되고 새 항목이 추가됩니다)")
    @NotNull(message = "설문 항목은 필수입니다.")
    @Size(min = SurveyConstants.Survey.MIN_QUESTIONS_COUNT, 
          max = SurveyConstants.Survey.MAX_QUESTIONS_COUNT,
          message = "설문 항목은 {min}개 이상 {max}개 이하여야 합니다.")
    @Valid
    private List<CreateQuestionRequest> questions;

    @Schema(description = "수정자 식별자 (생성자 권한 확인용)", example = "admin@company.com")
    @NotBlank(message = "수정자 정보는 필수입니다.")
    private String modifiedBy;

    /**
     * 생성자
     */
    public UpdateSurveyRequest(String title, String description, List<CreateQuestionRequest> questions, String modifiedBy) {
        this.title = title;
        this.description = description;
        this.questions = questions;
        this.modifiedBy = modifiedBy;
    }
}
