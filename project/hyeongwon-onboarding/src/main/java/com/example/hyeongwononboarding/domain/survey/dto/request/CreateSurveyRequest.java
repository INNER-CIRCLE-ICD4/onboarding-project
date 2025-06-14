package com.example.hyeongwononboarding.domain.survey.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.List;

/**
 * 설문조사 생성 요청 DTO
 * - 설문조사 생성시 전달되는 요청 정보를 담는 클래스입니다.
 */
@Getter
@NoArgsConstructor
public class CreateSurveyRequest {
    @NotBlank(message = "설문조사 제목은 필수입니다.")
    @Size(max = 255, message = "제목은 255자를 초과할 수 없습니다.")
    private String title;

    @Size(max = 1000, message = "설명은 1000자를 초과할 수 없습니다.")
    private String description;

    @NotEmpty(message = "질문은 최소 1개 이상이어야 합니다.")
    @Size(max = 10, message = "질문은 최대 10개까지 가능합니다.")
    @Valid
    private List<CreateQuestionRequest> questions;

    public CreateSurveyRequest(String title, String description, List<CreateQuestionRequest> questions) {
        this.title = title;
        this.description = description;
        this.questions = questions;
    }
}
