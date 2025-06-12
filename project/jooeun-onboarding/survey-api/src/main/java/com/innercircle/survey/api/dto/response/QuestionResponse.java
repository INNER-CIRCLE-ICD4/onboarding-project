package com.innercircle.survey.api.dto.response;

import com.innercircle.survey.common.domain.QuestionType;
import com.innercircle.survey.domain.survey.SurveyQuestion;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.List;

/**
 * 설문 항목 응답 DTO
 */
@Schema(description = "설문 항목 응답")
@Getter
public class QuestionResponse {

    @Schema(description = "항목 ID", example = "01HK123ABC456DEF789GHI012K")
    private final String id;

    @Schema(description = "항목 제목", example = "전반적인 서비스에 만족하십니까?")
    private final String title;

    @Schema(description = "항목 설명", example = "서비스 전반에 대한 만족도를 평가해 주세요.")
    private final String description;

    @Schema(description = "항목 입력 형태", example = "SINGLE_CHOICE")
    private final QuestionType questionType;

    @Schema(description = "필수 여부", example = "true")
    private final boolean required;

    @Schema(description = "표시 순서", example = "1")
    private final int displayOrder;

    @Schema(description = "선택 옵션 (선택형 질문만)", 
            example = "[\"매우 불만족\", \"불만족\", \"보통\", \"만족\", \"매우 만족\"]")
    private final List<String> options;

    /**
     * 도메인 엔티티로부터 응답 DTO 생성
     */
    public QuestionResponse(SurveyQuestion question) {
        this.id = question.getId();
        this.title = question.getTitle();
        this.description = question.getDescription();
        this.questionType = question.getQuestionType();
        this.required = question.isRequired();
        this.displayOrder = question.getDisplayOrder();
        this.options = question.getQuestionType().isChoiceType() ? 
                      List.copyOf(question.getOptions()) : null;
    }
}
