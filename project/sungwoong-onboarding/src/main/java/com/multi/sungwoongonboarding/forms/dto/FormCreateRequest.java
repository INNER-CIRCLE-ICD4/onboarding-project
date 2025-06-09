package com.multi.sungwoongonboarding.forms.dto;

import com.multi.sungwoongonboarding.questions.dto.QuestionCreateRequest;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@Builder
@RequiredArgsConstructor
public class FormCreateRequest {

    @NotBlank(message = "제목은 필수 입력 항목입니다.")
    private final String title;

    private final String description;

    @Min(value = 1, message = "최소 1개 이상의 질문이 필요합니다.")
    @Max(value = 10, message = "최대 10개 이하의 질문을 작성할 수 있습니다.")
    private final List<QuestionCreateRequest> questionCreateRequests;

}
