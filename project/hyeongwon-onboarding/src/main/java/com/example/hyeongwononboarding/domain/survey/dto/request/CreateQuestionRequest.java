package com.example.hyeongwononboarding.domain.survey.dto.request;

import com.example.hyeongwononboarding.domain.survey.entity.QuestionInputType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.List;

/**
 * 설문조사 질문 생성 요청 DTO
 * - 설문 생성시 각 질문의 정보를 담는 클래스입니다.
 */
@Getter
@NoArgsConstructor
public class CreateQuestionRequest {
    @NotBlank(message = "질문명은 필수입니다.")
    @Size(max = 255, message = "질문명은 255자를 초과할 수 없습니다.")
    private String name;

    @Size(max = 500, message = "질문 설명은 500자를 초과할 수 없습니다.")
    private String description;

    @NotNull(message = "입력 형태는 필수입니다.")
    private QuestionInputType inputType;

    private Boolean isRequired = false;

    @NotNull(message = "질문 순서는 필수입니다.")
    @Positive(message = "질문 순서는 양수여야 합니다.")
    private Integer order;

    private List<String> options;

    public CreateQuestionRequest(String name, String description, QuestionInputType inputType, Boolean isRequired, Integer order, List<String> options) {
        this.name = name;
        this.description = description;
        this.inputType = inputType;
        this.isRequired = isRequired != null ? isRequired : false;
        this.order = order;
        this.options = options;
    }
}
