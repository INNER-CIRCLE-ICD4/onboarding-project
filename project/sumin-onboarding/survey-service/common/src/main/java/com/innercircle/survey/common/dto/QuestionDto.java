package com.innercircle.survey.common.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

//설문 받을 항목 DTO
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class QuestionDto {
    //항목 이름
    @NotNull(message = "질문 제목은 필수입니다.")
    private String title;
    //항목 설명
    private String description;
    //항목 입력 형태
    @NotNull(message = "질문 입력 형태는 필수입니다.")
    private String type;
    //항목 필수 여부
    private boolean required;
    //선택 질문 옵션
    private List<String> options;

}
