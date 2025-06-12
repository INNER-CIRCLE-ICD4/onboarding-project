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
 * 설문조사 생성 요청 DTO
 */
@Schema(description = "설문조사 생성 요청")
@Getter
@NoArgsConstructor
public class CreateSurveyRequest {

    @Schema(description = "설문조사 제목", example = "2024년 고객 만족도 조사")
    @NotBlank(message = "설문조사 제목은 필수입니다.")
    @Size(min = SurveyConstants.Survey.MIN_TITLE_LENGTH, 
          max = SurveyConstants.Survey.MAX_TITLE_LENGTH,
          message = "설문조사 제목은 {min}자 이상 {max}자 이하여야 합니다.")
    private String title;

    @Schema(description = "설문조사 설명", example = "고객 서비스 개선을 위한 만족도 조사입니다.")
    @Size(max = SurveyConstants.Survey.MAX_DESCRIPTION_LENGTH,
          message = "설문조사 설명은 {max}자 이하여야 합니다.")
    private String description;

    @Schema(description = "설문 항목 목록")
    @NotNull(message = "설문 항목은 필수입니다.")
    @Size(min = SurveyConstants.Survey.MIN_QUESTIONS_COUNT, 
          max = SurveyConstants.Survey.MAX_QUESTIONS_COUNT,
          message = "설문 항목은 {min}개 이상 {max}개 이하여야 합니다.")
    @Valid
    private List<CreateQuestionRequest> questions;

    /**
     * 생성자
     */
    public CreateSurveyRequest(String title, String description, List<CreateQuestionRequest> questions) {
        this.title = title;
        this.description = description;
        this.questions = questions;
    }
}
