package com.innercircle.survey.api.dto.request;

import com.innercircle.survey.common.constants.SurveyConstants;
import com.innercircle.survey.common.domain.QuestionType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 설문 항목 생성 요청 DTO
 */
@Schema(description = "설문 항목 생성 요청")
@Getter
@NoArgsConstructor
public class CreateQuestionRequest {

    @Schema(description = "항목 제목", example = "전반적인 서비스에 만족하십니까?")
    @NotBlank(message = "설문 항목 제목은 필수입니다.")
    @Size(min = SurveyConstants.Question.MIN_TITLE_LENGTH,
          max = SurveyConstants.Question.MAX_TITLE_LENGTH,
          message = "설문 항목 제목은 {min}자 이상 {max}자 이하여야 합니다.")
    private String title;

    @Schema(description = "항목 설명", example = "서비스 전반에 대한 만족도를 평가해 주세요.")
    @Size(max = SurveyConstants.Question.MAX_DESCRIPTION_LENGTH,
          message = "설문 항목 설명은 {max}자 이하여야 합니다.")
    private String description;

    @Schema(description = "항목 입력 형태", example = "SINGLE_CHOICE")
    @NotNull(message = "설문 항목 입력 형태는 필수입니다.")
    private QuestionType questionType;

    @Schema(description = "필수 여부", example = "true")
    private boolean required = false;

    @Schema(description = "선택 옵션 (선택형 질문만)", 
            example = "[\"매우 불만족\", \"불만족\", \"보통\", \"만족\", \"매우 만족\"]")
    private List<String> options;

    /**
     * 생성자
     */
    public CreateQuestionRequest(String title, String description, QuestionType questionType, 
                               boolean required, List<String> options) {
        this.title = title;
        this.description = description;
        this.questionType = questionType;
        this.required = required;
        this.options = options;
    }

    /**
     * 선택형 질문 여부 확인
     */
    public boolean isChoiceType() {
        return questionType != null && questionType.isChoiceType();
    }

    /**
     * 옵션 검증
     */
    public void validateOptions() {
        if (questionType == null) {
            return;
        }

        if (questionType.isChoiceType()) {
            if (options == null || options.isEmpty()) {
                throw new IllegalArgumentException("선택형 질문에는 최소 1개의 옵션이 필요합니다.");
            }
            
            if (options.size() > SurveyConstants.Question.MAX_OPTIONS_COUNT) {
                throw new IllegalArgumentException("선택 옵션은 최대 " + SurveyConstants.Question.MAX_OPTIONS_COUNT + "개까지 가능합니다.");
            }
            
            for (String option : options) {
                if (option == null || option.trim().isEmpty()) {
                    throw new IllegalArgumentException("선택 옵션은 빈 값일 수 없습니다.");
                }
                if (option.length() > SurveyConstants.Question.MAX_OPTION_LENGTH) {
                    throw new IllegalArgumentException("선택 옵션은 " + SurveyConstants.Question.MAX_OPTION_LENGTH + "자 이하여야 합니다.");
                }
            }
        } else {
            if (options != null && !options.isEmpty()) {
                throw new IllegalArgumentException("텍스트 입력형 질문에는 선택 옵션을 설정할 수 없습니다.");
            }
        }
    }
}
