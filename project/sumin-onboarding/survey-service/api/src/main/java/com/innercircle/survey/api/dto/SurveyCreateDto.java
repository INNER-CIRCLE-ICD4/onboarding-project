package com.innercircle.survey.api.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

//설문 생성 요청 DTO
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SurveyCreateDto {
    //설문조사 이름
    @NotNull(message = "설문조사 제목은 필수입니다.")
    private String title;
    //설문조사 설명
    private String description;
    //설문 받을 항목
    @Size(min = 1, max = 10, message = "설문조사 항목은 1개 이상 10개 이하로 입력 가능합니다.")
    @Valid //내부 객체 검증 시 필요
    private List<QuestionDto> questions;
}
